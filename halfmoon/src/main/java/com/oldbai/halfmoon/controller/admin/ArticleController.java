package com.oldbai.halfmoon.controller.admin;


import com.oldbai.halfmoon.entity.Article;
import com.oldbai.halfmoon.interceptor.CheckTooFrequentCommit;
import com.oldbai.halfmoon.response.ResponseResult;
import com.oldbai.halfmoon.service.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author oldbai
 * @since 2021-02-14
 */
@Api(description = "管理中心文章模块API")
@RestController
@RequestMapping("/halfmoon/article")
public class ArticleController {

    @Autowired
    ArticleService articleService;

    /**
     * 发表文章
     * <p>
     * 后期可以去做一些定时发布的功能
     * 如果是多人博客系统，得考虑审核的问题--->成功,通知，审核不通过，也可通知
     * <p>
     * 保存成草稿
     * 1、用户手动提交：会发生页面跳转-->提交完即可
     * 2、代码自动提交，每隔一段时间就会提交-->不会发生页面跳转-->多次提交-->如果没有唯一标识，会就重添加到数据库里
     * <p>
     * 不管是哪种草稿-->必须有标题
     * <p>
     * 方案一：每次用户发新文章之前-->先向后台请求一个唯一文章ID
     * 如果是更新文件，则不需要请求这个唯一的ID
     * <p>
     * 方案二：可以直接提交，后台判断有没有ID,如果没有ID，就新创建，并且ID作为此次返回的结果
     * 如果有ID，就修改已经存在的内容。
     * <p>
     * 推荐做法：
     * 自动保存草稿，在前端本地完成，也就是保存在本地。
     * 如果是用户手动提交的，就提交到后台
     *
     *
     * <p>
     * 防止重复提交（网络卡顿的时候，用户点了几次提交）：
     * 可以通过ID的方式
     * 通过token_key的提交频率来计算，如果30秒之内有多次提交，只有最前的一次有效
     * 其他的提交，直接return,提示用户不要太频繁操作.
     * <p>
     * 前端的处理：点击了提交以后，禁止按钮可以使用，等到有响应结果，再改变按钮的状态.
     *
     * @param article
     * @return
     */
    @CheckTooFrequentCommit
    @PreAuthorize("@permission.adminPermission()")
    @ApiOperation("发表文章")
    @PostMapping("/add_article")
    public ResponseResult addArticle(@RequestBody Article article) {
        return articleService.postArticle(article);
    }

    /**
     * 如果是多用户，用户不可以删除，删除只是修改状态
     * 管理可以删除
     * <p>
     * 做成真的删除
     *
     * @param articleId
     * @return
     */
    @PreAuthorize("@permission.adminPermission()")
    @ApiOperation("删除文章")
    @GetMapping("/delete/{articleId}")
    public ResponseResult deleteArticle(@PathVariable("articleId") String articleId) {
        return articleService.deleteArticleById(articleId);
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
    @CheckTooFrequentCommit
    @PreAuthorize("@permission.adminPermission()")
    @ApiOperation("更新文章")
    @PostMapping("/update/{articleId}")
    public ResponseResult updateArticle(@PathVariable("articleId") String articleId,
                                        @RequestBody Article article) {
        return articleService.updateArticle(articleId, article);
    }

    /**
     * 获取文章
     * <p>
     * 获取文章详情，我们要设置一下viewCount，另外则是做缓存处理。
     * <p>
     * 缓存我们一般会统一处理。因为要考虑添加缓存和删除缓存
     * <p>
     * 比如说，我们在访问文章的时候，添加缓存，先从缓存中获取，如果没有再去数据库中获取，获取到了再添加到缓存里。
     * <p>
     * 如果我们更新文章、置顶文章、删除文章、就需要去清除缓存。等待下次获取文章详情的时候，重新加入缓存里。
     * <p>
     * 还要注意的是权限，管理员可以拿到任何状态的文章，作者可以拿到除了删除状态的文章，其他人只能拿到发布，或者置顶的文章。
     *
     * @param articleId
     * @return
     */
    @PreAuthorize("@permission.adminPermission()")
    @ApiOperation("获取文章")
    @GetMapping("/get_article/{articleId}")
    public ResponseResult getArticle(@PathVariable("articleId") String articleId) {
        return articleService.getArticleById(articleId);
    }

    /**
     * 获取文章集合
     * 这里的条件包括状态呀，标题搜索，分类这些。
     *
     * @param page
     * @param size
     * @return
     */
    @PreAuthorize("@permission.adminPermission()")
    @ApiOperation("获取文章集合")
    @GetMapping("/list/{page}/{size}")
    public ResponseResult listArticles(@PathVariable("page") int page,
                                       @PathVariable("size") int size,
                                       @RequestParam(value = "state", required = false) String state,
                                       @RequestParam(value = "keyword", required = false) String keyword,
                                       @RequestParam(value = "categoryId", required = false) String categoryId) {
        return articleService.listArticles(page, size, keyword, categoryId, state);
    }

    /**
     * 单独更新
     * 更新状态
     *
     * @return
     */
    @ApiOperation("更新文章状态")
    @PreAuthorize("@permission.adminPermission()")
    @PostMapping("/state/{articleId}")
    public ResponseResult updateArticleState(@PathVariable("articleId") String articleId) {
        return articleService.deleteArticleByState(articleId);
    }

    /**
     * 文章置顶
     * <p>
     * 文章置顶是独立开来的，因为我们希望不管用户翻到哪一页内容，我们都把这些文章置顶。
     * <p>
     * 多次调用：文章置顶会取消，取消了的则会置顶
     *
     * @return
     */
    @ApiOperation("文章置顶")
    @PreAuthorize("@permission.adminPermission()")
    @PostMapping("/top/{articleId}")
    public ResponseResult topArticleState(@PathVariable("articleId") String articleId) {
        return articleService.topArticle(articleId);
    }


}

