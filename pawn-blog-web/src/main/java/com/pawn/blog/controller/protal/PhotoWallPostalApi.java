package com.pawn.blog.controller.protal;

import com.pawn.blog.response.Result;
import com.pawn.blog.service.impl.PhotoWallServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 照片控制器
 * </p>
 *
 * @author pawn
 * @since 2020-12-17
 */

@RestController
@RequestMapping("/postal/photo")
@Api(value = "照片墙管理", tags = "门户照片墙管理Api")
public class PhotoWallPostalApi {
    @Autowired
    private PhotoWallServiceImpl photoWallService;

    /**
     * 获取照片列表
     *
     * @param page
     * @param size
     * @return
     */
    @PreAuthorize("@permission.user()")
    @GetMapping("/list/{page}/{size}")
    @ApiOperation(value = "获取照片列表接口", notes = "")
    public Result postalPhotoList(@PathVariable("page") int page, @PathVariable("size") int size) {
        return photoWallService.listLooper(page, size);
    }
}
