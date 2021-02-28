package com.oldbai.halfmoon.controller.portal;

import com.oldbai.halfmoon.response.ResponseResult;
import com.oldbai.halfmoon.service.CategoryService;
import com.oldbai.halfmoon.service.FriendLinkService;
import com.oldbai.halfmoon.service.ImgLooperService;
import com.oldbai.halfmoon.service.SettingsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 网站信息
 */
@Api(description = "门户-网站")
@RestController
@CrossOrigin
@RequestMapping("/halfmoon/portal/web_size_info")
public class WebSettingController {

    @Autowired
    CategoryService categoryService;
    @Autowired
    FriendLinkService friendLinkService;
    @Autowired
    SettingsService sizeInfoService;
    @Autowired
    ImgLooperService loopService;

    /**
     * 获取分类
     *
     * @return
     */
    @ApiOperation("获取分类")
    @GetMapping("/categories")
    public ResponseResult getCategories() {
        return categoryService.listCategories();
    }

    /**
     * 获取网站标题
     *
     * @return
     */
    @ApiOperation("获取网站标题")
    @GetMapping("/title")
    public ResponseResult getTitle() {
        return sizeInfoService.getWebSizeTitle();
    }

    /**
     * 统计访问页，每个页面都统一次，PV，page view.
     * 直接增加一个访问量，可以刷量
     * 根据ip进行一些过滤，可以集成第三方的一个统计工具
     * //
     * 递增的统计
     * 统计信息，通过redis来统计，数据也会保存在mysql里
     * 不会每次都更新到Mysql里，当用户去获取访问量的时候，会更新一次
     * 平时的调用，只增加redis里的访问量
     * <p>
     * redis时机：每个页面访问的时候，如果不在从mysql中读取数据，写到redis里
     * 如果，就自增
     * <p>
     * mysql的时机，用户读取网站总访问量的时候，我们就读取一redis的，并且更新到mysql中
     * 如果redis里没有，那就读取mysql写到reds里的
     * <p>
     * TODO  更新访问量没完成
     */
    @ApiOperation("更新访问量")
    @GetMapping("/view_count")
    public void updateViewCount() {
        sizeInfoService.updateViewCount();
    }

    @ApiOperation("获取访问量")
    @GetMapping("/get/view_count")
    public ResponseResult getViewCount() {
        return sizeInfoService.getSizeViewCount();
    }

    /**
     * 获取网站SEO信息
     *
     * @return
     */
    @ApiOperation("获取网站SEO信息")
    @GetMapping("/seo")
    public ResponseResult getSeo() {
        return sizeInfoService.getSeoInfo();
    }

    /**
     * 获取轮播图信息
     *
     * @return
     */
    @ApiOperation("获取轮播图信息")
    @GetMapping("/loop")
    public ResponseResult getLoops() {
        return loopService.listLoops();
    }

    /**
     * 获取友情链接信息
     *
     * @return
     */
    @ApiOperation("获取友情链接信息")
    @GetMapping("/friend_link")
    public ResponseResult getFriendLinks() {
        return friendLinkService.listFriendLinks();
    }

}
