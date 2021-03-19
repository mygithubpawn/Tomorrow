package com.pawn.blog.handler;

import com.pawn.blog.response.Result;
import com.pawn.blog.response.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/***
 * description: 全局异常处理
 * @author:美茹冠玉
 * @Return
 * @param
 * @date 2020/12/6 10:49
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 全局异常处理，没有指定异常类型
     *
     * @param e
     * @return
     */
//    @ExceptionHandler(Exception.class)
//    @ResponseBody
//    public Result error(Exception e) {
//        log.error(e.getMessage());
//        return Result.error();
//    }

    /**
     * 异常：ArithmeticException
     *
     * @param e
     * @return
     */
    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public Result error(ArithmeticException e) {
        log.error(e.getMessage());
        return Result.error().code(ResultCode.ARITHMETIC_EXCEPTION.getCode())
                .message(ResultCode.ARITHMETIC_EXCEPTION.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public Result error(BusinessException e) {
        log.error(e.getErrMsg());
        return Result.error().code(e.getCode())
                .message(e.getErrMsg());
    }

}
