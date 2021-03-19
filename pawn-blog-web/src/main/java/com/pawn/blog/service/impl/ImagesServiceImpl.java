package com.pawn.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pawn.blog.entity.Article;
import com.pawn.blog.entity.Images;
import com.pawn.blog.entity.User;
import com.pawn.blog.mapper.ImagesMapper;
import com.pawn.blog.response.Result;
import com.pawn.blog.response.ResultCode;
import com.pawn.blog.service.ImagesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pawn.blog.utils.IdWorker;
import com.pawn.blog.utils.OssUtil;
import com.pawn.blog.utils.TestUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 图片操作实现类
 *
 * </p>
 *
 * @author pawn
 * @since 2020-12-17
 */
@Slf4j
@Service
@Transactional
public class ImagesServiceImpl extends ServiceImpl<ImagesMapper, Images> implements ImagesService {

    @Autowired
    OssUtil ossUtil;
    @Autowired
    UserServiceImpl userService;
    @Resource
    ImagesMapper imagesMapper;
    @Autowired
    IdWorker idWorker;
    @Autowired
    TestUtils testUtils;

    /**
     * 描述
     * 图片上传
     *
     * @param file
     * @return
     */
    @Override
    public Result uploadImage(String userId, String original, MultipartFile file) {
        //数据校验
        if (file == null)
            return Result.error().code(ResultCode.UPLOAD_PICTURE_NULL.getCode()).message(ResultCode.UPLOAD_PICTURE_NULL.getMessage());
        //上传图片
        String uploadImage = ossUtil.upload(file, OssUtil.FileDirType.ARTICLE_BOK);
        //填充数据
        Images images = new Images()
                .setId(idWorker.nextId() + "")
                .setUrl(uploadImage)
                .setState("1")
                .setUserId(userId)
                .setOriginal(original)
                .setCreateTime(new Date())
                .setUpdateTime(new Date());
//        保存图片
        int insert = imagesMapper.insert(images);
        return insert > 0 ? Result.ok().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage()).data("images", images)
                : Result.error().code(ResultCode.UPLOAD_PICTURE_EXCEED.getCode())
                .message(ResultCode.UPLOAD_PICTURE_EXCEED.getMessage());
    }

    /**
     * 图片获取接口
     *
     * @param imagesId
     * @return
     */
    @Override
    public Result getImages(String imagesId) {
        if (StringUtils.isEmpty(imagesId))
            return Result.error().code(ResultCode.UPLOAD_PICTURE_ID_NULL.getCode()).message(ResultCode.UPLOAD_PICTURE_ID_NULL.getMessage());
        //查询数据
        Images images = imagesMapper.selectOne(new QueryWrapper<Images>().eq("id", imagesId));
        if (images == null)
            return Result.error().code(ResultCode.UPLOAD_PICTURE_IMAGE_NULL.getCode()).message(ResultCode.UPLOAD_PICTURE_IMAGE_NULL.getMessage());

        return Result.ok().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage())
                .data("images", images);
    }

    /**
     * 获取图片列表
     *
     * @param page
     * @param size
     * @return
     */
    @Override
    public Result listImages(String original, int page, int size) {
        User user = userService.checkUser();
        //分页约束
        int checkPage = testUtils.checkPage(page);
        int checkSize = testUtils.checkSize(size);
        List<Page> imagesList = new ArrayList<>();
        //获取用户列表（分页查询）
        Page page1 = new Page(checkPage - 1, checkSize);
        Page Image_List = imagesMapper.selectPage(page1, new QueryWrapper<Images>()
                .eq("user_id", user.getId())
                .eq("original", original));
        imagesList.add(Image_List);
        return Result.ok().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage())
                .data("imagesList", imagesList);
    }

    /**
     * 图片删除
     *
     * @param imagesId
     * @return
     */
    @Override
    public Result deleteImages(String imagesId) {
        User user = userService.checkUser();
        if (imagesId == null)
            return Result.error().code(ResultCode.UPLOAD_PICTURE_ID_NULL.getCode()).message(ResultCode.UPLOAD_PICTURE_ID_NULL.getMessage());
        Images images = imagesMapper.selectById(imagesId);
        if (!images.getUserId().equals(user.getId()))
            return Result.error().code(ResultCode.NO_PERMISSION.getCode()).message(ResultCode.NO_PERMISSION.getMessage());
        if (images == null)
            return Result.error().code(ResultCode.UPLOAD_PICTURE_IMAGE_NULL.getCode()).message(ResultCode.UPLOAD_PICTURE_IMAGE_NULL.getMessage());

        //删除图片
        int imgDeleteId = imagesMapper.deleteById(imagesId);
        return imgDeleteId > 0 ? Result.ok().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage())
                : Result.ok().code(ResultCode.UPLOAD_PICTURE_DELETE_EXCEED.getCode())
                .message(ResultCode.UPLOAD_PICTURE_DELETE_EXCEED.getMessage());
    }

    /**
     * 获取数据库所有上传图片
     *
     * @return
     */
    @Override
    public Result imageTotalPar() {
        Integer imageCount = imagesMapper.selectCount(new QueryWrapper<Images>().select("id").eq("state", "1"));
        return Result.ok().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage())
                .data("imageCount", imageCount);
    }
}
