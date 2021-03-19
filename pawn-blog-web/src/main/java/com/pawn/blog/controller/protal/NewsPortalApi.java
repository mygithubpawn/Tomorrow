package com.pawn.blog.controller.protal;

import com.pawn.blog.entity.News;
import com.pawn.blog.response.Result;
import com.pawn.blog.service.impl.NewsServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/***
 * description: 消息控制层
 * @author:美茹冠玉
 * @Return
 * @param
 * @date 2021/2/20 11:26
 */

@RestController
@RequestMapping("/portal/news")
@Api(value = "消息发送，管理", tags = "门户消息管理API")
public class NewsPortalApi {
    @Resource
    NewsServiceImpl newsService;

    /**
     * 消息发送
     *
     * @param news
     * @return
     */
    @PreAuthorize("@permission.user()")
    @PostMapping
    @ApiOperation(value = "消息发送接口",
            notes = "消息发送（添加消息） ")
    public Result addNews(@RequestBody News news) {
        return newsService.addNews(news);
    }

    /**
     * 获取用户所有的消息
     *
     * @param userId
     * @param page
     * @param size
     * @return
     */
    @PreAuthorize("@permission.user()")
    @GetMapping("/list/{userId}/{page}/{size}")
    @ApiOperation(value = "获取用户所有的消息接口",
            notes = "获取用户的所有消息 ")
    public Result NewsList(@PathVariable("userId") String userId,
                           @PathVariable("page") int page,
                           @PathVariable("size") int size) {
        return newsService.NewsList(page, size, userId);
    }

    /**
     * 获取用户未查看消息的总数
     *
     * @param userId
     * @return
     */
    @PreAuthorize("@permission.user()")
    @GetMapping("/list/content/{userId}")
    @ApiOperation(value = "获取用户未查看消息的总数接口",
            notes = "获取用户未查看消息的总数 view==>1  announcement==>2")
    public Result NewsListContent(@PathVariable("userId") String userId) {
        return newsService.NewsListContent(userId);
    }

    /**
     * 获取用户未查看的消息
     *
     * @param userId
     * @return
     */
    @PreAuthorize("@permission.user()")
    @GetMapping("/wait/list/{userId}")
    @ApiOperation(value = "获取用户未查看的消息接口",
            notes = "获取用户未查看的消息 view==>1 announcement==>2")
    public Result NewsListWait(@PathVariable("userId") String userId) {
        return newsService.NewsListWait(userId);
    }

    /**
     * 获取所有的公告
     *
     * @return
     */
    @PreAuthorize("@permission.user()")
    @GetMapping("/announcement/list")
    @ApiOperation(value = "获取所有的公告接口",
            notes = "获取用户未查看的公告 view==>1 announcement==>1")
    public Result AnnouncementList() {
        return newsService.AnnouncementList();
    }

    /**
     * 消息删除接口
     *
     * @param newsId
     * @return
     */
    @PreAuthorize("@permission.admin()")
    @DeleteMapping("/delete/{newsId}")
    @ApiOperation(value = "消息删除接口",
            notes = "消息删除 ")
    public Result NewsDelete(@PathVariable("newsId") String newsId) {
        return newsService.NewsDelete(newsId);
    }

    /**
     * 消息状态修改为已查看
     *
     * @param newsId
     * @return
     */
    @PreAuthorize("@permission.user()")
    @GetMapping("/already/{newsId}")
    @ApiOperation(value = "消息状态修改为已查看接口",
            notes = "消息状态修改为已查看 ")
    public Result NewsAlready(@PathVariable("newsId") String newsId) {
        return newsService.NewsAlready(newsId);
    }

    /**
     * 一键修改消息状态为已修改
     *
     * @param userId
     * @return
     */
    @PreAuthorize("@permission.user()")
    @GetMapping("/news/one/{userId}")
    @ApiOperation(value = "一键修改消息状态为已修改接口",
            notes = "一键修改消息状态为已修改 ")
    public Result newsIdOne(@PathVariable("userId") String userId) {
        return newsService.newsIdOne(userId);
    }

}
