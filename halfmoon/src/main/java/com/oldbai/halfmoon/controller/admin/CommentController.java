package com.oldbai.halfmoon.controller.admin;


import com.oldbai.halfmoon.response.ResponseResult;
import com.oldbai.halfmoon.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author oldbai
 * @since 2021-02-14
 */
@Api(description = "管理中心评论模块API")
@RestController
@RequestMapping("/halfmoon/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    /**
     * 删
     * <p>
     * 删除评论的话，直接物理删除即可
     * <p>
     * 门户可以删除，管理中心也可以删除。
     * <p>
     * 管理员可以删除任意的，但是，门户的用户，只能删除自己的。
     *
     * @param commentId
     * @return
     */
    @ApiOperation("删除评论")
    @PreAuthorize("@permission.adminPermission()")
    @GetMapping("/delete/{commentId}")
    public ResponseResult deleteComment(@PathVariable("commentId") String commentId) {
        return commentService.deleteCommentById(commentId);
    }

    /**
     * 获取集合
     * <p>
     * 这个获取只需要根据时间排序即可，如果同学们要做得全一点，可以跟前面的文章一样，条件查询即可。
     * <p>
     * 根据条件获取评论列表，比如说按时间，比如说按文章，比如说按状态.
     *
     * @param page
     * @param size
     * @return
     */
    @ApiOperation("获取评论列表")
    @PreAuthorize("@permission.adminPermission()")
    @GetMapping("/list")
    public ResponseResult listComments(@RequestParam("page") int page,
                                       @RequestParam("size") int size) {
        return commentService.listComments(page, size);
    }

    /**
     * 评论置顶
     * <p>
     * 这里是后台，没发表评论，可以操作评论。
     * <p>
     * 这个是置顶，评论内容由门户那这发表。
     * <p>
     * 评论置顶的话，查询的时候，我们就需要以这个作为排序了
     *
     * @return
     */
    @ApiOperation("评论置顶")
    @PreAuthorize("@permission.adminPermission()")
    @GetMapping("/top/{commentId}")
    public ResponseResult topComments(@PathVariable("commentId") String commentId) {
        return commentService.topComment(commentId);
    }

}

