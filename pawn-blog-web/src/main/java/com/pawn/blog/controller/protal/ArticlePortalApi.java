package com.pawn.blog.controller.protal;

import com.pawn.blog.response.Result;
import com.pawn.blog.service.impl.ArticleServiceImpl;
import com.pawn.blog.utils.Constants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/***
 * description:  文章Api(门户)
 * @author:美茹冠玉
 * @Return
 * @param
 * @date 2020/12/18 15:28
 */
@RestController
@RequestMapping("/portal/article")
@Api(value = "门户（文章列表）", tags = "门户文章管理API")
public class ArticlePortalApi {

    @Autowired
    ArticleServiceImpl articleService;

    /**
     * 获取社区文章列表（博客首页）
     *
     * @param page
     * @param size
     * @return
     */
    @GetMapping("community/list/{page}/{size}")
    @ApiOperation(value = "社区文章列表接口",
            notes = "获取发布到社区的文章列表，只获取已发布的文章 ")
    public Result listArticle(@PathVariable("page") int page, @PathVariable("size") int size) {
        return articleService.CommListArticle(page, size);
    }

    /**
     * 通过分类获取文章接口
     *
     * @param page
     * @param size
     * @param categoryId
     * @return
     */
    @GetMapping("/sort/list/{categoryId}/{page}/{size}")
    @ApiOperation(value = "通过分类获取文章接口",
            notes = "通过分类获取文章接口")
    public Result sortistArticleByCategoryId(@PathVariable("page") int page,
                                             @PathVariable("size") int size,
                                             @PathVariable("categoryId") String categoryId) {
        return articleService.listArticle(page, size, Constants.Article.STATE_PUBLISH, null, Constants.Article.POSITION_LOCAL, categoryId);

    }

    /**
     * 通过分类获取文章接口
     *
     * @param page
     * @param size
     * @param title
     * @return
     */
    @GetMapping("/article/list/{title}/{page}/{size}")
    @ApiOperation(value = "门户通过标题获取文章接口",
            notes = "通过标题获取文章接口（不获取草稿）")
    public Result sortistArticleTitle(@PathVariable("page") int page,
                                      @PathVariable("size") int size,
                                      @PathVariable("title") String title) {
        return articleService.listArticle(page, size, null, title, null, null);

    }

    /**
     * 获取用户的所有文章
     *
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/list/{page}/{size}")
    @ApiOperation(value = "用户文章列表接口",
            notes = "获取用户的所有文章，获取已发布的文章 ")
    public Result listArticleByCategoryId(@PathVariable("page") int page,
                                          @PathVariable("size") int size) {
        return articleService.listArticle(page, size, null, null, null, null);

    }

    /**
     * 获取用户草稿列表
     *
     * @param page
     * @param size
     * @param categoryId
     * @return
     */
    @GetMapping("/draft/{categoryId}/{page}/{size}")
    @ApiOperation(value = "用户草稿文章列表接口",
            notes = "获取用户草稿文章列表，获取用户草稿 ")
    public Result listArticleByRaftId(@PathVariable("page") int page,
                                      @PathVariable("size") int size,
                                      @PathVariable("categoryId") String categoryId) {
        return articleService.listArticle(page, size, Constants.Article.STATE_DRAFT, null, Constants.Article.POSITION_LOCAL, categoryId);

    }

    /**
     * 获取用户本地文章
     *
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/local/{page}/{size}")
    @ApiOperation(value = "用户本地文章列表接口",
            notes = "获取用户本地文章列表，获取用户发布到本地有的文章 ")
    public Result listArticleByLocal(@PathVariable("page") int page,
                                     @PathVariable("size") int size) {
        return articleService.listArticle(page, size, null, null, Constants.Article.POSITION_LOCAL, null);

    }

    /**
     * 获取用户社区文章
     *
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/comment/{page}/{size}")
    @ApiOperation(value = "用户社区文章列表接口",
            notes = "获取用户社区文章列表，获取用户发布到社区有的文章")
    public Result listArticleByComment(@PathVariable("page") int page,
                                       @PathVariable("size") int size) {
        return articleService.listArticle(page, size, null, null, Constants.Article.POSITION_COMMUNITY, null);

    }

    /**
     * 获取文章详情
     *
     * @param articleId
     * @return
     */
    @ApiOperation(value = "获取文章详情接口",
            notes = "获取文章详情")
    @GetMapping("/{articleId}")
    public Result getArticleDetail(@PathVariable("articleId") String articleId) {
        return articleService.getArticle(articleId);
    }

