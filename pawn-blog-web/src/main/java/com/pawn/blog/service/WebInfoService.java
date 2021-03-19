package com.pawn.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pawn.blog.entity.WebInfo;
import com.pawn.blog.response.Result;
import org.springframework.web.bind.annotation.RequestParam;

public interface WebInfoService extends IService<WebInfo> {

    Result getWebSizeTitle();

    Result updateWebSizeTitle(String title);

    Result putSeoInfo(WebInfo webInfo);

    Result checkWebInfoTitle(String value);
}
