package com.oldbai.halfmoon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.oldbai.halfmoon.entity.FriendLink;
import com.oldbai.halfmoon.entity.User;
import com.oldbai.halfmoon.mapper.FriendLinkMapper;
import com.oldbai.halfmoon.response.ResponseResult;
import com.oldbai.halfmoon.service.FriendLinkService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oldbai.halfmoon.service.UserService;
import com.oldbai.halfmoon.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author oldbai
 * @since 2021-02-14
 */
@Service
public class FriendLinkServiceImpl extends ServiceImpl<FriendLinkMapper, FriendLink> implements FriendLinkService {

    @Autowired
    FriendLinkMapper friendLinkMapper;
    @Autowired
    UserService userService;

    /**
     * 添加友情连接
     *
     * @param friendLink
     * @return
     */
    @Override
    public ResponseResult addFriendLink(FriendLink friendLink) {
        //判断数据
        String url = friendLink.getUrl();
        if (StringUtils.isEmpty(url)) {
            return ResponseResult.FAILED("链接Url不可以为空.");
        }
        String logo = friendLink.getLogo();
        if (StringUtils.isEmpty(logo)) {
            return ResponseResult.FAILED("logo不可以为空.");
        }
        String name = friendLink.getName();
        if (StringUtils.isEmpty(name)) {
            return ResponseResult.FAILED("对方网站名不可以为空.");
        }
        //补全数据
//        friendLink.setId(idWorker.nextId() + "");
//        friendLink.setUpdateTime(new Date());
//        friendLink.setCreateTime(new Date());
        friendLink.setState("1");
        //保存数据
        friendLinkMapper.insert(friendLink);
        //返回结果
        return ResponseResult.FAILED("添加成功.");
    }

    @Override
    public ResponseResult deleteFriendLink(String friendLinkId) {
        int result = friendLinkMapper.deleteById(friendLinkId);
        if (result == 0) {
            return ResponseResult.FAILED("删除失败.");
        }
        return ResponseResult.SUCCESS("删除成功.");
    }

    /**
     * 更新内容有什么：
     * logo
     * 对方网站的名称
     * url
     * order
     *
     * @param friendLinkId
     * @param friendLink
     * @return
     */
    @Override
    public ResponseResult updateFriendLink(String friendLinkId, FriendLink friendLink) {
        FriendLink friendLinkFromDb = friendLinkMapper.selectById(friendLinkId);
        if (friendLinkFromDb == null) {
            return ResponseResult.FAILED("更新失败.");
        }
        String logo = friendLink.getLogo();
        if (!StringUtils.isEmpty(logo)) {
            friendLinkFromDb.setLogo(logo);
        }
        String name = friendLink.getName();
        if (!StringUtils.isEmpty(name)) {
            friendLinkFromDb.setName(name);
        }
        String url = friendLink.getUrl();
        if (!StringUtils.isEmpty(url)) {
            friendLinkFromDb.setUrl(url);
        }
        friendLinkFromDb.setOrder(friendLink.getOrder());
        friendLinkFromDb.setUpdateTime(new Date());
        //保存数据
        friendLinkMapper.updateById(friendLinkFromDb);
        return ResponseResult.SUCCESS("更新成功.");
    }

    @Override
    public ResponseResult getFriendLink(String friendLinkId) {
        FriendLink friendLink = friendLinkMapper.selectById(friendLinkId);
        if (friendLink == null) {
            return ResponseResult.FAILED("友情链接不存");
        }
        return ResponseResult.SUCCESS("获取成功", friendLink);
    }

    @Override
    public ResponseResult listFriendLinks() {
        //创建条件
        List<FriendLink> all;
        User sobUser = userService.checkUser();
        QueryWrapper<FriendLink> friendLinkQueryWrapper = null;
        if (sobUser == null || !Constants.User.ROLE_ADMIN.equals(sobUser.getRoles())) {
            //只能获取到正常的category
            friendLinkQueryWrapper = new QueryWrapper<>();
            friendLinkQueryWrapper.eq("state","1").orderByDesc("update_time");
            all = friendLinkMapper.selectList(friendLinkQueryWrapper);
        } else {
            //查询
            friendLinkQueryWrapper = new QueryWrapper<>();
            friendLinkQueryWrapper.orderByDesc("update_time");
            all = friendLinkMapper.selectList(friendLinkQueryWrapper);
        }
        return ResponseResult.SUCCESS("获取列表成功.", all);
    }
}
