package com.pawn.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pawn.blog.entity.Article;
import com.pawn.blog.entity.Labels;
import com.pawn.blog.mapper.LabelsMapper;
import com.pawn.blog.response.Result;
import com.pawn.blog.response.ResultCode;
import com.pawn.blog.service.LabelsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author pawn
 * @since 2020-12-17
 */
@Service
public class LabelsServiceImpl extends ServiceImpl<LabelsMapper, Labels> implements LabelsService {

    @Resource
    LabelsMapper labelsMapper;
    @Override
    public Result LabelsTotal(String userId) {
        Integer LabelsCount = labelsMapper.selectCount(new QueryWrapper<Labels>().select("id")
                .eq("user_id", userId));
        return Result.ok().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage())
                .data("LabelsCount", LabelsCount);
    }
}
