package com.oldbai.halfmoon.controller.portal;

import com.oldbai.halfmoon.entity.Comment;
import com.oldbai.halfmoon.response.ResponseResult;
import com.oldbai.halfmoon.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(description = "门户-评论")
@RestController
@RequestMapping("/portal/comment")
@CrossOrigin
public class CommentPortalController {
    @Autowired
    CommentService commentService;

    /**
     * 增
     *
     * @return
     */
    @ApiOperation("添加评论")
    @PostMapping("/add_comment")
    public ResponseResult addComment(@RequestBody Comment comment) {
        return commentService.postComment(comment);
    }

    /**
     * 删
     *
     * @param commentId
     * @return
     */
    @ApiOperation("删除评论")
    @GetMapping("/delete/{commentId}")
    public ResponseResult deleteComment(@PathVariable("commentId") String commentId) {
        return commentService.deleteCommentById(commentId);
    }

    /**
     * 获取评论集合
     *
     * @param articleId
     * @return
     */
    @ApiOperation("获取评论集合")
    @GetMapping("/list/{articleId}/{page}/{size}")
    public ResponseResult listComments(@PathVariable("articleId") String articleId, @PathVariable("page") int page,@PathVariable("size") int size) {
        return commentService.listCommentByArticleId(articleId, page, size);
    }
}
