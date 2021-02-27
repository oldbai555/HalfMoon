package com.oldbai.halfmoon.solr;

import com.oldbai.halfmoon.entity.Article;
import com.oldbai.halfmoon.response.ResponseResult;
import com.oldbai.halfmoon.util.Constants;
import com.oldbai.halfmoon.util.Utils;
import com.oldbai.halfmoon.vo.PageList;
import com.oldbai.halfmoon.vo.SearchResult;
import com.vladsch.flexmark.ext.jekyll.tag.JekyllTagExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.ext.toc.SimTocExtension;
import com.vladsch.flexmark.ext.toc.TocExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * solr服务类
 * 时机
 * <p>
 * 搜索内容添加：
 * 文章发表的时候，也就是状态为 1
 * <p>
 * 搜索内容删除：
 * 文章删除，包括物理删除和逻辑删除
 * <p>
 * 搜索内容更新：
 * TODO
 * 阅读量更新
 */
@Service
public class SolrServiceImpl implements SolrService {

    @Autowired
    private SolrClient solrClient;


    @Override
    public ResponseResult doSearch(String keyword, int page, int size, String categoryId, Integer sort) {
        //1、检查page和size
        page = Utils.getPage(page);
        size = Utils.getSize(size);
        SolrQuery solrQuery = new SolrQuery();
        //2、分页设置
        //先设置每页的数量
        solrQuery.setRows(size);
        //设置开始的位置
        //找个规律
        //第1页 -- > 0
        //第2页 == > size
        //第3页 == > 2*size
        //第4页 == > 3*size
        //第n页 == > (n-1)*size
        int start = (page - 1) * size;
        solrQuery.setStart(start);
        //solrQuery.set("start", start);
        //3、设置搜索条件
        //关键字
        solrQuery.set("df", "search_item");
        //条件过滤
        if (StringUtils.isEmpty(keyword)) {
            solrQuery.set("q", "*");
        } else {
            solrQuery.set("q", keyword);
        }
        //排序
        //排序有四个：根据时间的升序（1）和降序（2），根据浏览量的升序（3）和降序（4）
        if (sort != null) {
            if (sort == 1) {
                solrQuery.setSort("blog_create_timme", SolrQuery.ORDER.asc);
            } else if (sort == 2) {
                solrQuery.setSort("blog_create_timme", SolrQuery.ORDER.desc);
            } else if (sort == 3) {
                solrQuery.setSort("blog_view_count", SolrQuery.ORDER.asc);
            } else if (sort == 4) {
                solrQuery.setSort("blog_view_count", SolrQuery.ORDER.desc);
            }
        }
        //分类
        if (!StringUtils.isEmpty(categoryId)) {
            solrQuery.setFilterQueries("blog_category_id:" + categoryId);
        }
        //关键字高亮
        solrQuery.setHighlight(true);
        solrQuery.addHighlightField("blog_title,blog_content");
        solrQuery.setHighlightSimplePre("<font color='red'>");
        solrQuery.setHighlightSimplePost("</font>");
        solrQuery.setHighlightFragsize(500);
        //设置返回字段
        //blog_content,blog_create_time,blog_labels,blog_url,blog_title,blog_view_count
        solrQuery.addField("id,blog_content,blog_create_timme,blog_labels,blog_url,blog_title,blog_view_count");
        //
        //4、搜索
        try {
            //4.1、处理搜索结果
            QueryResponse result = solrClient.query(solrQuery);
            //获取到高亮内容
            Map<String, Map<String, List<String>>> highlighting = result.getHighlighting();
            //把数据转成bean类
            List<SearchResult> resultList = result.getBeans(SearchResult.class);
            //结果列表
            for (SearchResult item : resultList) {
                Map<String, List<String>> stringListMap = highlighting.get(item.getId());
                List<String> blogContent = stringListMap.get("blog_content");
                if (!StringUtils.isEmpty(blogContent)) {
                    item.setBlogContent(blogContent.get(0));
                }
                List<String> blogTitle = stringListMap.get("blog_title");
                if (!StringUtils.isEmpty(blogTitle)) {
                    item.setBlogTitle(blogTitle.get(0));
                }
            }
            //5、返回搜索结果
            //包含内容
            //列表、页面、每页数量
            long numFound = result.getResults().getNumFound();
            PageList<SearchResult> pageList = new PageList<>(page, numFound, size);
            pageList.setContents(resultList);
            //返回结果
            return ResponseResult.SUCCESS("搜索成功.", pageList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseResult.FAILED("搜索失败，请稍后重试.");
    }

    @Override
    public void addArticle(Article article) {
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField("id", article.getId());
        if (StringUtils.isEmpty(article.getViewCount())) {
            article.setViewCount(0);
        }
        doc.addField("blog_view_count", article.getViewCount());
        doc.addField("blog_title", article.getTitle());
        //对内容进行处理，去掉标签，提取纯文本内容
        //第一种是由markdown写的内容 ==> type = 1
        //第二种是有富文本内容 ==> type = 0
        //如果type等于 1 ，需要转换成 html
        //再由 html 转换成 纯文本
        //如果type等于 0 ，提取出纯文本
        String type = article.getType();
        String html = null;
        if (Constants.Article.TYPE_MARKDOWN.equals(type)) {
            //转成HTML
            // markdown to html
            MutableDataSet options = new MutableDataSet().set(Parser.EXTENSIONS, Arrays.asList(
                    TablesExtension.create(),
                    JekyllTagExtension.create(),
                    TocExtension.create(),
                    SimTocExtension.create()
            ));
            Parser parser = Parser.builder(options).build();
            HtmlRenderer renderer = HtmlRenderer.builder(options).build();
            Node document = parser.parse(article.getContent());
            html = renderer.render(document);
        } else if (Constants.Article.TYPE_RICH_TEXT.equals(type)) {
            //富文本
            html = article.getContent();
        }
        //到了这里不管是什么都变成了 html
        //HTML转Text
        String content = Jsoup.parse(html).text();
        doc.addField("blog_content", content);
        if (StringUtils.isEmpty(article.getCreateTime())) {
            article.setCreateTime(new Date());
        }
        doc.addField("blog_create_timme", article.getCreateTime());
        doc.addField("blog_labels", article.getLabels());
        doc.addField("blog_url", "null");
        doc.addField("blog_category_id", article.getCategoryId());
        try {
            solrClient.add(doc);
            solrClient.commit();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteArticle(String id) {
        //单独删除一条
        try {
            solrClient.deleteById(id);
            solrClient.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateArticle(Article article) {
        this.addArticle(article);
    }


}
