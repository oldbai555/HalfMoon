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
import com.oldbai.halfmoon.vo.ArticleView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


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
            commentMapper.updateById(comment);
            return ResponseResult.SUCCESS("置顶成功.");
        } else if (Constants.Comment.STATE_TOP.equals(state)) {
            comment.setState(Constants.Comment.STATE_PUBLISH);
            commentMapper.updateById(comment);
            return ResponseResult.SUCCESS("取消置顶.");
        } else {
            return ResponseResult.FAILED("评论状态非法.");
        }
    }

    /**
     * 发表评论
     *
     * @param comment 评论
     * @return
     */
    @Override
    public ResponseResult postComment(Comment comment) {
        //检查用户是否有登录
        User sobUser = userService.checkUser();
        if (sobUser == null) {
            return ResponseResult.NO_LOGIN();
        }
        //检查内容
        String articleId = comment.getArticleId();
        if (StringUtils.isEmpty(articleId)) {
            return ResponseResult.FAILED("文章ID不可以为空.");
        }
        ArticleView article = articleViewMapper.selectById(articleId);
        if (article == null) {
            return ResponseResult.FAILED("文章不存在.");
        }
        String content = comment.getContent();
        if (StringUtils.isEmpty(content)) {
            return ResponseResult.FAILED("评论内容不可以为空.");
        }
        if (StringUtils.isEmpty(comment.getState())){
            comment.setState("1");
        }
        //补全内容
        comment.setUserAvatar(sobUser.getAvatar());
        comment.setUserName(sobUser.getUserName());
        comment.setUserId(sobUser.getId());
        //保存入库
        commentMapper.insert(comment);
        //返回结果
        return ResponseResult.SUCCESS("评论成功");
    }

    /**
     * 获取文章的评论
     * 评论的排序策略：
     * 最基本的就按时间排序-->升序和降序-->先发表的在前面或者后发表的在前面
     * <p>
     * 置顶的：一定在前最前面
     * <p>
     * 后发表的：前单位时间内会排在前面，过了此单位时间，会按点赞量和发表时间进行排序
     *
     * @param articleId
     * @param page
     * @param size
     * @return
     */
    @Override
    public ResponseResult listCommentByArticleId(String articleId, int page, int size) {
        page = Utils.getPage(page);
        size = Utils.getSize(size);
        Page<Comment> commentPage = new Page<>(page, size);
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("state", "create_time");
        Page<Comment> all = commentMapper.selectPage(commentPage, queryWrapper);

        return ResponseResult.SUCCESS("评论列表获取成功.", all);
    }
}
