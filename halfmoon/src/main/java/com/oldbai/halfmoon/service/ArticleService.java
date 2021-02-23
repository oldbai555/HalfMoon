package com.oldbai.halfmoon.service;

import com.oldbai.halfmoon.entity.Article;
import com.baomidou.mybatisplus.extension.service.IService;
import com.oldbai.halfmoon.response.ResponseResult;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author oldbai
 * @since 2021-02-14
 */
public interface ArticleService extends IService<Article> {

    ResponseResult postArticle(Article article);

    ResponseResult deleteArticleById(String articleId);

    ResponseResult updateArticle(String articleId, Article article);

    ResponseResult getArticleById(String articleId);

    ResponseResult listArticles(int page, int size, String keyword, String categoryId, String state);

    ResponseResult deleteArticleByState(String articleId);

    ResponseResult topArticle(String articleId);

    ResponseResult listTopArticles();

    ResponseResult listLabels(int size);

    ResponseResult listRecommendArticle(String articleId, int size);

    ResponseResult listArticlesByLabel(int page, int size, String label);

    ResponseResult doSearch(String keyword, int page, int size, String categoryId, Integer sort);
}
