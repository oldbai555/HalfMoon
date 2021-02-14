package com.oldbai.halfmoon.service.impl;

import com.oldbai.halfmoon.entity.User;
import com.oldbai.halfmoon.mapper.UserMapper;
import com.oldbai.halfmoon.service.UserService;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
