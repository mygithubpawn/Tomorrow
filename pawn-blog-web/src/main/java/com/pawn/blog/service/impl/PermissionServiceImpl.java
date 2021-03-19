package com.pawn.blog.service.impl;

import com.pawn.blog.entity.User;
import com.pawn.blog.service.UserService;
import com.pawn.blog.utils.Constants;
import com.pawn.blog.utils.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/***
 * description: 权限控制
 * @author:美茹冠玉
 * @Return
 * @param
 * @date 2020/12/26 11:08
 */

@Service("permission")
public class PermissionServiceImpl {

    @Autowired
    CookieUtils cookieUtils;
    @Autowired
    UserService userService;

    /**
     * 管理员权限
     *
     * @return
     */
    public boolean admin() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        //拿到request, response
        HttpServletRequest request = requestAttributes.getRequest();
        String cookie = cookieUtils.getCookie(request, Constants.User.COOKIE_TOKEN_KEY);

        //没有令牌的key，肯定没有登陆
        if (StringUtils.isEmpty(cookie))
            return false;

        User checkUser = userService.checkUser();
        if (checkUser == null)
            return false;

        if (Constants.User.ROLE_ADMIN.equals(checkUser.getRoles()))
            //为管理员
            return true;

        return false;
    }

    /**
     * 普通用户权限
     *
     * @return
     */
    public boolean user() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        //拿到request, response
        HttpServletRequest request = requestAttributes.getRequest();
        String cookie = cookieUtils.getCookie(request, Constants.User.COOKIE_TOKEN_KEY);

        //没有令牌的key，肯定没有登陆
        if (StringUtils.isEmpty(cookie))
            return false;

        User checkUser = userService.checkUser();
        if (checkUser == null)
            return false;

        if (Constants.User.ROLE_NORMAL.equals(checkUser.getRoles()) || Constants.User.ROLE_ADMIN.equals(checkUser.getRoles()))
            //用户已登陆
            return true;

        return false;
    }

}
