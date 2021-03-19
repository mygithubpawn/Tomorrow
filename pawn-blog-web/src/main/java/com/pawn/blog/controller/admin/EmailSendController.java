package com.pawn.blog.controller.admin;

import com.pawn.blog.response.Result;
import com.pawn.blog.service.impl.ArticleServiceImpl;
import com.pawn.blog.service.impl.UserServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * description: 邮件发送控制层
 *
 * @param
 * @author:美茹冠玉
 * @Return
 * @date 2021/2/18 20:29
 */

@RestController
@RequestMapping("/email/send")
@Api(value = "邮件发送", tags = "邮件发送API")
public class EmailSendController {
    @Autowired
    ArticleServiceImpl articleService;
    @Autowired
    UserServiceImpl userService;

    /**
     * 邮箱修改邮件发送
     *
     * @param request
     * @param emailAddress
     * @param name
     * @return
     */
    @GetMapping("/ email/update")
    @ApiOperation(value = "邮箱修改邮件发送接口",
            notes = "邮箱修改邮件发送")
    public Result emailUpdate(HttpServletRequest request,
                              @RequestParam("email") String emailAddress,
                              @RequestParam("name") String name) {
        return articleService.emailUpdate(emailAddress, name, request);
    }

    /**
     * 用户反馈
     *
     * @param userName
     * @param content
     * @return
     */
    @GetMapping("/feedback")
    @ApiOperation(value = "用户反馈邮件发送接口",
            notes = "用户反馈邮件发送")
    public Result feedback(@RequestParam("userName") String userName,
                           @RequestParam("userEmail") String userEmail,
                           @RequestParam("content") String content) {
        return userService.feedback(userName, userEmail, content);
    }

    /**
     * 用户留言邮件发送
     *
     * @param userName
     * @param content
     * @return
     */
    @GetMapping("/leave/message")
    @ApiOperation(value = "用户留言邮件发送接口",
            notes = "用户留言邮件发送")
    public Result leaveMessage(@RequestParam("userName") String userName,
                               @RequestParam("userEmail") String userEmail,
                               @RequestParam("content") String content) {
        return userService.leaveMessage(userName, userEmail, content);
    }

    /**
     * 管理员邮件发送（前台定义发送内容）
     *
     * @param userName
     * @param content
     * @return
     */
    @GetMapping("/operating")
    @ApiOperation(value = "管理员邮件发送接口（前台定义发送内容）",
            notes = "管理员邮件发送（前台定义发送内容）")
    public Result userOperating(@RequestParam("userName") String userName,
                                @RequestParam("userEmail") String userEmail,
                                @RequestParam("content") String content) {
        return userService.userOperating(userName, userEmail, content);
    }

    /**
     * 密码修改邮箱提示
     *
     * @param request
     * @param emailAddress
     * @param name
     * @return
     */
    @GetMapping("/password/update")
    @ApiOperation(value = "密码修改邮箱提示接口",
            notes = "密码修改邮箱提示")
    public Result passwordUpdate(HttpServletRequest request,
                                 @RequestParam("email") String emailAddress,
                                 @RequestParam("name") String name) {
        return articleService.passwordUpdate(emailAddress, name, request);
    }

}
