package com.pawn.blog.service.impl;

import com.pawn.blog.entity.DailyViewCount;
import com.pawn.blog.mapper.DailyViewCountMapper;
import com.pawn.blog.service.DailyViewCountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author pawn
 * @since 2020-12-17
 */
@Service
public class DailyViewCountServiceImpl extends ServiceImpl<DailyViewCountMapper, DailyViewCount> implements DailyViewCountService {

}
