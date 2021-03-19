package com.pawn.blog.service;

import com.pawn.blog.entity.Looper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pawn.blog.response.Result;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author pawn
 * @since 2020-12-17
 */
public interface LooperService extends IService<Looper> {

    Result addLooper(Looper looper);

    Result deleteLooper(String looperId);

    Result updateLooper(String looperId, Looper looper);

    Result getLooper(String looperId);

    Result listLooper(int page, int size);
}
