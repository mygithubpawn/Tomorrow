package com.pawn.blog.controller.protal;

import com.pawn.blog.entity.Comment;
import com.pawn.blog.response.Result;
import com.pawn.blog.service.impl.CommentServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/***
 * description: 评论Api
 * @author:美茹冠玉
 * @Return
 * @param
 * @date 2020/12/18 15:26
 */
@RestController
@RequestMapping("/portal/comment")
@Api(value = "文章评论", tags = "门户文章评论管理API")
public class CommentPortalApi {
    @Resource
    CommentServiceImpl commentService;

    /**
     * 添加评论
     *
     * @param comment
     * @return
     */
    @PostMapping
    @ApiOperation(value = "评论文章接口",
            notes = "添加评论 ")
    public Result addComment(@RequestBody Comment comment) {
        return commentService.addComment(comment);
    }

    /**
     * 获取评论列表
     *
     * @param commentId
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/list/{commentId}/{page}/{size}")
    @ApiOperation(value = "文章评论列表接口",
            notes = "获取该文章的所有评论 ")
    public Result listComments(@PathVariable("commentId") String commentId,
                               @PathVariable("page") int page,
                               @PathVariable("size") int size) {
        return commentService.listComments(commentId, page, size);
    }
    /**
     * 获取评论回复列表
     *
     * @param releaseId
     * @return
     */
    @GetMapping("/list/release/{releaseId}")
    @ApiOperation(value = "文章评论回复列表接口",
            notes = "获取评论回复列表 ")
    public Result replyList(@PathVariable("releaseId") String releaseId) {
        return commentService.replyList(releaseId);
    }
}
