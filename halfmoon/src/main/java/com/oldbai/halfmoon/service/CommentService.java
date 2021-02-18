package com.oldbai.halfmoon.service;

import com.oldbai.halfmoon.entity.Comment;
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
public interface CommentService extends IService<Comment> {

    ResponseResult deleteCommentById(String commentId);

    ResponseResult listComments(int page, int size);

    ResponseResult topComment(String commentId);
}
