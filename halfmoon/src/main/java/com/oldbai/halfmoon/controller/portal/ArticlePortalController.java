package com.oldbai.halfmoon.controller.portal;

import com.oldbai.halfmoon.interceptor.CheckTooFrequentCommit;
import com.oldbai.halfmoon.response.ResponseResult;
import com.oldbai.halfmoon.service.ArticleService;
import com.oldbai.halfmoon.service.CategoryService;
import com.oldbai.halfmoon.service.ImagesService;
import com.oldbai.halfmoon.util.Constants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Api(description = "门户-文章")
@RestController
@RequestMapping("/halfmoon/portal/article")
public class ArticlePortalController {
    @Autowired
    ArticleService articleService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    ImagesService imagesService;


    /**
     * 获取文章详情
     * 权限：任意用户
     * <p>
     * 内容过滤：只允许拿置顶的，或者已经发布的
     * 其他的获取：比如说草稿、只能对应用户获取。已经删除的，只有管理员才可以获取.
     *
     * @param articleId
     * @return
     */
    @CheckTooFrequentCommit
    @ApiOperation("获取文章详情")
    @GetMapping("/get_article/{articleId}")
    public ResponseResult getArticle(@PathVariable("articleId") String articleId) {
        return articleService.getArticleById(articleId);
    }

    /**
     * 获取文章列表
     * 权限,所有用户
     * 状态:必须已经发布的,置顶的由另外一个接口获取,其他的不可以从此接口获取
     *
     * @param page
     * @param size
     * @return
     */
    @ApiOperation("获取文章列表")
    @GetMapping("/list/{page}/{size}")
    public ResponseResult listArticles(@PathVariable("page") int page,
                                       @PathVariable("size") int size) {
        return articleService.listArticles(page, size, null, null, Constants.Article.STATE_PUBLISH);
    }

    /**
     * 获取文章列表置顶
     *
     * @return
     */
    @ApiOperation("获取文章列表置顶")
    @GetMapping("/top")
    public ResponseResult getTopArticle() {
        return articleService.listTopArticles();
    }

    /**
     * 获取标签云，用户点击标签，就会通过标签获取相关的文章列表
     * 任意用户
     *
     * @param size
     * @return
     */
    @ApiOperation("获取标签云，用户点击标签，就会通过标签获取相关的文章列表")
    @GetMapping("/label/{size}")
    public ResponseResult getLabels(@PathVariable("size") int size) {
        return articleService.listLabels(size);
    }

    /**
     * 获取推荐文章
     * 通过标签来计算这个匹配度
     * 标签：有一个，或者多个（5个以内，包含5个）
     * 从里面随机拿一个标签出来--->每一次获取的推荐文章，不那么雷同，种一样就雷同了
     * 通过标签去查询类似的文章，所包含此标签的文章
     * 如果没有相关文章，则从数据中获取最新的文章的
     *
     * @param articleId
     * @return
     */
    @ApiOperation("获取推荐文章")
    @GetMapping("/recommend/{articleId}/{size}")
    public ResponseResult getRecommendArticles(@PathVariable("articleId") String articleId, @PathVariable("size") int size) {
        return articleService.listRecommendArticle(articleId, size);
    }

    /**
     * 根据标签获取文章列表
     *
     * @param label
     * @param page
     * @param size
     * @return
     */
    @ApiOperation("根据标签获取文章列表")
    @GetMapping("/list/label/{label}/{page}/{size}")
    public ResponseResult listArticleByLabel(@PathVariable("label") String label,
                                             @PathVariable("page") int page, @PathVariable("size") int size) {
        return articleService.listArticlesByLabel(page, size, label);
    }

    /**
     * 获取分类内容
     *
     * @return
     */
    @ApiOperation("获取分类内容")
    @GetMapping("/categories")
    public ResponseResult getCategories() {
        return categoryService.listCategories();
    }

    /**
     * 根据分类获取文章列表
     *
     * @param categoryId
     * @param page
     * @param size
     * @return
     */
    @ApiOperation("根据分类获取文章列表")
    @GetMapping("/list/{categoryId}/{page}/{size}")
    public ResponseResult listArticleByCategoryId(@PathVariable("categoryId") String categoryId,
                                                  @PathVariable("page") int page,
                                                  @PathVariable("size") int size) {
        return articleService.listArticles(page, size, null, categoryId, Constants.Article.STATE_PUBLISH);
    }

    /**
     * 获取图片
     *
     * @param response
     * @param imageId
     */
    @ApiOperation("获取图片")
    @GetMapping("/{imageId}")
    public ResponseResult getImage(HttpServletResponse response, @PathVariable("imageId") String imageId) {
        return imagesService.viewImage(imageId);
    }
}
