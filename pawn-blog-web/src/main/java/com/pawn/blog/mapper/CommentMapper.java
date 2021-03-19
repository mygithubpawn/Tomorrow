package com.pawn.blog.mapper;

import com.pawn.blog.entity.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author pawn
 * @since 2020-12-17
 */
public interface CommentMapper extends BaseMapper<Comment> {

    @Delete("delete from tb_comment where release_id=#{articleId}")
    int deleteCommRes(String articleId);
}
