package com.pawn.blog.service;

import com.pawn.blog.entity.Article;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pawn.blog.response.Result;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 文章 服务类
 * </p>
 *
 * @author pawn
 * @since 2020-12-17
 */
public interface ArticleService extends IService<Article> {

    Result addArticle(Article article);

    Result listArticle(int page, int size, String status, String keyword, String position, String categoryId);

    Result getArticle(String articleId);

    Result updateArticle(String articleId, Article article);

    Result deleteArticle(String articleId);


    Result deleteUpdateArticleSate(String articleId);

    Result topArticleSate(Article article, String articleId);


    Result gitRecommendSate(String articleId, int size);

    Result gitLabels(String userId,int size);


    Result listArticleLabels(String labels, int page, int size);

    Result getRecommendMend(int size);

    Result getRecommendCommunity(int size);

    Result CommListArticle(int page, int size);

    Result reviewArticle(Article article, String articleId);

    Result reviewArticleList(int page,int size);

    Result getArticleMin(String articleId);

    Result reviewArticleListDid(int page, int size);

    Result capacityArticle(String capacity, int page, int size);

    Result capacityTitleArticle(String title, int page, int size);

    Result emailUpdate(String emailAddress,String name, HttpServletRequest request);

    Result passwordUpdate(String emailAddress, String name, HttpServletRequest request);

    Result articleTotal(String userId);

    Result articleTotalPor();

    Result articleTotalPar();

    Result addViewCount(String articleId);

    Result articleRecycleBin(String userId, int page, int size);

    Result ArticleRecovery(String articleId);
}
