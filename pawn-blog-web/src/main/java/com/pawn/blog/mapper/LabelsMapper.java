package com.pawn.blog.mapper;

import com.pawn.blog.entity.Labels;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author pawn
 * @since 2020-12-17
 */
public interface LabelsMapper extends BaseMapper<Labels> {
    @Update("update `tb_labels`  set count =count+1 where name=#{tables}")
    int updateName(String tables);

}
