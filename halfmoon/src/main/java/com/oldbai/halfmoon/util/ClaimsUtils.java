package com.oldbai.halfmoon.util;

import com.oldbai.halfmoon.entity.User;
import io.jsonwebtoken.Claims;

import java.util.HashMap;
import java.util.Map;

/**
 * token 的 Claims 工具类
 *
 * @author 老白
 */
public class ClaimsUtils {


    public static final String ID = "id";
    public static final String USERNAME = "user_name";
    public static final String ROLES = "roles";
    public static final String AVATAR = "avatar";
    public static final String EMAIL = "email";
    public static final String SIGN = "sign";

    /**
     * 封装user信息
     * 得到一个jwt载荷
     *
     * @return
     */
    public static Map<String, Object> User2Claims(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(ID, user.getId());
        claims.put(USERNAME, user.getUserName());
        claims.put(ROLES, user.getRoles());
        claims.put(AVATAR, user.getAvatar());
        claims.put(EMAIL, user.getEmail());
        claims.put(SIGN, user.getSign());
        return claims;
    }

    /**
     * 解析jwt载荷
     * 得到user对象
     *
     * @param claims
     * @return
     */
    public static User claims2ToUser(Claims claims) {
        User user = new User();
        user.setId((String) claims.get(ID));
        user.setUserName((String) claims.get(USERNAME));
        user.setRoles((String) claims.get(ROLES));
        user.setAvatar((String) claims.get(AVATAR));
        user.setEmail((String) claims.get(EMAIL));
        user.setSign((String) claims.get(SIGN));
        return user;
    }
}