    /**
     * 文章推荐
     *
     * @param articleId
     * @param size
     * @return
     */
    @GetMapping("/recommend/{articleId}/{size}")
    @ApiOperation(value = "文章推荐接口",
            notes = "通过标签进行匹配")
    public Result getRecommendArticle(@PathVariable("articleId") String articleId, @PathVariable("size") int size) {
        return articleService.gitRecommendSate(articleId, size);
    }

    /**
     * 博客首页文章推荐
     *
     * @param size
     * @return
     */
    @GetMapping("/recommend/{size}")
    @ApiOperation(value = "博客首页文章推荐接口",
            notes = "通过浏览量进行匹配")
    public Result getRecommendMend(@PathVariable("size") int size) {
        return articleService.getRecommendMend(size);
    }


    /**
     * 博客首页文章推荐(社区文章)
     *
     * @param size
     * @return
     */
    @GetMapping("/recommend/community/{size}")
    @ApiOperation(value = "博客首页文章推荐接口(社区文章)",
            notes = "通过浏览量进行匹配")
    public Result getRecommendCommunity(@PathVariable("size") int size) {
        return articleService.getRecommendCommunity(size);
    }

    /**
     * 通过标签获取文章列表接口
     *
     * @param labels
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/list/labels/{labels}/{page}/{size}")
    @ApiOperation(value = "通过标签获取文章列表接口",
            notes = "通过标签进行匹配")
    public Result getRecommendArticle(@PathVariable("labels") String labels,
                                      @PathVariable("page") int page,
                                      @PathVariable("size") int size) {
        return articleService.listArticleLabels(labels, page, size);
    }

    /**
     * 通过分类获取文章列表接口（社区接口）
     *
     * @param capacity
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/list/capacity/{capacity}/{page}/{size}")
    @ApiOperation(value = "通过分类获取文章列表接口（社区接口）",
            notes = "通过分类获取文章列表接口")
    public Result capacityArticle(@PathVariable("capacity") String capacity,
                                  @PathVariable("page") int page,
                                  @PathVariable("size") int size) {
        return articleService.capacityArticle(capacity, page, size);
    }

    /**
     * 通过标题获取文章列表接口（社区接口）
     *
     * @param title
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/capacity/title/{title}/{page}/{size}")
    @ApiOperation(value = "通过标题获取文章列表接口（社区接口）",
            notes = "通过标题获取文章列表接口")
    public Result capacityTitleArticle(@PathVariable("title") String title,
                                       @PathVariable("page") int page,
                                       @PathVariable("size") int size) {
        return articleService.capacityTitleArticle(title, page, size);
    }

    /**
     * 获取社区文章总数接口
     *
     * @return
     */
    @GetMapping("/get/total")
    @ApiOperation(value = "获取社区文章总数接口",
            notes = "获取社区文章总数")
    public Result articleTotalPor() {
        return articleService.articleTotalPor();
    }

    /**
     * 获取标签云
     *
     * @param size
     * @return
     */
    @GetMapping("/Labels/{userId}/{size}")
    @ApiOperation(value = "标签云接口",
            notes = "获取标签云,通过标签数量没排序")
    public Result gitLabels(@PathVariable("userId") String userId,@PathVariable("size") int size) {
        return articleService.gitLabels(userId,size);
    }

    /**
     * 获取置顶文章
     *
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/private/top/{page}/{size}")
    @ApiOperation(value = "用户置顶文章列表接口",
            notes = "获取用户置顶文章列表，获取用户置顶的文章 ")
    public Result topArticle(@PathVariable("page") int page,
                             @PathVariable("size") int size,
                             @RequestParam(value = "categoryId", required = false) String categoryId) {
        return articleService.listArticle(page, size, Constants.Article.STATE_TOP, null, null, categoryId);
    }

    /**
     * 增加文章浏览量
     *
     * @param articleId
     * @return
     */
    @GetMapping("/private/count/{articleId}")
    @ApiOperation(value = "浏览量接口",
            notes = "增加文章浏览量")
    public Result addViewCount(@PathVariable("articleId") String articleId) {
        return articleService.addViewCount(articleId);
    }
}
