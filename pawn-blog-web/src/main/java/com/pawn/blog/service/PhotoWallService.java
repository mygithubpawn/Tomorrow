package com.pawn.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pawn.blog.entity.PhotoWall;
import com.pawn.blog.response.Result;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author pawn
 * @since 2020-12-17
 */
public interface PhotoWallService extends IService<PhotoWall> {
    Result addPhoto(PhotoWall photoWall);

    Result deletePhoto(String photoId);

    Result updateLooper(String photoId, PhotoWall photoWall);

    Result getLooper(String photoId);

    Result listLooper(int page, int size);

    Result photoTotal(String userId);
}
