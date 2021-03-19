package com.pawn.blog.controller.admin;


import com.pawn.blog.entity.Looper;
import com.pawn.blog.response.Result;
import com.pawn.blog.response.ResultCode;
import com.pawn.blog.service.impl.LooperServiceImpl;
import com.pawn.blog.utils.OssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 轮播图控制器
 * </p>
 *
 * @author pawn
 * @since 2020-12-17
 */
@RestController
@RequestMapping("/looper")
@Api(value = "轮播图管理", tags = "轮播图管理Api")
public class LooperController {
    @Autowired
    private LooperServiceImpl looperService;

    /**
     * 添加轮播图
     *
     * @param looper
     * @return
     */
    @PreAuthorize("@permission.user()")
    @PostMapping
    @ApiOperation(value = "添加轮播图接口", notes = "")
    public Result addLooper(@RequestBody Looper looper) {
        return looperService.addLooper(looper);
    }

    /**
     * 删除轮播图列表
     *
     * @param looperId
     * @return
     */
    @PreAuthorize("@permission.user()")
    @DeleteMapping("/delete/{looperId}")
    @ApiOperation(value = "删除轮播图接口", notes = "物理删除")
    public Result deleteLooper(@PathVariable("looperId") String looperId) {
        return looperService.deleteLooper(looperId);
    }

    /**
     * 更新轮播图
     *
     * @param looperId
     * @param looper
     * @return
     */
    @PreAuthorize("@permission.user()")
    @PutMapping("/update/{looperId}")
    @ApiOperation(value = "更新轮播图接口", notes = "")
    public Result updateLooper(@PathVariable("looperId") String looperId, @RequestBody Looper looper) {
        return looperService.updateLooper(looperId, looper);
    }

    /**
     * 获取单个轮播图
     *
     * @param looperId
     * @return
     */
    @PreAuthorize("@permission.user()")
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
    @PreAuthorize("@permission.user()")
    @GetMapping("/list")
    @ApiOperation(value = "获取轮播图列表接口", notes = "")
    public Result listLooper(@RequestParam("page") int page, @RequestParam("size") int size) {
        return looperService.listLooper(page, size);
    }

}

