package com.pawn.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pawn.blog.entity.RefreshToken;
import org.apache.ibatis.annotations.Delete;

public interface RefreshTokenMapper extends BaseMapper<RefreshToken> {

    @Delete("delete from tb_refresh_token where user_id=#{userId}")
    int deleteByUserId(String userId);
}

