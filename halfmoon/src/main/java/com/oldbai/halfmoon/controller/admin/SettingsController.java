package com.oldbai.halfmoon.controller.admin;


import com.oldbai.halfmoon.interceptor.CheckTooFrequentCommit;
import com.oldbai.halfmoon.response.ResponseResult;
import com.oldbai.halfmoon.service.SettingsService;
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
@Api(description = "管理中心网站信息模块API")
@RestController
@RequestMapping("/halfmoon/settings")
public class SettingsController {
    @Autowired
    private SettingsService iWebSizeInfoService;

    /**
     * 获取网站标题
     * <p>
     * 网站的标题，就是tab标签栏上显示的标题
     * <p>
     * 这个标题一般只显示在首页，如果是文章页面，则会显示：文章的名称+网站名称
     *
     * @return
     */
    @ApiOperation("获取网站标题")
    @PreAuthorize("@permission.adminPermission()")
    @GetMapping("/title")
    public ResponseResult getWebSizeTitle() {
        return iWebSizeInfoService.getWebSizeTitle();
    }


    /**
     * 更新网站标题
     *
     * @return
     */
    @CheckTooFrequentCommit
    @ApiOperation("更新网站标题")
    @PreAuthorize("@permission.adminPermission()")
    @GetMapping("/updateTitle/{title}")
    public ResponseResult uploadWebSizeInfoTitle(@PathVariable("title") String title) {
        return iWebSizeInfoService.putWebSizeTitle(title);
    }


    /**
     * 获取网站信息
     * <p>
     * 网站SEO信息，其实包括：描述、关键字和标题
     * <p>
     * 标题我们单独获取了，这里的话，我们就获取关键字和描述信息，主要是首页。
     * <p>
     * 如果是文章页面的话，就使用文章的摘要作为描述，用标签作为关键字即可。
     *
     * @return
     */
    @ApiOperation("获取网站信息")
    @PreAuthorize("@permission.adminPermission()")
    @GetMapping("/seo")
    public ResponseResult getSeoInfo() {
        return iWebSizeInfoService.getSeoInfo();
    }

    /**
     * 修改网站信息
     * <p>
     * 更新网站的SEO信息，不建议经常更新哦，一般定了就别改了。
     *
     * @return
     */
    @CheckTooFrequentCommit
    @ApiOperation("修改网站信息")
    @PreAuthorize("@permission.adminPermission()")
    @PostMapping("/update/seo")
    public ResponseResult UpdateSeoInfo(@RequestParam("keywords") String keywords,
                                        @RequestParam("description") String description) {
        return iWebSizeInfoService.putSeoInfo(keywords, description);
    }

    /**
     * 获取浏览网站信息
     * <p>
     * 网站的浏览量，
     * <p>
     * 更新时机：用户页面加载完以后，向统计接口提交一下。
     * <p>
     * 获取时机：管理中心获取访问量/前端如果有需要的话也可以获取
     *
     * @return
     */
    @ApiOperation("获取浏览网站信息")
    @GetMapping("/view_count")
    public ResponseResult getWebSizeViewCount() {
        return iWebSizeInfoService.getSizeViewCount();
    }


}

