package com.pawn.blog.controller.admin;

import com.pawn.blog.entity.PhotoWall;
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
@RequestMapping("/photo")
@Api(value = "照片墙管理", tags = "相册管理Api")
public class PhotoWallController {
    @Autowired
    private PhotoWallServiceImpl photoWallService;

    /**
     * 添加照片
     *
     * @param photoWall
     * @return
     */
    @PreAuthorize("@permission.user()")
    @PostMapping
    @ApiOperation(value = "添加照片接口", notes = "")
    public Result addPhoto(@RequestBody PhotoWall photoWall) {
        return photoWallService.addPhoto(photoWall);
    }

    /**
     * 获取用户相片总数接口
     *
     * @param userId
     * @return
     */
    @PreAuthorize("@permission.user()")
    @GetMapping("/get/total/{userId}")
    @ApiOperation(value = "获取用户相片总数接口",
            notes = "获取用户相片总数")
    public Result photoTotal(@PathVariable("userId") String userId) {
        return photoWallService.photoTotal(userId);
    }
    /**
     * 删除照片
     *
     * @param photoId
     * @return
     */
    @PreAuthorize("@permission.user()")
    @DeleteMapping("/delete/{photoId}")
    @ApiOperation(value = "删除照片接口", notes = "物理删除")
    public Result deletePhoto(@PathVariable("photoId") String photoId) {
        return photoWallService.deletePhoto(photoId);
    }

    /**
     * 照片更新
     *
     * @param photoId
     * @param PhotoWall
     * @return
     */
    @PreAuthorize("@permission.user()")
    @PutMapping("/update/{photoId}")
    @ApiOperation(value = "更新照片接口", notes = "")
    public Result updatePhoto(@PathVariable("photoId") String photoId, @RequestBody PhotoWall PhotoWall) {
        return photoWallService.updateLooper(photoId, PhotoWall);
    }

    /**
     * 获取单张照片接口
     *
     * @param photoId
     * @return
     */
    @PreAuthorize("@permission.user()")
    @PutMapping("/get/{photoId}")
    @ApiOperation(value = "获取单张照片接口", notes = "")
    public Result getPhoto(@PathVariable("photoId") String photoId) {
        return photoWallService.getLooper(photoId);
    }

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
    public Result PhotoList(@PathVariable("page") int page, @PathVariable("size") int size) {
        return photoWallService.listLooper(page, size);
    }

}
