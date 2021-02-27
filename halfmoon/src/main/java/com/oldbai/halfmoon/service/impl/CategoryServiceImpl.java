package com.oldbai.halfmoon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oldbai.halfmoon.entity.Category;
import com.oldbai.halfmoon.entity.User;
import com.oldbai.halfmoon.mapper.CategoryMapper;
import com.oldbai.halfmoon.response.ResponseResult;
import com.oldbai.halfmoon.service.CategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.oldbai.halfmoon.service.UserService;
import com.oldbai.halfmoon.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author oldbai
 * @since 2021-02-14
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private UserService userService;

    @Transactional
    @Override
    public ResponseResult addCategory(Category category) {
        //TODO 还可以进行完善
        //先检查数据
        // 必须的数据有：
        //分类名称、分类的pinyin、顺序、描述
        if (StringUtils.isEmpty(category.getName())) {
            return ResponseResult.FAILED("分类名称不可以为空.");
        }
        if (StringUtils.isEmpty(category.getPinyin())) {
            return ResponseResult.FAILED("分类拼音不可以为空.");
        }
        if (StringUtils.isEmpty(category.getDescription())) {
            return ResponseResult.FAILED("分类描述不可以为空.");
        }
        //补全数据
//        category.setId(idWorker.nextId() + "");
        category.setStatus("1");
        category.setOrder(0);
//        category.setCreateTime(new Date());
//        category.setUpdateTime(new Date());
        //保存数据
        categoryMapper.insert(category);
        //返回结果
        return ResponseResult.SUCCESS("添加分类成功");
    }

    @Override
    public ResponseResult deleteCategory(String categoryId) {
        int result = categoryMapper.deleteById(categoryId);
        if (result == 0) {
            return ResponseResult.FAILED("该分类不存在.");
        }
        return ResponseResult.SUCCESS("删除分类成功.");
    }

    @Override
    public ResponseResult updateCategory(String categoryId, Category category) {
        //1.找出来
        Category one = categoryMapper.selectById(categoryId);
        if (StringUtils.isEmpty(one)) {
            return ResponseResult.FAILED("分类不存在");
        }
        //2.对内容进行判断
        if (!StringUtils.isEmpty(category.getName())) {
            one.setName(category.getName());
        }
        if (!StringUtils.isEmpty(category.getDescription())) {
            one.setDescription(category.getDescription());
        }
        if (!StringUtils.isEmpty(category.getPinyin())) {
            one.setPinyin(category.getPinyin());
        }
        if (!StringUtils.isEmpty(category.getOrder())) {
            one.setOrder(category.getOrder());
        }
        //3.保存数据
        categoryMapper.updateById(one);
        //4.返回结果
        return ResponseResult.SUCCESS("保存成功...");
    }

    @Override
    public ResponseResult getCategory(String categoryId) {
        Category category = categoryMapper.selectById(categoryId);
        if (category == null) {
            return ResponseResult.FAILED("分类不存在.");
        }
        return ResponseResult.SUCCESS("获取分类成功.", category);
    }

    @Override
    public ResponseResult listCategories() {
        //创建条件

        //判断用户角色，普通用户 未登录用户 只能获取到正常的category
        //管理员账号 可以拿到所有的分类
        User checkUser = userService.checkUser();
        List<Category> all = null;
        QueryWrapper<Category> queryWrapper = null;
        if (StringUtils.isEmpty(checkUser) || !Constants.User.ROLE_ADMIN.equals(checkUser.getRoles())) {
            //只能获取到正常的category
            queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("status", 1).orderByDesc("update_time");
            all = categoryMapper.selectList(queryWrapper);
        } else {
            //查询
            queryWrapper = new QueryWrapper<>();
            queryWrapper.orderByDesc("update_time");
            all = categoryMapper.selectList(queryWrapper);
        }
        //返回结果
        return ResponseResult.SUCCESS("获取分类列表成功.", all);
    }


}
