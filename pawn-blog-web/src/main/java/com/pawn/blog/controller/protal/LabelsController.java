package com.pawn.blog.controller.protal;


import com.pawn.blog.response.Result;
import com.pawn.blog.service.impl.LabelsServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author pawn
 * @since 2020-12-17
 */
@RestController
@RequestMapping("/labels")
@Api(value = "标签管理", tags = "标签管理API")
public class LabelsController {
    @Resource
    LabelsServiceImpl articleTotal;

    /**
     * 获取用户标签总数
     *
     * @param userId
     * @return
     */
    @PreAuthorize("@permission.user()")
    @GetMapping("/get/labels/{userId}")
    @ApiOperation(value = "获取用户标签总数接口",
            notes = "获取用户标签总数")
    public Result LabelsTotal(@PathVariable("userId") String userId) {
        return articleTotal.LabelsTotal(userId);
    }


}

