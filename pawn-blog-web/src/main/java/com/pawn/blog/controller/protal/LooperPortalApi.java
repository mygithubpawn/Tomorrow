package com.pawn.blog.controller.protal;

import com.pawn.blog.response.Result;
import com.pawn.blog.service.impl.LooperServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/***
 * description: 门户轮播图
 * @author:美茹冠玉
 * @Return
 * @param
 * @date 2021/1/23 19:47
 */
@RestController
@RequestMapping("/portal/looper")
@Api(value = "轮播图管理", tags = "门户轮播图管理Api")
public class LooperPortalApi {
    @Autowired
    private LooperServiceImpl looperService;

    /**
     * 获取单个轮播图
     *
     * @param looperId
     * @return
     */
    @PutMapping("/get/{looperId}")
    @ApiOperation(value = "获取单个轮播图接口", notes = "")
    public Result getLooper(@PathVariable("looperId") String looperId) {
        return looperService.getLooper(looperId);
    }

    /**
     * 获取轮播图列表
     *
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "获取轮播图列表接口", notes = "")
    public Result listLooper(@RequestParam("page") int page, @RequestParam("size") int size) {
        return looperService.listLooper(page, size);
    }

}
