package com.pawn.blog.config;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

/***
 * description: 定义异常页面
 * @author:美茹冠玉
 * @Return
 * @param
 * @date 2020/12/26 11:49
 */

@Configuration
public class ErrorCodeConfig implements ErrorPageRegistrar {
    @Override
    public void registerErrorPages(ErrorPageRegistry registry) {
        registry.addErrorPages(new ErrorPage(HttpStatus.FORBIDDEN, "/403")
                , new ErrorPage(HttpStatus.NOT_FOUND, "/404")
                , new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500")
                , new ErrorPage(HttpStatus.GATEWAY_TIMEOUT, "/504")
                , new ErrorPage(HttpStatus.HTTP_VERSION_NOT_SUPPORTED, "/505"));
    }
}
