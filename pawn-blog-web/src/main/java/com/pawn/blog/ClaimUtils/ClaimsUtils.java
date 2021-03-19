package com.pawn.blog.ClaimUtils;

import com.pawn.blog.entity.User;
import io.jsonwebtoken.Claims;

import java.util.HashMap;
import java.util.Map;

/**
 * description: Claims工具类
 *
 * @param
 * @author:美茹冠玉
 * @Return
 * @date 2020/12/24 8:49
 */

public class ClaimsUtils {
    public static final String ID = "id";
    public static final String USERNAME = "userName";
    public static final String AVATAR = "avatar";
    public static final String ROLES = "roles";
    public static final String EMAIL = "email";
    public static final String SIGN = "sign";
    public static final String OCCUPATION = "occupation";

    public static Map<String, Object> user2Claims(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(ID, user.getId());
        claims.put(USERNAME, user.getUserName());
        claims.put(AVATAR, user.getAvatar());
        claims.put(ROLES, user.getRoles());
        claims.put(EMAIL, user.getEmail());
        claims.put(SIGN, user.getSign());
        claims.put(OCCUPATION, user.getOccupation());
        return claims;
    }

    /**
     * 获取
     *
     * @param claims
     * @return
     */
    public static User claims2User(Claims claims) {
        User user = new User();
        user.setId((String) claims.get(ID));
        user.setUserName((String) claims.get(USERNAME));
        user.setAvatar((String) claims.get(AVATAR));
        user.setRoles((String) claims.get(ROLES));
        user.setEmail((String) claims.get(EMAIL));
        user.setSign((String) claims.get(SIGN));
        user.setOccupation((String) claims.get(OCCUPATION));
        return user;
    }
}
