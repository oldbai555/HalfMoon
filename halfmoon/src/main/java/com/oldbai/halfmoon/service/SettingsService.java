package com.oldbai.halfmoon.service;

import com.oldbai.halfmoon.entity.Settings;
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
public interface SettingsService extends IService<Settings> {

    ResponseResult getWebSizeTitle();

    ResponseResult putWebSizeTitle(String title);

    ResponseResult getSeoInfo();

    ResponseResult putSeoInfo(String keywords, String description);

    ResponseResult getSizeViewCount();

    void updateViewCount();
}
