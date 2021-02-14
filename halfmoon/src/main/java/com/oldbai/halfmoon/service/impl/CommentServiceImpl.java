package com.oldbai.halfmoon.service.impl;

import com.oldbai.halfmoon.entity.Comment;
import com.oldbai.halfmoon.mapper.CommentMapper;
import com.oldbai.halfmoon.service.CommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author oldbai
 * @since 2021-02-14
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

}
