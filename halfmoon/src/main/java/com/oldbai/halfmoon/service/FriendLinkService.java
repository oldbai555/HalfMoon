package com.oldbai.halfmoon.service;

import com.oldbai.halfmoon.entity.FriendLink;
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
public interface FriendLinkService extends IService<FriendLink> {

    ResponseResult addFriendLink(FriendLink friendLink);

    ResponseResult deleteFriendLink(String friendLinkId);

    ResponseResult updateFriendLink(String friendLinkId, FriendLink friendLink);

    ResponseResult getFriendLink(String friendLinkId);

    ResponseResult listFriendLinks();
}
