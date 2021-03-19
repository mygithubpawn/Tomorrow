package com.pawn.blog.service;

import com.pawn.blog.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pawn.blog.response.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;

/**
 * <p>
 * user服务类
 * </p>
 *
 * @author pawn
 * @since 2020-12-17
 */
public interface UserService extends IService<User> {


    /**
     * 邮件发送
     *
     * @param emailAddress
     * @return
     */
    Result senEmail(String type, String emailAddress, HttpServletRequest request);

    /**
     * 用户注册
     *
     * @param user
     * @param request
     * @return
     */
    Result initManagerAccount(User user, HttpServletRequest request);

    /**
     * 验证码生成
     *
     * @param response
     * @param captchakey
     * @throws IOException
     * @throws FontFormatException
     */
    void createCaptcha(HttpServletResponse response, String captchakey) throws IOException, FontFormatException;

    /**
     * 用户注册
     *
     * @param user
     * @param emailCode
     * @param captchaCode
     * @param captchakey
     * @param request
     * @return
     */
    Result register(User user, String emailCode, String captchaCode, String captchakey, HttpServletRequest request);

    /**
     * 用户登陆
     *
     * @param captcha
     * @param captchaKey
     * @param user
     * @return
     */
    Result doLogin(String captcha, String captchaKey, User user, HttpServletResponse response);


    /**
     * 判断用户状态
     *
     * @return
     */
    User checkUser();

    /**
     * 判断是否为当前用户
     *
     * @param usesById
     * @return
     */
    Boolean calibrationUser(String usesById);

    /**
     * 获取用户基本信息
     *
     * @param usesId
     * @return
     */
    Result getUserInfo(String usesId);

    /**
     * 检查邮箱
     *
     * @param email
     * @return
     */
    Result checkEmail(String email);

    /**
     * 用户名检查
     *
     * @param userName
     * @return
     */
    Result checkUserName(String userName);

    /**
     * 修改用户信息
     *
     * @param user
     * @return
     */
    Result updateUserInfo(String userId, User user);

    /**
     * 删除用户
     *
     * @param userId
     * @return
     */
    Result deleteUserById(String userId);

    /**
     * 获取用户列表
     *
     * @param page
     * @param size
     * @return
     */
    Result listUsers(int page, int size, String userName, String email);

    /**
     * 密码修改
     *
     * @param verifyCode
     * @param user
     * @return
     */
    Result updateUserPassword(String verifyCode, User user);

    /**
     * 更换邮箱
     *
     * @param email
     * @param verifyCode
     * @return
     */
    Result updateUserEmail(String email, String verifyCode);

    /**
     * 退出登陆
     *
     * @return
     */
    Result doLogout();

    Result parseToken();

    Result resetPassword(String userId, String password);

    Result adMinListUsers(int page, int size);

    Result enableUser(String userId, String userState);

    Result userTotal();

    Result feedback(String userName,String userEmail, String content);

    Result userOperating(String userName, String userEmail, String content);

    Result leaveMessage(String userName, String userEmail, String content);
}
