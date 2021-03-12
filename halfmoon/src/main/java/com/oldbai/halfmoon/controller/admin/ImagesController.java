package com.oldbai.halfmoon.controller.admin;


import com.oldbai.halfmoon.entity.FriendLink;
import com.oldbai.halfmoon.entity.Images;
import com.oldbai.halfmoon.interceptor.CheckTooFrequentCommit;
import com.oldbai.halfmoon.response.ResponseResult;
import com.oldbai.halfmoon.service.FriendLinkService;
import com.oldbai.halfmoon.service.ImagesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author oldbai
 * @since 2021-02-14
 */
@Api(description = "管理中心图片模块API")
@RestController
@CrossOrigin
@RequestMapping("/halfmoon/images")
public class ImagesController {
    @Autowired
    ImagesService imageService;

    /**
     * 增
     * 关于图片上传：
     * 一般来说，现在比较常用的是对象存储 --> 很简单，看文档就会了
     * 使用 Nginx + fastDFS ==>  fastDFS ==> 处理文件上传，Nginx --> 复制处理文件访问
     * 直接进行文件操作
     * <p>
     * 返回url给前端
     *
     * @return
     */
    @ApiOperation("上传图片")
    @PostMapping("/upload")
    @CheckTooFrequentCommit
    public ResponseResult uploadImage(@RequestParam("file") MultipartFile file) {

        return imageService.uploadImage(file);
    }

    /**
     * 删
     *
     * @param imageId
     * @return
     */
    @ApiOperation("删除图片")
    @PreAuthorize("@permission.adminPermission()")
    @GetMapping("/delete/{imageId}")
    public ResponseResult deleteImage(@PathVariable("imageId") String imageId) {
        return  imageService.deleteById(imageId);
    }


    /**
     * 查
     * 获取单张图片，前端进行解析一下得到 URL 然后展示出来
     *
     * @param imageId
     * @return
     */
    @ApiOperation("查图片")
    @GetMapping("/get_image/{imageId}")
    public ResponseResult getImage(@PathVariable("imageId") String imageId) {
        return imageService.viewImage(imageId);
    }

    /**
     * 获取集合
     *
     * @param page
     * @param size
     * @return
     */
    @ApiOperation("获取图片列表")
    @GetMapping("/list")
    public ResponseResult listImage(@RequestParam("page") int page,
                                    @RequestParam("size") int size) {
        return imageService.listImages(page, size);
    }


}

