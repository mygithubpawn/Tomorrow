package com.pawn.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pawn.blog.entity.Images;
import com.pawn.blog.entity.PhotoWall;
import com.pawn.blog.entity.User;
import com.pawn.blog.mapper.PhotoWallMapper;
import com.pawn.blog.response.Result;
import com.pawn.blog.response.ResultCode;
import com.pawn.blog.service.PhotoWallService;
import com.pawn.blog.utils.IdWorker;
import com.pawn.blog.utils.OssUtil;
import com.pawn.blog.utils.TestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * <p>
 * 服务实现类
 * </p>2
 *
 * @author pawn
 * @since 2020-12-17
 */
@Service
@Transactional
public class PhotoWallServiceImpl extends ServiceImpl<PhotoWallMapper, PhotoWall> implements PhotoWallService {

    @Autowired
    private OssUtil ossUtil;
    @Autowired
    private UserServiceImpl userService;
    @Resource
    private PhotoWallMapper photoWallMapper;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private TestUtils testUtils;

    /**
     * 添加照片
     *
     * @param photoWall
     * @return
     */
    @Override
    public Result addPhoto(PhotoWall photoWall) {
        User user = userService.checkUser();
        if (StringUtils.isEmpty(photoWall.getIntroduction()))
            return Result.error().code(ResultCode.PHOTO_TITLE_NULL.getCode())
                    .message(ResultCode.PHOTO_TITLE_NULL.getMessage());
        if (StringUtils.isEmpty(photoWall.getImageUrl()))
            return Result.error().code(ResultCode.PHOTO_IMAGE_URL_NULL.getCode())
                    .message(ResultCode.PHOTO_IMAGE_URL_NULL.getMessage());
        //补充数据
        photoWall.setId(idWorker.nextId() + "")
                .setUserId(user.getId())
                .setState("1")
                .setCreateTime(new Date())
                .setUpdateTime(new Date());
        //添加进数据库
        int photoInsert = photoWallMapper.insert(photoWall);
        //返回结果
        return photoInsert > 0 ? Result.ok().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage())
                : Result.ok().code(ResultCode.PHOTO_ADD_EXCEED.getCode())
                .message(ResultCode.PHOTO_ADD_EXCEED.getMessage());
    }

    /**
     * 删除照片
     *
     * @param photoId
     * @return
     */
    @Override
    public Result deletePhoto(String photoId) {
        User user = userService.checkUser();
        if (StringUtils.isEmpty(photoId))
            return Result.error().code(ResultCode.PHOTO_IMAGE_URL_NULL.getCode())
                    .message(ResultCode.PHOTO_IMAGE_URL_NULL.getMessage());
        PhotoWall photoWall = photoWallMapper.selectById(photoId);
        if (photoWall == null)
            return Result.error().code(ResultCode.PHOTO_IMAGE_EXCEED.getCode())
                    .message(ResultCode.PHOTO_IMAGE_EXCEED.getMessage());
        if (!photoWall.getUserId().equals(user.getId()))
            return Result.error().code(ResultCode.NO_PERMISSION.getCode())
                    .message(ResultCode.NO_PERMISSION.getMessage());
        //删除数据
        int deleteById = photoWallMapper.deleteById(photoId);

        return deleteById > 0 ? Result.ok().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage())
                : Result.ok().code(ResultCode.PHOTO_DELETE_EXCEED.getCode())
                .message(ResultCode.PHOTO_DELETE_EXCEED.getMessage());
    }

    /**
     * 更新照片
     *
     * @param photoId
     * @param photoWall
     * @return
     */
    @Override
    public Result updateLooper(String photoId, PhotoWall photoWall) {
        User user = userService.checkUser();
        String userId = user.getId();
        if (StringUtils.isEmpty(photoId))
            return Result.error().code(ResultCode.PHOTO_IMAGE_URL_NULL.getCode())
                    .message(ResultCode.PHOTO_IMAGE_URL_NULL.getMessage());
        PhotoWall photoWaFrom = photoWallMapper.selectById(photoId);
        if (photoWaFrom == null)
            return Result.error().code(ResultCode.PHOTO_IMAGE_EXCEED.getCode())
                    .message(ResultCode.PHOTO_IMAGE_EXCEED.getMessage());
        if (!photoWaFrom.getUserId().equals(userId))
            return Result.error().code(ResultCode.NO_PERMISSION.getCode())
                    .message(ResultCode.NO_PERMISSION.getMessage());

        String introduction = photoWall.getIntroduction();
        if (!StringUtils.isEmpty(introduction)) {
            photoWaFrom.setIntroduction(introduction);
        }
        String imageUrl = photoWall.getImageUrl();
        if (!StringUtils.isEmpty(imageUrl)) {
            photoWaFrom.setImageUrl(imageUrl);
        }
        photoWaFrom.setUpdateTime(new Date());
        //更新数据
        photoWallMapper.update(photoWaFrom, new UpdateWrapper<PhotoWall>()
                .eq("id", photoId).eq("user_id", userId));

        return Result.ok().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage());
    }

    /**
     * 获取单张照片
     *
     * @param photoId
     * @return
     */
    @Override
    public Result getLooper(String photoId) {
        User user = userService.checkUser();
        if (StringUtils.isEmpty(photoId))
            return Result.error().code(ResultCode.PHOTO_IMAGE_URL_NULL.getCode())
                    .message(ResultCode.PHOTO_IMAGE_URL_NULL.getMessage());
        PhotoWall photoFrom = photoWallMapper.selectById(photoId);
        if (photoFrom == null)
            return Result.error().code(ResultCode.PHOTO_IMAGE_EXCEED.getCode())
                    .message(ResultCode.PHOTO_IMAGE_EXCEED.getMessage());
        if (!photoFrom.getUserId().equals(user.getId()))
            return Result.error().code(ResultCode.NO_PERMISSION.getCode())
                    .message(ResultCode.NO_PERMISSION.getMessage());

        return Result.ok().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage())
                .data("photoFrom", photoFrom);
    }

    /**
     * 获取照片列表
     *
     * @param page
     * @param size
     * @return
     */
    @Override
    public Result listLooper(int page, int size) {
        User user = userService.checkUser();
        //分页约束
        int checkPage = testUtils.checkPage(page);
        int checkSize = testUtils.checkSize(size);

        //获取照片列表（分页查询）
        Page page1 = new Page(checkPage - 1, checkSize);
        Page photoList = photoWallMapper.selectPage(page1, new QueryWrapper<PhotoWall>()
                .eq("user_id", user.getId()).orderByDesc("create_time"));

        return Result.ok().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage())
                .data("photoList", photoList);
    }

    /**
     * 获取用户相片总数接口
     *
     * @param userId
     * @return
     */
    @Override
    public Result photoTotal(String userId) {
        Integer imageCount = photoWallMapper.selectCount(new QueryWrapper<PhotoWall>().select("id")
                .eq("user_id", userId).eq("state", "1"));
        return Result.ok().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage())
                .data("imageCount", imageCount);
    }
}
