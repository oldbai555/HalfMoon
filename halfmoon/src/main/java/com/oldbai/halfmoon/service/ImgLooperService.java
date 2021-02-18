package com.oldbai.halfmoon.service;

import com.oldbai.halfmoon.entity.ImgLooper;
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
public interface ImgLooperService extends IService<ImgLooper> {

    ResponseResult addLoop(ImgLooper looper);

    ResponseResult deleteLoop(String loopId);

    ResponseResult updateLoop(String loopId, ImgLooper looper);

    ResponseResult getLoop(String loopId);

    ResponseResult listLoops();
}
