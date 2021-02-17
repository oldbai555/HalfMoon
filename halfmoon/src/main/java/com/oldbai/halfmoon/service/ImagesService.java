package com.oldbai.halfmoon.service;

import com.oldbai.halfmoon.entity.Images;
import com.baomidou.mybatisplus.extension.service.IService;
import com.oldbai.halfmoon.response.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author oldbai
 * @since 2021-02-14
 */
public interface ImagesService extends IService<Images> {

    ResponseResult uploadImage(MultipartFile file);

    ResponseResult deleteById(String imageId);

    ResponseResult viewImage(String imageId);


    ResponseResult listImages(int page, int size);
}
