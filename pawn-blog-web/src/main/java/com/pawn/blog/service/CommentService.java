package com.pawn.blog.service;

import com.pawn.blog.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pawn.blog.response.Result;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author pawn
 * @since 2020-12-17
 */
public interface CommentService extends IService<Comment> {

    Result addComment(Comment comment);

    Result listComments(String commentId,int page, int size);

    Result deleteComment(String commentId);

    Result listCommentsAll(int page, int size);

    Result commentTotal(String userId);

    Result commentTotalPar();

    Result replyList(String releaseId);

}
