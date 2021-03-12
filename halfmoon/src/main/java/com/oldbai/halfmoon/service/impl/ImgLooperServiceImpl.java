package com.oldbai.halfmoon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.oldbai.halfmoon.entity.ImgLooper;
import com.oldbai.halfmoon.entity.User;
import com.oldbai.halfmoon.mapper.ImgLooperMapper;
import com.oldbai.halfmoon.response.ResponseResult;
import com.oldbai.halfmoon.service.ImgLooperService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oldbai.halfmoon.service.UserService;
import com.oldbai.halfmoon.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
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
public class ImgLooperServiceImpl extends ServiceImpl<ImgLooperMapper, ImgLooper> implements ImgLooperService {

    @Autowired
    private UserService userService;

    @Autowired
    private ImgLooperMapper looperMapper;


    @Override
    public ResponseResult addLoop(ImgLooper looper) {
        //检查数据
        String title = looper.getTitle();
        if (StringUtils.isEmpty(title)) {
            return ResponseResult.FAILED("标题不可以为空.");
        }
        String imageUrl = looper.getImageUrl();
        if (StringUtils.isEmpty(imageUrl)) {
            return ResponseResult.FAILED("图片不可以为空.");
        }
        String targetUrl = looper.getTargetUrl();
        if (StringUtils.isEmpty(targetUrl)) {
            return ResponseResult.FAILED("跳转链接不可以为空.");
        }
        if (StringUtils.isEmpty(looper.getOrder())) {
            looper.setOrder(0);
        }
        if (StringUtils.isEmpty(looper.getState())) {
            looper.setState("1");
        }
        //补充数据
        //保存数据
        looperMapper.insert(looper);
        //返回结果
        return ResponseResult.SUCCESS("轮播图添加成功.");
    }

    @Override
    public ResponseResult deleteLoop(String loopId) {
        int result = looperMapper.deleteById(loopId);
        if (result>0){

            return ResponseResult.SUCCESS("删除成功.");
        }else {
            return ResponseResult.FAILED("删除失败，请检查是否有该轮播图.");
        }
    }

    @Override
    public ResponseResult updateLoop(String loopId, ImgLooper looper) {
        //找出来
        ImgLooper loopFromDb = looperMapper.selectById(loopId);
        if (loopFromDb == null) {
            return ResponseResult.FAILED("轮播图不存在.");
        }
        //不可以为空的，要判空
        String title = looper.getTitle();
        if (!StringUtils.isEmpty(title)) {
            loopFromDb.setTitle(title);
        }
        String targetUrl = looper.getTargetUrl();
        if (!StringUtils.isEmpty(targetUrl)) {
            loopFromDb.setTargetUrl(targetUrl);
        }
        String imageUrl = looper.getImageUrl();
        if (!StringUtils.isEmpty(imageUrl)) {
            loopFromDb.setImageUrl(imageUrl);
        }
        if (!StringUtils.isEmpty(looper.getState())) {
            loopFromDb.setState(looper.getState());
        }
        loopFromDb.setOrder(looper.getOrder());
        //可以为空的直接设置
        //保存回去
        looperMapper.updateById(loopFromDb);
        return ResponseResult.SUCCESS("轮播图更新成功.");
    }

    @Override
    public ResponseResult getLoop(String loopId) {
        ImgLooper loop = looperMapper.selectById(loopId);
        if (loop == null) {
            return ResponseResult.FAILED("轮播图不存在.");
        }
        return ResponseResult.SUCCESS("轮播图获取成功.", loop);
    }

    @Override
    public ResponseResult listLoops() {
        User checkUser = userService.checkUser();
        List<ImgLooper> all = null;
        QueryWrapper<ImgLooper> wrapper = null;
        if (StringUtils.isEmpty(checkUser) || !Constants.User.ROLE_ADMIN.equals(checkUser.getRoles())) {
            //只能获取到正常的
            wrapper = new QueryWrapper<>();
            wrapper.eq("state", "1").orderByDesc("`order`");
            all = looperMapper.selectList(wrapper);
        } else {
            //查询
            wrapper = new QueryWrapper<>();
            wrapper.orderByDesc("update_time");
            all = looperMapper.selectList(wrapper);
        }
        return ResponseResult.SUCCESS("获取轮播图列表成功.", all);
    }
}
