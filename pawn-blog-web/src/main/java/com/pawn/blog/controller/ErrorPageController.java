package com.pawn.blog.controller;

import com.pawn.blog.response.Result;
import com.pawn.blog.response.ResultCode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/***
 * description: 异常控制器
 * @author:美茹冠玉
 * @Return
 * @param
 * @date 2020/12/26 13:19
 */

@Controller
@RequestMapping("/ErrorPage")
public class ErrorPageController {
    @GetMapping("/404")
    public Result page404() {
        return Result.error().message(ResultCode.ERROR_404.getMessage());
    }

    @GetMapping("/403")
    public Result page403() {
        return Result.error().message(ResultCode.ERROR_403.getMessage());
    }

    @GetMapping("/500")
    public Result page500() {
        return Result.error().message(ResultCode.ERROR_500.getMessage());
    }

    @GetMapping("/504")
    public Result page504() {
        return Result.error().message(ResultCode.ERROR_504.getMessage());
    }

}
