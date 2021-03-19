package com.pawn.blog.service;

import com.pawn.blog.entity.Labels;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pawn.blog.response.Result;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author pawn
 * @since 2020-12-17
 */
public interface LabelsService extends IService<Labels> {

    Result LabelsTotal(String userId);

}
