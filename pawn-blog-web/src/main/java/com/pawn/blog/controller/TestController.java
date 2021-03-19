package com.pawn.blog.controller;

import com.pawn.blog.entity.Comment;
import com.pawn.blog.entity.User;
import com.pawn.blog.mapper.CommentMapper;
import com.pawn.blog.response.Result;
import com.pawn.blog.response.ResultCode;
import com.pawn.blog.service.UserService;
import com.pawn.blog.utils.*;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/***
 * description:
 * @author:美茹冠玉
 * @Return
 * @param
 * @date 2020/12/23 11:09
 */

@Slf4j
@Controller
@RequestMapping("/test")
public class TestController {
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    JWTUtil jwtUtil;
    @Autowired
    IdWorker idWorker;
    @Resource
    CommentMapper commentMapper;
    @Autowired
    CookieUtils cookieUtils;
    @Autowired
    UserService userService;

    /**
     * 评论
     *
     * @param comment
     * @param request
     * @return
     */
    @PostMapping("/comment")
    public Result testComment(@RequestBody Comment comment, HttpServletRequest request) {

        String content = comment.getContent();
        log.info("content == > " + content);
        //对评论进行身份校验
        String toCookie = cookieUtils.getCookie(request, Constants.User.COOKIE_TOKEN_KEY);
        if (StringUtils.isEmpty(toCookie))
            return Result.error().message(ResultCode.USER_NOT_LOGIN.getMessage());
        //封装相同操作实现
        User user = userService.checkUser();
        if (user == null)
            return Result.error().message(ResultCode.USER_NOT_LOGIN.getMessage());
        comment.setId(idWorker.nextId() + "");
        log.info("comment_id == >", idWorker.nextId() + "");
        comment.setUserId(user.getId());
        log.info("comment_User_name == >", user.getUserName());
        comment.setUserName(user.getUserName());
        comment.setUserAvatar(user.getAvatar());
        comment.setCreateTime(new Date());
        comment.setUpdateTime(new Date());
        commentMapper.insert(comment);
        return Result.ok().message(ResultCode.SUCCESS.getMessage());
    }
}
