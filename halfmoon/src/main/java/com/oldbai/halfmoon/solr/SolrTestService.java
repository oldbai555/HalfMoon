package com.oldbai.halfmoon.solr;

import com.oldbai.halfmoon.entity.Article;
import com.oldbai.halfmoon.mapper.ArticleMapper;
import com.oldbai.halfmoon.util.Constants;
import com.vladsch.flexmark.ext.jekyll.tag.JekyllTagExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.ext.toc.SimTocExtension;
import com.vladsch.flexmark.ext.toc.TocExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;
import io.swagger.annotations.Api;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class SolrTestService {

    @Autowired
    private SolrClient solrClient;

    public void solrAdd() {
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField("id", "005f490b2ff5da2483bd5398ae140b1d");
        doc.addField("blog_view_count", 10);
        doc.addField("blog_title", "测试啊12345");
        doc.addField("blog_content", "测试内容12345");
        doc.addField("blog_create_timme", new Date());
        doc.addField("blog_labels", "测试标签-文章");
        doc.addField("blog_url", "www.baidu.com");
        doc.addField("blog_category_id", "b316fb0a5dd63ec177dc572f28f24b90");
        try {
            solrClient.add(doc);
            solrClient.commit();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void solrUpdate() {
        //只要ID一样就是修改
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField("id", "005f490b2ff5da2483bd5398ae140b1d");
        doc.addField("blog_view_count", 10);
        doc.addField("blog_title", "修改了测试啊12345");
        doc.addField("blog_content", "修改了测试内容12345");
        doc.addField("blog_create_timme", new Date());
        doc.addField("blog_labels", "测试标签-文章");
        doc.addField("blog_url", "www.baidu.com");
        doc.addField("blog_category_id", "b316fb0a5dd63ec177dc572f28f24b90");
        try {
            solrClient.add(doc);
            solrClient.commit();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void solrDelete() {
        //单独删除一条
        try {
            solrClient.deleteById("005f490b2ff5da2483bd5398ae140b1d");
            solrClient.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void solrDeleteAll() {
        try {
            //删除所有
            solrClient.deleteByQuery("*");
            solrClient.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Autowired
    private ArticleMapper articleMapper;

    /**
     * 向solr添加数据库中所有文章
     */
    public void importAll() {
        List<Article> articleList = articleMapper.selectList(null);
        SolrInputDocument doc = null;
        for (Article article : articleList) {
            doc = new SolrInputDocument();
            doc.addField("id", article.getId());
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
    }

}
