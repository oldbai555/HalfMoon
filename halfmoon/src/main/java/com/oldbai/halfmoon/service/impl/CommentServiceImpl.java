package com.oldbai.halfmoon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oldbai.halfmoon.entity.Comment;
import com.oldbai.halfmoon.entity.User;
import com.oldbai.halfmoon.mapper.ArticleViewMapper;
import com.oldbai.halfmoon.mapper.CommentMapper;
import com.oldbai.halfmoon.mapper.ImagesMapper;
import com.oldbai.halfmoon.response.ResponseResult;
import com.oldbai.halfmoon.service.CommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oldbai.halfmoon.service.UserService;
import com.oldbai.halfmoon.util.Constants;
import com.oldbai.halfmoon.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author oldbai
 * @since 2021-02-14
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private ImagesMapper imagesMapper;
    @Autowired
    private ArticleViewMapper articleViewMapper;

    @Override
    public ResponseResult deleteCommentById(String commentId) {
        //检查用户角色
        User sobUser = userService.checkUser();
        if (sobUser == null) {
            return ResponseResult.NO_LOGIN();
        }
        //把评论找出来，比对用户权限
        Comment comment = commentMapper.selectById(commentId);
        if (comment == null) {
            return ResponseResult.FAILED("评论不存在.");
        }
        if (sobUser.getId().equals(comment.getUserId()) ||
                //登录要判断角色
                Constants.User.ROLE_ADMIN.equals(sobUser.getRoles())) {
            commentMapper.deleteById(commentId);
            return ResponseResult.SUCCESS("评论删除成功.");
        } else {
            //没有权限
            return ResponseResult.NO_PERMISSION();
        }
    }

    @Override
    public ResponseResult listComments(int page, int size) {
        page = Utils.getPage(page);
        size = Utils.getSize(size);
        Page<Comment> commentPage = new Page<>(page, size);
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        Page<Comment> all = commentMapper.selectPage(commentPage, queryWrapper);
        return ResponseResult.SUCCESS("获取评论列表成功.", all);
    }

    @Override
    public ResponseResult topComment(String commentId) {
        Comment comment = commentMapper.selectById(commentId);
        if (comment == null) {
            return ResponseResult.FAILED("评论不存在.");
        }
        String state = comment.getState();
        if (Constants.Comment.STATE_PUBLISH.equals(state)) {
            comment.setState(Constants.Comment.STATE_TOP);
            return ResponseResult.SUCCESS("置顶成功.");
        } else if (Constants.Comment.STATE_TOP.equals(state)) {
            comment.setState(Constants.Comment.STATE_PUBLISH);
            return ResponseResult.SUCCESS("取消置顶.");
        } else {
            return ResponseResult.FAILED("评论状态非法.");
        }
    }
}
