package com.pawn.blog.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pawn.blog.dao.ArticleUserDao;
import com.pawn.blog.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pawn.blog.response.Result;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author pawn
 * @since 2020-12-17
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("select * from tb_user where state='0'")
    IPage<User> listUser(IPage<User> page);

    @Update("update tb_user set state=#{state} where id=#{userId}")
    int enableUser(String userId,String state);
}
