package com.pawn.blog.controller.admin;


import com.pawn.blog.entity.Article;
import com.pawn.blog.response.Result;
import com.pawn.blog.service.impl.ArticleServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 文章控制器
 * </p>
 *
 * @author pawn
 * @since 2020-12-17
 */
@RestController
@RequestMapping("/article")
@Api(value = "文章管理", tags = "文章管理API")
public class ArticleController {
    @Autowired
    ArticleServiceImpl articleService;

    /**
     * 添加文章
     *
     * @param article
     * @return
     */
    @PreAuthorize("@permission.user()")
    @PostMapping
    @ApiOperation(value = "添加文章与提交草稿接口",
            notes = "发布文章，需填写类容有：分类id，文章标题，文章类容，文章摘要，标签，文章发布状态，类型，发布位置，同时支持保存草稿：type=2")
    public Result addArticle(@RequestBody Article article) {
        return articleService.addArticle(article);
    }

    /**
     * 文章删除（
     * <p>
     * 管理员删除：真删）
     *
     * @param articleId
     * @return
     */
    @PreAuthorize("@permission.user()")
    @DeleteMapping("/delete/{articleId}")
    @ApiOperation(value = "文章物理删除接口",
            notes = "文章删除，物理删出，只能文章发布用户有权限操作 ")
    public Result deleteArticle(@PathVariable("articleId") String articleId) {
        return articleService.deleteArticle(articleId);
    }


    /**
     * 更新文章
     *
     * @param articleId
     * @param article
     * @return
     */
    @PreAuthorize("@permission.user()")
    @PutMapping("/update/{articleId}")
    @ApiOperation(value = "修改文章接口",
            notes = "该接口支持修改类容：标题，标签，类容，分类，摘要")
    public Result updateArticle(@PathVariable("articleId") String articleId, @RequestBody Article article) {
        return articleService.updateArticle(articleId, article);
    }

    /**
     * 获取文章详情
     *
     * @param articleId
     * @return
     */
    @GetMapping("/get/{articleId}")
    @ApiOperation(value = "查询单篇文章接口（用户）",
            notes = "获取单篇文章，")
    public Result getArticle(@PathVariable("articleId") String articleId) {
        return articleService.getArticle(articleId);
    }

    /**
     * 文章恢复
     *
     * @param articleId
     * @return
     */
    @PreAuthorize("@permission.user()")
    @GetMapping("/recovery/{articleId}")
    @ApiOperation(value = "文章恢复接口",
            notes = "文章恢复")
    public Result ArticleRecovery(@PathVariable("articleId") String articleId) {
        return articleService.ArticleRecovery(articleId);
    }

    /**
     * 获取回收站文章列表
     *
     * @param userId
     * @return
     */
    @PreAuthorize("@permission.user()")
    @GetMapping("/get/recycle/{userId}/{page}/{size}")
    @ApiOperation(value = "获取回收站文章列表接口",
            notes = "获取回收站文章列表 state=0，")
    public Result articleRecycleBin(@PathVariable("userId") String userId,
                                    @PathVariable("page") int page,
                                    @PathVariable("size") int size) {
        return articleService.articleRecycleBin(userId, page, size);
    }

    /**
     * 获取单单篇文章（后台）
     *
     * @param articleId
     * @return
     */
    @PreAuthorize("@permission.user()")
    @GetMapping("/get/min/{articleId}")
    @ApiOperation(value = "获取单单篇文章接口（后台）",
            notes = "获取单篇文章，")
    public Result getArticleMin(@PathVariable("articleId") String articleId) {
        return articleService.getArticleMin(articleId);
    }

    /**
     * 获取用户文章总数接口
     *
     * @param userId
     * @return
     */
    @PreAuthorize("@permission.user()")
    @GetMapping("/get/total/{userId}")
    @ApiOperation(value = "获取用户文章总数接口",
            notes = "获取用户文章总数")
    public Result articleTotal(@PathVariable("userId") String userId) {
        return articleService.articleTotal(userId);
    }

    /**
     * 获取文章总数接口
     *
     * @return
     */
    @ApiOperation(value = "获取所有文章总数接口",
            notes = "获取数据库的所有文章总数")
    public Result articleTotalPar() {
        return articleService.articleTotalPar();
    }

    /**
     * 获取文章列表
     *
     * @param page
     * @param size
     * @param status     文章状态：，1表示已发布，2表示草稿，3表示置顶
     * @param keyword    搜索关建子，标题
     * @param position   发布的位置： 1表示发布到社区，2表示发布到本地博客，3表示私有
     * @param categoryId 分类id
     * @return
     */
    @PreAuthorize("@permission.user()")
    @GetMapping("/list/{page}/{size}")
    @ApiOperation(value = "获取文章列表接口",
            notes = "支持多条件的查询，")
    public Result listArticle(@PathVariable("page") int page,
                              @PathVariable("size") int size,
                              @RequestParam(value = "status", required = false) String status,
                              @RequestParam(value = "position", required = false) String position,
                              @RequestParam(value = "keyword", required = false) String keyword,
                              @RequestParam(value = "categoryId", required = false) String categoryId) {
        return articleService.listArticle(page, size, status, keyword, position, categoryId);
    }

    /***
     * 删除文章，只修改状态，
     * @param articleId
     * @return
     */
    @PreAuthorize("@permission.user()")
    @DeleteMapping("/sate/{articleId}")
    @ApiOperation(value = "文章逻辑删除接口",
            notes = "文章删除，逻辑删出，只能文章发布用户有权限操作 ")
    public Result deleteUpdateArticleSate(@PathVariable("articleId") String articleId) {
        return articleService.deleteUpdateArticleSate(articleId);
    }


    /**
     * 文章置顶
     *
     * @param articleId
     * @return
     */
    @PreAuthorize("@permission.user()")
    @PutMapping("/top/{articleId}")
    @ApiOperation(value = "文章置顶接口",
            notes = "只能在用户本地实现，社区实现需管理员权限 ")
    public Result topArticleSate(@PathVariable("articleId") String articleId, @RequestBody Article article) {
        return articleService.topArticleSate(article, articleId);
    }

    /**
     * 文章审核
     *
     * @return
     */
    @PreAuthorize("@permission.admin()")
    @PutMapping("/review/{articleId}")
    @ApiOperation(value = "文章审核接口",
            notes = "文章审核，需要管理员权限 ")
    public Result reviewArticle(@PathVariable("articleId") String articleId, @RequestBody Article article) {
        return articleService.reviewArticle(article, articleId);
    }

    /**
     * 获取待审核文章
     *
     * @return
     */
    @PreAuthorize("@permission.admin()")
    @GetMapping("/review/list/{page}/{size}")
    @ApiOperation(value = "获取待审核文章接口",
            notes = "获取待审核文章，需要管理员权限 ")
    public Result reviewArticleList(@PathVariable("page") int page, @PathVariable("size") int size) {
        return articleService.reviewArticleList(page, size);
    }

    /**
     * 获取审核未通过文章
     *
     * @return
     */
    @PreAuthorize("@permission.user()")
    @GetMapping("/review/did/{page}/{size}")
    @ApiOperation(value = "获取审核未通过文章接口",
            notes = "获取审核未通过文章 ")
    public Result reviewArticleListDid(@PathVariable("page") int page, @PathVariable("size") int size) {
        return articleService.reviewArticleListDid(page, size);
    }

}

