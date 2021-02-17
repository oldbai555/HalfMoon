package com.oldbai.halfmoon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.oldbai.halfmoon.entity.Settings;
import com.oldbai.halfmoon.mapper.SettingsMapper;
import com.oldbai.halfmoon.response.ResponseResult;
import com.oldbai.halfmoon.service.SettingsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oldbai.halfmoon.util.Constants;
import com.oldbai.halfmoon.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author oldbai
 * @since 2021-02-14
 */
@Service
public class SettingsServiceImpl extends ServiceImpl<SettingsMapper, Settings> implements SettingsService {

    @Autowired
    private SettingsMapper settingsMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Transactional
    @Override
    public ResponseResult getWebSizeTitle() {
        QueryWrapper<Settings> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("`key`", Constants.Settings.WEB_SIZE_TITLE);
        Settings title = settingsMapper.selectOne(queryWrapper);
        return ResponseResult.SUCCESS("获取网站title成功.", title);
    }

    @Transactional
    @Override
    public ResponseResult putWebSizeTitle(String title) {
        if (StringUtils.isEmpty(title)) {
            return ResponseResult.FAILED("网站标题不可以为空.");
        }
        QueryWrapper<Settings> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("`key`", Constants.Settings.WEB_SIZE_TITLE);
        Settings titleFromDb = settingsMapper.selectOne(queryWrapper);
        if (titleFromDb == null) {
            titleFromDb = new Settings();
            titleFromDb.setKey(Constants.Settings.WEB_SIZE_TITLE);
        }
        titleFromDb.setValue(title);
        settingsMapper.insert(titleFromDb);
        return ResponseResult.SUCCESS("网站Title更新成功.");
    }

    @Transactional
    @Override
    public ResponseResult getSeoInfo() {
        QueryWrapper<Settings> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("`key`", Constants.Settings.WEB_SIZE_DESCRIPTION);
        Settings description = settingsMapper.selectOne(queryWrapper);
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("`key`", Constants.Settings.WEB_SIZE_KEYWORDS);
        Settings keyWords = settingsMapper.selectOne(queryWrapper);
        Map<String, String> result = new HashMap<>();
        if (description != null && keyWords != null) {
            result.put(description.getKey(), description.getValue());
            result.put(keyWords.getKey(), keyWords.getValue());
        }
        return ResponseResult.SUCCESS("获取SEO信息成功.", result);
    }

    @Transactional
    @Override
    public ResponseResult putSeoInfo(String keywords, String description) {
        //判断
        if (StringUtils.isEmpty(keywords)) {
            return ResponseResult.FAILED("关键字不可以为空.");
        }
        if (StringUtils.isEmpty(description)) {
            return ResponseResult.FAILED("网站描述不可以为空.");
        }
        QueryWrapper<Settings> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("`key`", Constants.Settings.WEB_SIZE_DESCRIPTION);
        Settings descriptionFromDb = settingsMapper.selectOne(queryWrapper);
        if (descriptionFromDb == null) {
            descriptionFromDb = new Settings();
            descriptionFromDb.setKey(Constants.Settings.WEB_SIZE_DESCRIPTION);
        }
        descriptionFromDb.setValue(description);
        settingsMapper.insert(descriptionFromDb);
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("`key`", Constants.Settings.WEB_SIZE_KEYWORDS);
        Settings keyWordsFromDb = settingsMapper.selectOne(queryWrapper);
        if (keyWordsFromDb == null) {
            keyWordsFromDb = new Settings();
            keyWordsFromDb.setKey(Constants.Settings.WEB_SIZE_KEYWORDS);
        }
        keyWordsFromDb.setValue(keywords);
        settingsMapper.insert(keyWordsFromDb);
        return ResponseResult.SUCCESS("更新SEO信息成功.");
    }

    /**
     * 这个是全网站的访问量，要做得细一点，还得分来源
     * 这里只统计浏览量，只统计文章的浏览量，提供一个浏览量的统计接口（页面级的）
     *
     * @return 浏览量
     */
    @Transactional
    @Override
    public ResponseResult getSizeViewCount() {
        //先从redis里拿出来
        String viewCountStr = (String) redisUtil.get(Constants.Settings.WEB_SIZE_VIEW_COUNT);
        QueryWrapper<Settings> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("`key`", Constants.Settings.WEB_SIZE_VIEW_COUNT);
        Settings viewCount = settingsMapper.selectOne(queryWrapper);
        if (viewCount == null) {
            viewCount = this.initViewItem();
            settingsMapper.insert(viewCount);
        }
        if (StringUtils.isEmpty(viewCountStr)) {
            viewCountStr = viewCount.getValue();
            redisUtil.set(Constants.Settings.WEB_SIZE_VIEW_COUNT, viewCountStr);
        } else {
            //把redis里的更新到数据里
            viewCount.setValue(viewCountStr);
            settingsMapper.updateById(viewCount);
        }
        Map<String, Integer> result = new HashMap<>();
        result.put(viewCount.getKey(), Integer.valueOf(viewCount.getValue()));
        return ResponseResult.SUCCESS("获取网站浏览量成功.", result);
    }

    private Settings initViewItem() {
        Settings settings = new Settings();
        settings.setKey(Constants.Settings.WEB_SIZE_VIEW_COUNT);
        settings.setValue("1");
        return settings;
    }
}
