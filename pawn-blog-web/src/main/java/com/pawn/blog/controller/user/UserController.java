package com.pawn.blog.controller.user;


import com.pawn.blog.controller.admin.EmailSendController;
import com.pawn.blog.entity.User;
import com.pawn.blog.response.Result;
import com.pawn.blog.service.impl.UserServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;

/**
 * <p>
 * 用户控制层
 * </p>
 *
 * @author pawn
 * @since 2020-12-17
 */
@Slf4j
@RestController
@RequestMapping("/user")
@Api(value = "用户管理", tags = "用户管理Api")
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    /**
     * 初始化管理员
     *
     * @param user    用户实体类
     * @param request
     * @return
     */
    @PostMapping("/admin")
    @ApiOperation(value = "初始化管理员",
            notes = "添加管理员数据并修改状态")
    public Result initManagerAccount(@RequestBody User user, HttpServletRequest request) {
        return userService.initManagerAccount(user, request);
    }


    /**
     * 注册
     *
     * @param user        用户实体类
     * @param emailCode   邮箱验证码
     * @param captchaCode 图灵验证码
     * @param captchakey  图灵验证码的key
     * @param request
     * @return
     */
    @PostMapping("/join/to")
    @ApiOperation(value = "注册接口",
            notes = "判断注册时填写信息的正确性")
    public Result register(@RequestBody User user,
                           @RequestParam("emailCode") String emailCode,
                           @RequestParam("captchaCode") String captchaCode,
                           @RequestParam("captchaKey") String captchakey,
                           HttpServletRequest request) {
        return userService.register(user, emailCode, captchaCode, captchakey, request);
    }

    /**
     * 登陆
     * 需要提交的数据
     * 1，用户账号，或邮箱-->都做过唯一处理
     * 2，密码f
     * 3，图灵验证码
     * 4，图灵验证码的key
     *
     * @param captcha    图灵验证码
     * @param captchaKey 图灵验证码的key
     * @param user       用户类，封装着密码和账号
     * @param response
     * @return
     */
    @PostMapping("/login/{captcha}/{captcha_key}")
    @ApiOperation(value = "登陆接口",
            notes = "token默认有效三小时")
    public Result login(@PathVariable("captcha") String captcha,
                        @PathVariable("captcha_key") String captchaKey,
                        @RequestBody User user,
                        HttpServletResponse response) {
        return userService.doLogin(captcha, captchaKey, user, response);
    }

    /**
     * 生成图灵验证码
     * 有效时间10分钟
     *
     * @return
     */
    @GetMapping("/captcha")
    @ApiOperation(value = "人类验证码接口",
            notes = "生成图灵验证码——>有效时间10分钟")
    public void getCaptcha(HttpServletResponse response, @RequestParam("captcha_key") String captchakey) {
        log.info("capt ==> chakey ==> " + captchakey);
        try {
            userService.createCaptcha(response, captchakey);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FontFormatException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送邮件
     * 业务场景：
     * 1，注册(register)
     * 2，找回密码(forget)
     * 3，更换邮箱(update)
     *
     * @param request
     * @param type         用户当初操作状态
     * @param emailAddress 邮箱
     * @return
     */
    @GetMapping("/email")
    @ApiOperation(value = "邮箱验证码发送接口",
            notes = "发送邮箱验证码——>有效时间10分钟")
    public Result sendVerifyCode(HttpServletRequest request,
                                 @RequestParam("type") String type,
                                 @RequestParam("email") String emailAddress) {
        log.info("emailAddress ==> " + emailAddress);
        return userService.senEmail(type, emailAddress, request);
    }

    /**
     * 1，注册(register)
     * 2，找回密码(forget)
     * 3，更换邮箱(update)
     * <p>
     * 修改密码
     * <p>
     * 找回密码
     * 步骤：
     * 1，用户填写邮箱
     * 2，获取验证码，type=forget
     * 3，填写验证码
     * 4，填写新的密码
     * 5，提交数据（邮箱，新密码 ）
     *
     * @param user
     * @param verifyCode
     * @return
     */
    @PutMapping("/password/{verifyCode}")
    @ApiOperation(value = "密码修改接口",
            notes = "修改当前 用户密码，包括找回密码")
    public Result updatePassword(@PathVariable("verifyCode") String verifyCode,
                                 @RequestBody User user) {
        return userService.updateUserPassword(verifyCode, user);
    }

    /**
     * 用户邮箱修改
     * <p>
     * 步骤：
     * 1，必须扽登陆
     * 2，输入新的邮箱地址
     * 3，获取验证码
     * 4，输入验证码
     * 5，提交数据
     * <p>
     * 需提交的数据
     * 1，新的邮箱地址
     * 2，验证密
     * 3，其他的可以从token里拿
     *
     * @param email
     * @param verifyCode
     * @return
     */
    @PreAuthorize("@permission.user()")
    @PutMapping("/update/email")
    @ApiOperation(value = "邮箱更换接口",
            notes = "更改用户邮箱")
    public Result updateEmail(@RequestParam("email") String email,
                              @RequestParam("verifyCode") String verifyCode) {
        return userService.updateUserEmail(email, verifyCode);
    }

    /**
     * 获取作者信息
     *
     * @param usesId
     * @return
     */
    @PreAuthorize("@permission.user()")
    @GetMapping("/select/userId")
    @ApiOperation(value = "作者信息接口",
            notes = "获取作者的基本信息，不包概括隐秘信息")
    public Result getUserInfo(@RequestParam("userId") String usesId) {

        return userService.getUserInfo(usesId);
    }

    /**
     * 修改用户信息
     * 1，用户名（唯一的）
     * 2，密码（单独修改）
     * 3，头像
     * 4，邮箱
     * 5，Email（唯一的，单独修改）
     *
     * @param userId
     * @param user
     * @return
     */
    @PreAuthorize("@permission.user()")
    @PutMapping("/update/{userId}")
    @ApiOperation(value = "修改用户信息接口",
            notes = "修改用户信息，只包括（用户名，头像，邮箱）")
    public Result updateUserInfo(@PathVariable("userId") String userId,
                                 @RequestBody User user) {

        return userService.updateUserInfo(userId, user);
    }

    /**
     * 获取用户列表
     * 需判断权限
     *
     * @param page
     * @param size
     * @return
     * @PreAuthorize("@permission.admin()") 自定义权限（管理员权限）
     */
    @GetMapping("/list")
    @PreAuthorize("@permission.admin()")
    @ApiOperation(value = "用户列表接口",
            notes = "获取用户列表，只能管理员才能操作")
    public Result listUser(@RequestParam("page") int page,
                           @RequestParam("size") int size,
                           @RequestParam(value = "userName", required = false) String userName,
                           @RequestParam(value = "email", required = false) String email) {
        return userService.listUsers(page, size, userName, email);
    }

    /**
     * 用户列表接口(已停用用户)
     *
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/admin/list")
    @PreAuthorize("@permission.admin()")
    @ApiOperation(value = "用户列表接口(已停用用户)",
            notes = "获取用户列表，只能管理员才能操作")
    public Result adminListUser(@RequestParam("page") int page,
                                @RequestParam("size") int size) {
        return userService.adMinListUsers(page, size);
    }

    /**
     * 取消用户禁用接口
     *
     * @param userId
     * @param userState
     * @return
     */
    @PreAuthorize("@permission.admin()")
    @PutMapping("/enable")
    @ApiOperation(value = "解锁用户禁用接口",
            notes = "取消用户禁用接口，修改用户状态")
    public Result enableUser(@RequestParam("userId") String userId, @RequestParam String userState) {
        return userService.enableUser(userId, userState);
    }

    /**
     * 获取用户总数
     *
     * @return
     */
    @PreAuthorize("@permission.admin()")
    @GetMapping("/user/total")
    @ApiOperation(value = "获取用户总数接口",
            notes = "获取用户总数")
    public Result userTotal() {
        return userService.userTotal();
    }

    /**
     * 删除用户
     * 并不是真的删除，而是修改状态
     * 1、需要管理员权限
     *
     * @param userId
     * @return
     * @PreAuthorize("@permission.admin()") 自定义权限（管理员权限）
     */
    @DeleteMapping("/delete/{userId}")
    @PreAuthorize("@permission.admin()")
    @ApiOperation(value = "用户删除接口",
            notes = "删除用户，只能管理员才能操作，并不是真的删除，而是修改状态")
    public Result deleteUser(@PathVariable("userId") String userId) {
        return userService.deleteUserById(userId);

    }

    /**
     * 退出登陆
     * <p>
     * 1，拿到token_key
     * 2,删除redis里对应的token
     * 3，删除mysql里的refreshToken
     * 4,删除token里的token_key
     *
     * @return
     */
    @GetMapping("/logout")
    @ApiOperation(value = "退出登陆接口",
            notes = "删除redis里对应的token，删除mysql里的refreshToken，删除token里的token_key")
    public Result logout() {
        return userService.doLogout();
    }

    /**
     * 检查邮箱是否已经注册
     *
     * @param email 邮箱地址
     * @return
     */
    @GetMapping("/check/email")
    @ApiOperation(value = "邮箱合法性检验接口",
            notes = "检查用户邮箱的合法性，是否已经注册，用户修改邮箱时调用")
    public Result checkEmail(@RequestParam("email") String email) {

        return userService.checkEmail(email);
    }

    /**
     * 检查用户名是否合法（是否已经注册）
     *
     * @param userName 用户名
     * @return
     */
    @GetMapping("/check/name_name")
    @ApiOperation(value = "用户名合法性检验接口",
            notes = "检查用户合法性，是否已经注册，用户修改用户名时调用")
    public Result checkUserName(@RequestParam("userName") String userName) {

        return userService.checkUserName(userName);
    }

    /**
     * 通过Token获取 用信息
     *
     * @return
     */
    @GetMapping("/check_token")
    @ApiOperation(value = "用户信息接口-Token",
            notes = "通过token获取用户接口")
    public Result parseToken() {
        return userService.parseToken();
    }

    /**
     * 重置密码，需要管理员权限
     *
     * @param userId
     * @param password 新密码
     * @return
     */
    @PreAuthorize("@permission.admin()")
    @PutMapping("/reset_password/{userId}")
    @ApiOperation(value = "重置密码接口",
            notes = "管理员才有权操作")
    public Result resetPassword(@PathVariable("userId") String userId,
                                @RequestParam("password") String password) {
        return userService.resetPassword(userId, password);
    }
}

