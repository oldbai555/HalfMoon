package com.oldbai.halfmoon.util;

import com.oldbai.halfmoon.entity.User;
import com.oldbai.halfmoon.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 简单版权限管理
 *
 * @author 老白
 */
@Service("permission")
public class PermissionCheckService {

    @Autowired
    private UserService userService;

    /**
     * 判断是不是管理员
     *
     * @return
     */
    public boolean adminPermission() {
        //拿到 request  response
        // 获取到当前权限所有的角色，进行角色对比即可确定权限
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        //如果token返回false
        String token = CookieUtils.getCookie(request, Constants.User.COOKIE_TOKE_KEY);
        //没有令牌的key , 没有登录，不用往下执行了
        if (StringUtils.isEmpty(token)) {
            return false;
        }
        User sobUser = userService.checkUser();
        if (sobUser == null || StringUtils.isEmpty(sobUser.getRoles())) {
            return false;
        }
        if (Constants.User.ROLE_ADMIN.equals(sobUser.getRoles())) {
            return true;
        }
        return false;
    }

}