package com.pawn.blog.mapper;

import com.pawn.blog.entity.Settings;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author pawn
 * @since 2020-12-17
 */
public interface SettingsMapper extends BaseMapper<Settings> {
    @Select("select 'key' from tb_settings")
    Settings findOneByKey(String key);
}
