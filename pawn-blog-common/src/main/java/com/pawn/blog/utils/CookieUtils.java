package com.pawn.blog.utils;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/***
 * description: Cookie工具类
 * @author:美茹冠玉
 * @Return
 * @param
 * @date 2020/12/23 18:21
 */
@Component
@Service
public class CookieUtils {
    public static final int default_age = 60 * 60 * 24 * 365;

    public static final String domain = "localhost";

    /**
     * 设置cookie值
     *
     * @param response
     * @param key
     * @param value
     */
    public void setupCookie(HttpServletResponse response, String key, String value) {
        setupCookie(response, key, value, default_age);

    }

    public void setupCookie(HttpServletResponse response, String key, String value, int age) {
        Cookie cookie = new Cookie(key, value);
        cookie.setPath("/");
        cookie.setDomain(domain);
        cookie.setMaxAge(age);
        response.addCookie(cookie);
    }

    /**
     * 删除cookie
     *
     * @param response
     * @param kay
     */
    public void deleteCookie(HttpServletResponse response, String kay) {
        setupCookie(response, kay, null, 0);
    }


    /**
     * 获取cookie
     *
     * @param request
     * @param key
     * @return
     */
    public String getCookie(HttpServletRequest request, String key) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (key.equals(cookie.getName()))
                return cookie.getValue();
        }
        return null;
    }
}
