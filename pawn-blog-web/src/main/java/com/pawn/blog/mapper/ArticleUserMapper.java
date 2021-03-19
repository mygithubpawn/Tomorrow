package com.pawn.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pawn.blog.dao.ArticleUserDao;
import com.pawn.blog.entity.Article;
import org.apache.ibatis.annotations.Select;

public interface ArticleUserMapper  extends BaseMapper<ArticleUserDao> {
    @Select(" SELECT `tb_article`.*,tb_user.* FROM " +
            "tb_article LEFT JOIN tb_user ON tb_article.user_id=tb_user.id" +
            " WHERE tb_article.state !='2'")
    IPage<ArticleUserDao> getProductPage(IPage<ArticleUserDao> page);
}
