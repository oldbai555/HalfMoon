package com.oldbai.halfmoon.controller.admin;


import com.oldbai.halfmoon.entity.Category;
import com.oldbai.halfmoon.interceptor.CheckTooFrequentCommit;
import com.oldbai.halfmoon.mapper.CategoryMapper;
import com.oldbai.halfmoon.response.ResponseResult;
import com.oldbai.halfmoon.service.CategoryService;
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
@Api(description = "管理中心分类模块API")
@RestController
@RequestMapping("/halfmoon/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    /**
     * 添加分类
     *
     * @return
     */
    @CheckTooFrequentCommit
    @ApiOperation("添加分类")
    @PreAuthorize("@permission.adminPermission()")
    @PostMapping("/add_categories")
    public ResponseResult addCategory(@RequestBody Category category) {
        return categoryService.addCategory(category);
    }

    /**
     * 删除分类
     *
     * @param categoryId
     * @return
     */
    @PreAuthorize("@permission.adminPermission()")
    @ApiOperation("删除分类")
    @GetMapping("/delete_categories/{categoryId}")
    public ResponseResult deleteCategory(@PathVariable("categoryId") String categoryId) {
        return categoryService.deleteCategory(categoryId);
    }

    /**
     * 更新分类
     *
     * @param category
     * @param categoryId
     * @return
     */
    @CheckTooFrequentCommit
    @ApiOperation("更新分类")
    @PreAuthorize("@permission.adminPermission()")
    @PostMapping("/update_categories/{categoryId}")
    public ResponseResult updateCategory(@RequestBody Category category,
                                         @PathVariable("categoryId") String categoryId) {
        return categoryService.updateCategory(categoryId, category);
    }

    /**
     * 获取分类
     * <p>
     * 使用的case:修改的时候，获取一下。填充弹窗
     * 不获取也是可以的，从列表里获取数据
     * <p>
     * 权限：管理员权限
     *
     * @param categoryId
     * @return
     */
    @PreAuthorize("@permission.adminPermission()")
    @ApiOperation("获取分类")
    @GetMapping("/get_categories/{categoryId}")
    public ResponseResult getCategory(@PathVariable("categoryId") String categoryId) {
        return categoryService.getCategory(categoryId);
    }

    /**
     * 获取分类列表
     * <p>
     * 权限：管理员权限
     *
     * @return
     */
    @ApiOperation("获取分类列表")
    @PreAuthorize("@permission.adminPermission()")
    @GetMapping("/list")
    public ResponseResult listCategory() {
        return categoryService.listCategories();
    }

}

