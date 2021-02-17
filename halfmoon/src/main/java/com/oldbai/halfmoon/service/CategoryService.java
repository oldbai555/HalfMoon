package com.oldbai.halfmoon.service;

import com.oldbai.halfmoon.entity.Category;
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
public interface CategoryService extends IService<Category> {

    ResponseResult addCategory(Category category);

    ResponseResult deleteCategory(String categoryId);

    ResponseResult updateCategory(String categoryId, Category category);

    ResponseResult getCategory(String categoryId);

    ResponseResult listCategories();
}
