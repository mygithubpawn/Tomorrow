package com.pawn.blog.controller.admin;


import com.pawn.blog.response.Result;
import com.pawn.blog.service.ImagesService;
import com.pawn.blog.service.impl.ImagesServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 图片管理控制器
 * 有对象存储是，重构代码
 * </p>
 *
 * @author pawn
 * @since 2020-12-17
 */
@RestController
@RequestMapping("/images")
@Api(value = "图片上传管理", tags = "图片上传Api")
public class ImagesController {
    @Autowired
    ImagesServiceImpl imagesService;

    /**
     * 图片上传
     *
     * @param file
     * @param original //图片类型
     * @return
     */
//    @PreAuthorize("@permission.user()")
    @PostMapping("/{userId}/{original}")
    @ApiOperation(value = "图片上传接口",
            notes = "上传图片，保存到本地，后面通过oss重构")
    public Result uploadImage(@PathVariable("original") String original,
                              @PathVariable("userId") String userId,
                              @RequestParam("file") MultipartFile file) {
        return imagesService.uploadImage(userId,original, file);
    }

    /**
     * 图片删除
     *
     * @param imagesId
     * @return
     */
    @PreAuthorize("@permission.user()")
    @DeleteMapping("/delete/{imagesId}")
    @ApiOperation(value = "删除图片接口",
            notes = "通过图片id删除图==>逻辑删除）")
    public Result deleteImages(@PathVariable("imagesId") String imagesId) {
        return imagesService.deleteImages(imagesId);
    }
    /**
     * 获取所有上传图片的总数
     *
     * @return
     */
    @GetMapping("/image/Total")
    @ApiOperation(value = "获取所有上传图片总数接口",
            notes = "获取所有上传图片的总数")
    public Result imageTotalPar() {
        return imagesService.imageTotalPar();
    }
    /**
     * 查询图片
     *
     * @param imagesId
     * @return
     */
    @PreAuthorize("@permission.user()")
    @GetMapping("/get/{imagesId}")
    @ApiOperation(value = "查询图片接口",
            notes = "通过图片id查询图片")
    public Result getImages(@PathVariable("imagesId") String imagesId) {
        return imagesService.getImages(imagesId);
    }

    /**
     * 获取图片列表
     *
     * @param page
     * @param size
     * @return
     */
    @PreAuthorize("@permission.user()")
    @GetMapping("/list/{page}/{size}/{original}")
    @ApiOperation(value = "获取图片列表接口",
            notes = "获取当前用户的所有图片")
    public Result listImages(@PathVariable("original") String original,
                             @PathVariable("page") int page, @PathVariable("size") int size) {
        return imagesService.listImages(original, page, size);
    }

}

