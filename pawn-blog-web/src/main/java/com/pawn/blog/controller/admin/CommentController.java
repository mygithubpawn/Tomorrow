package com.pawn.blog.controller.admin;


import com.pawn.blog.entity.Comment;
import com.pawn.blog.entity.Looper;
import com.pawn.blog.response.Result;
import com.pawn.blog.service.impl.CommentServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 评论控制器
 * </p>
 *
 * @author pawn
 * @since 2020-12-17
 */
@RestController
@RequestMapping("/comment")
@Api(value = "评论管理", tags = "评论管理API")
public class CommentController {
    @Resource
    CommentServiceImpl commentService;

    /**
     * 获取所有评论总数
     *
     * @return
     */
    @GetMapping("/comment/Total")
    @ApiOperation(value = "获取所有评论总数接口",
            notes = "获取数据库所有评论的总数")
    public Result commentTotalPar() {
        return commentService.commentTotalPar();
    }

    /**
     * 获取单个评论
     *
     * @param commentId
     * @return
     */
    @GetMapping("/get/{commentId}")
    @ApiOperation(value = "获取单个评论接口",
            notes = "获取单个评论")
    public Result getComment(@PathVariable("commentId") String commentId) {
        return null;
    }

    /**
     * 删除评论
     *
     * @param commentId
     * @return
     */
    @PreAuthorize("@permission.user()")
    @DeleteMapping("/delete/{commentId}")
    @ApiOperation(value = "评论删除接口",
            notes = "评论删除")
    public Result deleteComment(@PathVariable("commentId") String commentId) {
        return commentService.deleteComment(commentId);
    }

    /**
     * 获取评论列表
     *
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "获取评论列表接口",
            notes = "获取该用户的所有评论")
    public Result listComment(@RequestParam("page") int page, @RequestParam("size") int size) {
        return commentService.listCommentsAll(page, size);
    }

    /**
     * 获取用户评论总数接口
     *
     * @param userId
     * @return
     */
    @PreAuthorize("@permission.user()")
    @GetMapping("/get/total/{userId}")
    @ApiOperation(value = "获取用户评论总数接口",
            notes = "获取用户评论总数")
    public Result commentTotal(@PathVariable("userId") String userId) {
        return commentService.commentTotal(userId);
    }

    /**
     * 评论置顶
     *
     * @param commentId
     * @return
     */
    @PreAuthorize("@permission.user()")
    @PutMapping("/top/{commentId}")
    @ApiOperation(value = "评论置顶接口",
            notes = "评论置顶")
    public Result topComment(@PathVariable("commentId") String commentId) {
        return null;
    }
}

