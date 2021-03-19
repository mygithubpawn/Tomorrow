package com.pawn.blog.controller.admin;

import com.pawn.blog.entity.User;
import com.pawn.blog.entity.WebInfo;
import com.pawn.blog.response.Result;
import com.pawn.blog.service.impl.WebInfoServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/***
 * description: 网站信息
 * @author:美茹冠玉
 * @Return
 * @param
 * @date 2020/12/18 13:44
 */

@RestController
@RequestMapping("/web_size_info")
@Api(value = "网站信息管理", tags = "网站信息管理Api")
public class WebSizeInfoApi {
    @Resource
    WebInfoServiceImpl webInfoService;


    /**
     * 获取网站标题title
     *
     * @return
     */
    @PreAuthorize("@permission.user()")
    @GetMapping("/get/title")
    @ApiOperation(value = "获取网站信息",
            notes = "")
    public Result getWebSizeTitle() {
        return webInfoService.getWebSizeTitle();
    }

    /**
     * 修改网站标题
     *
     * @param title
     * @return
     */
    @PreAuthorize("@permission.user()")
    @PutMapping("/update/title")
    @ApiOperation(value = "修改网站标题",
            notes = "网站标题不能为空，且不能重复")
    public Result updateWebSizeTitle(@RequestParam("title") String title) {
        return webInfoService.updateWebSizeTitle(title);
    }

    @PreAuthorize("@permission.user()")
    @GetMapping("/check/value")
    @ApiOperation(value = "网站标题校验接口",
            notes = "")
    public Result checkWebInfoTitle(@RequestParam("value") String value) {
        return webInfoService.checkWebInfoTitle(value);
    }


    /**
     * 修改seo信息
     *
     * @return
     */
    @PreAuthorize("@permission.user()")
    @PutMapping("/put/seo")
    @ApiOperation(value = "修改网站信息",
            notes = "支持修改网站名称")
    public Result putSeoInfo(@RequestBody WebInfo WebInfo) {
        return webInfoService.putSeoInfo(WebInfo);
    }


    /**
     * 获取view统计信息
     *
     * @return
     */
    @PreAuthorize("@permission.user()")
    @GetMapping("/view_count")
    public Result getWebSizeViewCount() {
        return null;
    }

}
