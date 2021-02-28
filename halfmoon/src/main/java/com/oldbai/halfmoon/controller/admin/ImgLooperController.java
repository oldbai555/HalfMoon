package com.oldbai.halfmoon.controller.admin;


import com.oldbai.halfmoon.entity.ImgLooper;
import com.oldbai.halfmoon.response.ResponseResult;
import com.oldbai.halfmoon.service.ImgLooperService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author oldbai
 * @since 2021-02-14
 */
@Api(description = "管理中心轮播图模块API")
@RestController
@CrossOrigin
@RequestMapping("/halfmoon/img-looper")
public class ImgLooperController {
    @Autowired
    ImgLooperService loopService;

    /**
     * 增
     * 对于增删改查的套路就是：
     * 权限有没有
     * 不能为空的数据进行检查
     * 数据格式进行检查
     * 补充数据
     * 保存数据
     * 返回结果
     * <p>
     * 添加轮播图前，要上传图片，然后返回访问的ID，然后拼接成url
     * <p>
     * 这样子创建轮播图的bean，就可以提交了。
     *
     * @return
     */

    @ApiOperation("添加轮播图")
    @PreAuthorize("@permission.adminPermission()")
    @PostMapping("/add_loop")
    public ResponseResult uploadLoop(@RequestBody ImgLooper looper) {
        return loopService.addLoop(looper);
    }

    /**
     * 删
     * 话不多说，直接删除，前端可以弹窗警告
     *
     * @param loopId
     * @return
     */
    @ApiOperation("删除轮播图")
    @PreAuthorize("@permission.adminPermission()")
    @GetMapping("/delete/{loopId}")
    public ResponseResult deleteLoop(@PathVariable("loopId") String loopId) {
        return loopService.deleteLoop(loopId);
    }

    /**
     * 改
     *
     * @param loopId
     * @param looper
     * @return
     */
    @ApiOperation("修改轮播图")
    @PreAuthorize("@permission.adminPermission()")
    @PostMapping("/update/{loopId}")
    public ResponseResult updateLoop(@PathVariable("loopId") String loopId,
                                     @RequestBody ImgLooper looper) {
        return loopService.updateLoop(loopId, looper);
    }

    /**
     * 查
     * 获取轮播图，这个是获取单个
     * <p>
     * 其实，有没有都可以的
     * <p>
     * 使用场景就是获取完轮播图列表，如果你要更新轮播图，你可以调用此接口获取到它的内容。
     * <p>
     * 如果不获取也可以，直接从列表里获取，因为我们的列表内容是全的。
     *
     * @param loopId
     * @return
     */
    @ApiOperation("查询轮播图")
    @PreAuthorize("@permission.adminPermission()")
    @GetMapping("/get_loop/{loopId}")
    public ResponseResult getLoop(@PathVariable("loopId") String loopId) {
        return loopService.getLoop(loopId);
    }

    /**
     * 获取集合
     * 除了前端，后台也是要获取轮播图列表的，因为我们要管理它呀
     *
     * @return
     */
    @ApiOperation("获取轮播图列表")
    @GetMapping("/list")
    public ResponseResult listLoops() {
        return loopService.listLoops();
    }
}

