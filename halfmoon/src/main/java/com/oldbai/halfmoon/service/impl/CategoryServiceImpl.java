package com.oldbai.halfmoon.service.impl;

import com.oldbai.halfmoon.entity.Category;
import com.oldbai.halfmoon.mapper.CategoryMapper;
import com.oldbai.halfmoon.service.CategoryService;
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
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

}
