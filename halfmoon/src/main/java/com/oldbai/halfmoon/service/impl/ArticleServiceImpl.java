package com.oldbai.halfmoon.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.BCUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oldbai.halfmoon.entity.Article;
import com.oldbai.halfmoon.entity.Comment;
import com.oldbai.halfmoon.entity.Labels;
import com.oldbai.halfmoon.entity.User;
import com.oldbai.halfmoon.mapper.*;
import com.oldbai.halfmoon.response.ResponseResult;
import com.oldbai.halfmoon.service.ArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oldbai.halfmoon.service.UserService;
import com.oldbai.halfmoon.util.Constants;
import com.oldbai.halfmoon.util.Utils;
import com.oldbai.halfmoon.vo.ArticleView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author oldbai
 * @since 2021-02-14
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    ArticleMapper articleMapper;
    @Autowired
    ArticleViewMapper articleViewMapper;
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    LabelsMapper labelsMapper;
    @Autowired
    UserService userService;

    /**
     * {
     * "categoryId": "805487266016395264",
     * "content": "测试内容1",
     * "labels": "测试标签-文章",
     * "state": "1",
     * "summary": "测试啊",
     * "title": "测试啊1",
     * "type": "0"
     * }
     */
    @Transactional
    @Override
    public ResponseResult postArticle(Article article) {
        //检查用户，获取到用户对象
        User sobUser = userService.checkUser();
        //未登录
        if (sobUser == null) {
            return ResponseResult.NO_LOGIN();
        }
        //检查数据
        //title、分类ID、内容、类型、摘要、标签
        String title = article.getTitle();
        if (StringUtils.isEmpty(title)) {
            return ResponseResult.FAILED("标题不可以为空.");
        }

        //2种，草稿和发布
        //获取文章状态
        String state = article.getState();
        //只接受两种状态 发布和草稿
        if (!Constants.Article.STATE_PUBLISH.equals(state) &&
                !Constants.Article.STATE_DRAFT.equals(state)) {
            //不支持此操作
            return ResponseResult.FAILED("不支持此操作");
        }
        //获取文章类型
        String type = article.getType();
        if (StringUtils.isEmpty(type)) {
            return ResponseResult.FAILED("类型不可以为空.");
        }
        //（0表示富文本，1表示markdown）
        if (!"0".equals(type) && !"1".equals(type)) {
            return ResponseResult.FAILED("类型格式不对.");
        }
        //以下检查是发布的检查，草稿不需要检查
        if (Constants.Article.STATE_PUBLISH.equals(state)) {
            //检查标题
            if (title.length() > Constants.Article.TITLE_MAX_LENGTH) {
                return ResponseResult.FAILED("文章标题不可以超过" + Constants.Article.TITLE_MAX_LENGTH + "个字符");
            }
            //检查内容
            String content = article.getContent();
            if (StringUtils.isEmpty(content)) {
                return ResponseResult.FAILED("内容不可为空.");
            }
            //检查摘要
            String summary = article.getSummary();
            if (StringUtils.isEmpty(summary)) {
                return ResponseResult.FAILED("摘要不可以为空.");
            }
            if (summary.length() > Constants.Article.SUMMARY_MAX_LENGTH) {
                return ResponseResult.FAILED("摘要不可以超出" + Constants.Article.SUMMARY_MAX_LENGTH + "个字符.");
            }
            //检查标签
            String labels = article.getLabels();
            //标签-标签1-标签2
            if (StringUtils.isEmpty(labels)) {
                return ResponseResult.FAILED("标签不可以为空.");
            }
        }
        //获取文章的ID
        String articleId = article.getId();
        if (StringUtils.isEmpty(articleId)) {
            //新内容,数据里没有的
            //补充数据：ID、创建时间、用户ID、更新时间
            article.setId(IdUtil.randomUUID() + "");
//            article.setCreateTime(new Date());
            article.setUserId(sobUser.getId());
            articleMapper.insert(article);
        } else {
            //更新内容，对状态进行处理，如果已经是发布的，则不能再保存为草稿
            Article articleFromDb = articleMapper.selectById(articleId);
            if (Constants.Article.STATE_PUBLISH.equals(articleFromDb.getState()) &&
                    Constants.Article.STATE_DRAFT.equals(state)) {
                //已经发布了，只能更新，不能保存草稿
                return ResponseResult.FAILED("已发布文章不支持成为草稿.");
            }
        }
        article.setUserId(sobUser.getId());
//        article.setUpdateTime(new Date());
        //保存到数据库里
        articleMapper.updateById(article);
        //TODO:保存到搜索的数据库里
        //打散标签，入库，统计
        this.setupLabels(article.getLabels());
        //返回结果,只有一种case使用到这个ID
        //如果要做程序自动保存成草稿（比如说每30秒保存一次，就需要加上这个ID了，否则会创建多个Item）
        return ResponseResult.SUCCESS(Constants.Article.STATE_DRAFT.equals(state) ? "草稿保存成功" :
                "文章发表成功.", article.getId());
    }

    @Transactional
    void setupLabels(String labels) {
        List<String> labelList = new ArrayList<>();
        if (labels.contains("-")) {
            labelList.addAll(Arrays.asList(labels.split("-")));
        } else {
            labelList.add(labels);
        }
        //入库 并统计
        for (String label : labelList) {
            int result = labelsMapper.updateCountByName(label);
            if (result == 0) {
                Labels targetLabel = new Labels();
                targetLabel.setCount(1);
                targetLabel.setName(label);
                labelsMapper.insert(targetLabel);
            }
        }
    }

    @Transactional
    @Override
    public ResponseResult deleteArticleById(String articleId) {
        Article article = articleMapper.selectById(articleId);
        this.deleteLabels(article.getLabels());
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("article_id", articleId);
        commentMapper.delete(queryWrapper);
        int result = articleMapper.deleteById(articleId);
        if (result > 0) {
            return ResponseResult.SUCCESS("文章删除成功.");
        }
        return ResponseResult.FAILED("文章不存在.");
    }

    @Transactional
    void deleteLabels(String labels) {
        List<String> labelList = new ArrayList<>();
        if (labels.contains("-")) {
            labelList.addAll(Arrays.asList(labels.split("-")));
        } else {
            labelList.add(labels);
        }
        //入库 并统计
        for (String label : labelList) {
            int result = labelsMapper.deleteCountByName(label);
        }
    }


    /**
     * 更新文章内容
     * <p>
     * 该接口只支持修改内容：标题、内容、标签、分类，摘要
     *
     * @param articleId 文章ID
     * @param article   文章
     * @return
     */
    @Transactional
    @Override
    public ResponseResult updateArticle(String articleId, Article article) {
        //先找出来
        Article articleFromDb = articleMapper.selectById(articleId);
        if (articleFromDb == null) {
            return ResponseResult.FAILED("文章不存在.");
        }
        //内容修改
        String title = article.getTitle();
        if (!StringUtils.isEmpty(title)) {
            articleFromDb.setTitle(title);
        }

        String summary = article.getSummary();
        if (!StringUtils.isEmpty(summary)) {
            articleFromDb.setSummary(summary);
        }

        String content = article.getContent();
        if (!StringUtils.isEmpty(content)) {
            articleFromDb.setContent(content);
        }

        String label = article.getLabels();
        if (!StringUtils.isEmpty(label)) {
            //TODO 字符串切割 。找到不一样的标签，并且添加进去，获取删除
            articleFromDb.setLabels(label);
        }

        String categoryId = article.getCategoryId();
        if (!StringUtils.isEmpty(categoryId)) {
            articleFromDb.setCategoryId(categoryId);
        }
        articleFromDb.setCover(article.getCover());
        articleMapper.updateById(articleFromDb);
        //返回结果
        return ResponseResult.SUCCESS("文章更新成功.");
    }

    /**
     * 如果有审核机制：审核中的文章-->只有管理员和作者自己可以获取
     * 有草稿、删除、置顶的、已经发布的
     * 删除的不能获取、其他都可以获取
     *
     * @param articleId
     * @return
     */
    @Transactional
    @Override
    public ResponseResult getArticleById(String articleId) {
        //查询出文章
        Article article = articleMapper.selectById(articleId);
        if (article == null) {
            return ResponseResult.FAILED("文章不存在.");
        }
        //判断文章状态
        String state = article.getState();
        if (Constants.Article.STATE_PUBLISH.equals(state) ||
                Constants.Article.STATE_TOP.equals(state)) {
            //可以返回
            return ResponseResult.SUCCESS("获取文章成功.", article);
        }
        //如果是删除/草稿，需要管理角色
        User sobUser = userService.checkUser();
        if (sobUser == null || !Constants.User.ROLE_ADMIN.equals(sobUser.getRoles())) {
            return ResponseResult.NO_PERMISSION();
        }
        //返回结果
        return ResponseResult.SUCCESS("获取文章成功.", article);
    }

    /**
     * 这里管理中，获取文章列表
     *
     * @param page       页码
     * @param size       每一页数量
     * @param keyword    标题关键字（搜索关键字）
     * @param categoryId 分类ID
     * @param state      状态：已经删除、草稿、已经发布的、置顶的
     * @return
     */
    @Transactional
    @Override
    public ResponseResult listArticles(int page, int size, String keyword, String categoryId, String state) {
        //TODO 可以不把文章内容返回回去
        //处理一下size 和page
        page = Utils.getPage(page);
        size = Utils.getSize(size);
        //创建分页和排序条件
        QueryWrapper<ArticleView> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(state)) {
            queryWrapper.eq("state", state);
        }
        if (!StringUtils.isEmpty(categoryId)) {
            queryWrapper.eq("category_id", categoryId);
        }
        if (!StringUtils.isEmpty(keyword)) {
            queryWrapper.like("title", keyword);
        }
        queryWrapper.orderByDesc("create_time");
        Page<ArticleView> viewPage = new Page<>(page, size);
        //开始查询
        Page<ArticleView> all = articleViewMapper.selectPage(viewPage, queryWrapper);
        //处理查询条件
        //返回结果
        return ResponseResult.SUCCESS("获取文章列表成功.", all);
    }

    @Transactional
    @Override
    public ResponseResult deleteArticleByState(String articleId) {
        Article article = articleMapper.selectById(articleId);
        this.deleteLabels(article.getLabels());
        article.setState("0");
        int result = articleMapper.updateById(article);
        if (result > 0) {
            return ResponseResult.SUCCESS("文章删除成功.");
        }
        return ResponseResult.FAILED("文章不存在.");
    }

    @Transactional
    @Override
    public ResponseResult topArticle(String articleId) {
        //必须已经发布的，才可以置顶
        Article article = articleMapper.selectById(articleId);
        if (article == null) {
            return ResponseResult.FAILED("文章不存在.");
        }
        String state = article.getState();
        if (Constants.Article.STATE_PUBLISH.equals(state)) {
            article.setState(Constants.Article.STATE_TOP);
            articleMapper.updateById(article);
            return ResponseResult.SUCCESS("文章置顶成功.");
        }
        if (Constants.Article.STATE_TOP.equals(state)) {
            article.setState(Constants.Article.STATE_PUBLISH);
            articleMapper.updateById(article);
            return ResponseResult.SUCCESS("已取消置顶.");
        }
        return ResponseResult.FAILED("不支持该操作.");
    }
}
