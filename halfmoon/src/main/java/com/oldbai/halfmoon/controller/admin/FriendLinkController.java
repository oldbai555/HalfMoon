package com.oldbai.halfmoon.controller.admin;


import com.oldbai.halfmoon.entity.FriendLink;
import com.oldbai.halfmoon.interceptor.CheckTooFrequentCommit;
import com.oldbai.halfmoon.response.ResponseResult;
import com.oldbai.halfmoon.service.FriendLinkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author oldbai
 * @since 2021-02-14
 */
@Api(description = "管理中心友情链接模块API")
@RestController
@CrossOrigin
@RequestMapping("/halfmoon/friend-link")
public class FriendLinkController {

    @Autowired
    private FriendLinkService friendLinkService;

    /**
     * 增
     *
     * @return
     */
    @CheckTooFrequentCommit
    @ApiOperation("添加友情链接")
    @PreAuthorize("@permission.adminPermission()")
    @PostMapping("/upload")
    public ResponseResult uploadFriendLink(@RequestBody FriendLink friendLink) {
        return friendLinkService.addFriendLink(friendLink);
    }

    /**
     * 删
     *
     * @param friendLinkId
     * @return
     */
    @ApiOperation("删除友情链接")
    @PreAuthorize("@permission.adminPermission()")
    @GetMapping("/delete/{friendLinkId}")
    public ResponseResult deleteFriendLink(@PathVariable("friendLinkId") String friendLinkId) {
        return friendLinkService.deleteFriendLink(friendLinkId);
    }

    /**
     * 改
     *
     * @param friendLinkId
     * @return
     */
    @CheckTooFrequentCommit
    @ApiOperation("更新友情链接")
    @PreAuthorize("@permission.adminPermission()")
    @PostMapping("/update/{friendLinkId}")
    public ResponseResult updateFriendLink(@PathVariable("friendLinkId") String friendLinkId,
                                           @RequestBody FriendLink friendLink) {
        return friendLinkService.updateFriendLink(friendLinkId, friendLink);
    }

    /**
     * 查
     *
     * @param friendLinkId
     * @return
     */
    @ApiOperation("获取友情链接")
    @PreAuthorize("@permission.adminPermission()")
    @GetMapping("/get_image/{friendLinkId}")
    public ResponseResult getFriendLink(@PathVariable("friendLinkId") String friendLinkId) {
        return friendLinkService.getFriendLink(friendLinkId);
    }

    /**
     * 获取集合
     *
     * @return
     */
    @ApiOperation("获取友情链接集合")
    @PreAuthorize("@permission.adminPermission()")
    @GetMapping("/list")
    public ResponseResult listFriendLinks() {
        return friendLinkService.listFriendLinks();
    }




}

