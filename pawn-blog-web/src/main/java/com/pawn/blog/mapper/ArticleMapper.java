package com.pawn.blog.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pawn.blog.dao.ArticleUserDao;
import com.pawn.blog.dao.detailArticleVo;
import com.pawn.blog.entity.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author pawn
 * @since 2020-12-17
 */
public interface ArticleMapper extends BaseMapper<Article> {

    @Select(" SELECT `tb_article`.id,`tb_article`.title,`tb_article`.content,`tb_article`.user_id,`tb_article`.category_id,`tb_article`.summary,`tb_article`.labels,`tb_article`.view_count,`tb_article`.create_time,`tb_article`.status,`tb_article`.position,`tb_article`.review," +
            "tb_user.id,tb_user.user_name,tb_user.avatar FROM " +
            "tb_article LEFT JOIN tb_user ON tb_article.user_id=tb_user.id" +
            " WHERE tb_article.id=#{articleId} and tb_article.status !='2'")
    detailArticleVo selectAerFrom(String articleId);

    @Delete("delete from tb_article where id=#{articleId}")
    int deleteArticle(String articleId);

    @Select(" SELECT `tb_article`.id,`tb_article`.title,`tb_article`.user_id,`tb_article`.category_id,`tb_article`.summary,`tb_article`.labels,`tb_article`.view_count,`tb_article`.create_time,`tb_article`.status,`tb_article`.position,`tb_article`.review," +
            "tb_user.id,tb_user.user_name,tb_user.avatar FROM " +
            "tb_article LEFT JOIN tb_user ON tb_article.user_id=tb_user.id" +
            " WHERE tb_article.status !='2' and review ='2' and position ='1' and tb_article.state ='1'")
    IPage<ArticleUserDao> getProductPage(IPage<ArticleUserDao> page);

    @Select(" SELECT `tb_article`.id,`tb_article`.title,`tb_article`.user_id,`tb_article`.category_id,`tb_article`.summary,`tb_article`.labels,`tb_article`.view_count,`tb_article`.create_time,`tb_article`.status,`tb_article`.position,`tb_article`.review," +
            "tb_user.id,tb_user.user_name,tb_user.avatar FROM " +
            "tb_article LEFT JOIN tb_user ON tb_article.user_id=tb_user.id" +
            " WHERE tb_article.status !='2' and review ='2' and position ='1' and tb_article.state ='1'" +
            "and category_id =#{category}")
    IPage<ArticleUserDao> capacityPage(IPage<ArticleUserDao> page, String category);

    @Select(" SELECT `tb_article`.id,`tb_article`.title,`tb_article`.user_id,`tb_article`.category_id,`tb_article`.summary,`tb_article`.labels,`tb_article`.view_count,`tb_article`.create_time,`tb_article`.status,`tb_article`.position,`tb_article`.review," +
            "tb_user.id,tb_user.user_name,tb_user.avatar FROM " +
            "tb_article LEFT JOIN tb_user ON tb_article.user_id=tb_user.id" +
            " WHERE tb_article.status !='2' and review ='2' and position ='1' and tb_article.state ='1'" +
            "and title  like '%${title}%' ")
    IPage<ArticleUserDao> searchTitlePage(IPage<ArticleUserDao> page, String title);

    @Select(" SELECT `tb_article`.id,`tb_article`.title,`tb_article`.user_id,`tb_article`.category_id,`tb_article`.summary,`tb_article`.labels,`tb_article`.view_count,`tb_article`.create_time,`tb_article`.status,`tb_article`.position,`tb_article`.review," +
            "tb_user.id,tb_user.user_name,tb_user.avatar FROM " +
            "tb_article LEFT JOIN tb_user ON tb_article.user_id=tb_user.id" +
            " WHERE tb_article.status !='2' and review ='1' and position ='1' and tb_article.state ='1'")
    IPage<ArticleUserDao> reviewArticleList(IPage<ArticleUserDao> page);

    @Update("update `tb_article`  set view_count =view_count+1 where id=#{articleId}")
    int addViewCount(String articleId);

    @Select("select id,title,user_id,category_id,state,summary,view_count,create_time,update_time" +
            " from tb_article where tb_article.user_id=#{userId} and tb_article.state='0'")
    IPage<Article> articleRecycleBin(IPage<Article> page,String userId);

    @Update("update `tb_article`  set state='1' where id=#{articleId}")
    int ArticleRecovery(String articleId);


}
