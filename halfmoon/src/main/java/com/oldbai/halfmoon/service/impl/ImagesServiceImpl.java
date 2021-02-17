package com.oldbai.halfmoon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oldbai.halfmoon.entity.Images;
import com.oldbai.halfmoon.entity.User;
import com.oldbai.halfmoon.mapper.ImagesMapper;
import com.oldbai.halfmoon.oss.OssService;
import com.oldbai.halfmoon.response.ResponseResult;
import com.oldbai.halfmoon.service.ImagesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oldbai.halfmoon.service.UserService;
import com.oldbai.halfmoon.util.Constants;
import com.oldbai.halfmoon.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author oldbai
 * @since 2021-02-14
 */
@Slf4j
@Service
public class ImagesServiceImpl extends ServiceImpl<ImagesMapper, Images> implements ImagesService {

    @Autowired
    private ImagesMapper imagesMapper;
    @Autowired
    private OssService ossService;
    @Autowired
    private UserService userService;

    HttpServletRequest request = null;
    HttpServletResponse response = null;

    public void getRequestAndResponse() {
        this.request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        this.response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();

    }

    /**
     * 上传图片文件
     *
     * @param file
     * @return
     */
    @Override
    public ResponseResult uploadImage(MultipartFile file) {
        //判断是否有文件
        if (file == null) {
            return ResponseResult.FAILED("图片不可以为空.");
        }
        //判断文件类型，我们只支持图片上传，比如说：png，jpg，gif
        String contentType = file.getContentType();
        String fileName = file.getOriginalFilename();

        log.info("contentType == > " + contentType);
        boolean isType = false;
        if (StringUtils.isEmpty(contentType)) {
            return ResponseResult.FAILED("图片格式错误.");
        } else if (Constants.ImageType.TYPE_JGP_WITH_PREFIX.equals(contentType)) {
            isType = true;
        } else if (Constants.ImageType.TYPE_GIF_WITH_PREFIX.equals(contentType)) {
            isType = true;
        } else if (Constants.ImageType.TYPE_PNG_WITH_PREFIX.equals(contentType)) {
            isType = true;
        }else if (Constants.ImageType.TYPE_JPEG_WITH_PREFIX.equals(contentType)) {
            isType = true;
        }
        if (!isType) {
            return ResponseResult.FAILED("图片格式错误.");
        }

        //获取上传文件  MultipartFile
        //返回上传到oss的路径
        String url = ossService.uploadFileAvatar(file);
        Images image = new Images();
        User checkUser = userService.checkUser();
        image.setName(fileName);
        image.setContentType(contentType);
        image.setUserId(checkUser.getId());
        image.setUrl(url);
        image.setState("1");
        imagesMapper.insert(image);
        Map<String, String> map = new HashMap<>();
        map.put("url", url);
        //返回结果
        return ResponseResult.SUCCESS("上传成功", map);
    }

    @Override
    public ResponseResult deleteById(String imageId) {
        int result = imagesMapper.deleteById(imageId);
        if (result > 0) {
            return ResponseResult.SUCCESS("删除成功.");
        }
        return ResponseResult.FAILED("图片不存在.");
    }

    @Override
    public ResponseResult viewImage(String imageId) {
        Images oneById = imagesMapper.selectById(imageId);
        if (StringUtils.isEmpty(oneById)) {
            return ResponseResult.FAILED("图片不存在......");
        }
        return ResponseResult.SUCCESS(oneById);
    }

    /**
     * 获取图片列表
     *
     * @param page
     * @param size
     * @return
     */
    @Override
    public ResponseResult listImages(int page, int size) {
        User checkUser = userService.checkUser();
        page = Utils.getPage(page);
        size = Utils.getSize(size);
        //创建条件
        Page<Images> imagesPage = new Page<>(page, size);
        QueryWrapper<Images> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time").eq("user_id", checkUser.getId()).eq("state", "1");
        Page<Images> all = imagesMapper.selectPage(imagesPage, wrapper);
        return ResponseResult.SUCCESS("获取成功", all);
    }
}
