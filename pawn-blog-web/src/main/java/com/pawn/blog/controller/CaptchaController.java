package com.pawn.blog.controller;

import com.pawn.blog.utils.Constants;
import com.pawn.blog.utils.RedisUtil;
//import com.wf.captcha.SpecCaptcha;
//import com.wf.captcha.base.Captcha;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/***
 * description: 图灵验证码
 * @author:美茹冠玉
 * @Return
 * @param
 * @date 2020/12/19 10:37
 */

@Controller
@Api(value = "图灵验证码", tags = "图灵验证码接口")
public class CaptchaController {
    @Autowired
    private RedisUtil redisUtil;

    @RequestMapping("/captcha")
    @ApiOperation(value = "初始化验证码", notes = "")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
    }

}
