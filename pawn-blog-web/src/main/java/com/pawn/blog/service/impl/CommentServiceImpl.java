package com.pawn.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pawn.blog.entity.Article;
import com.pawn.blog.entity.Comment;
import com.pawn.blog.entity.User;
import com.pawn.blog.mapper.ArticleMapper;
import com.pawn.blog.mapper.CommentMapper;
import com.pawn.blog.response.Result;
import com.pawn.blog.response.ResultCode;
import com.pawn.blog.service.CommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pawn.blog.service.UserService;
import com.pawn.blog.utils.Constants;
import com.pawn.blog.utils.IdWorker;
import com.pawn.blog.utils.TestUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author pawn
 * @since 2020-12-17
 */
@Service
@Slf4j
@Transactional
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    @Resource
    CommentMapper commentMapper;
    @Resource
    UserService userService;
    @Resource
    ArticleMapper articleMapper;
    @Autowired
    IdWorker idWorker;
    @Autowired
    TestUtils testUtils;

    /**
     * 发表评论
     *
     * @param comment
     * @return
     */
    @Override
    public Result addComment(Comment comment) {
        User user = userService.checkUser();
        if (user == null) return Result.error().code(ResultCode.USER_NOT_LOGIN.getCode())
                .message(ResultCode.USER_NOT_LOGIN.getMessage());

        String articleId = comment.getArticleId();
        if (StringUtils.isEmpty(articleId)) return Result.error().code(ResultCode.COMMENT_LINK_NULL.getCode())
                .message(ResultCode.COMMENT_LINK_NULL.getMessage());

        String releaseId = comment.getReleaseId();
        if (StringUtils.isEmpty(releaseId)) return Result.error().code(ResultCode.COMMENT_LINK_EXCEED.getCode())
                .message(ResultCode.COMMENT_LINK_EXCEED.getMessage());

        String commentType = comment.getCommentType();
        if (StringUtils.isEmpty(commentType)) return Result.error().code(ResultCode.COMMENT_LINK_TYPE.getCode())
                .message(ResultCode.COMMENT_LINK_TYPE.getMessage());

        Article selectById = articleMapper.selectById(articleId);
        if (selectById == null) return Result.error().code(ResultCode.ARTICLE_LINK_OPERATING_NULL.getCode())
                .message(ResultCode.ARTICLE_LINK_OPERATING_NULL.getMessage());

        String parentContent = comment.getParentContent();
        if (StringUtils.isEmpty(parentContent))
            return Result.error().code(ResultCode.COMMENT_LINK_CONTENT_NULL.getCode())
                    .message(ResultCode.COMMENT_LINK_CONTENT_NULL.getMessage());
        //补全数据
        comment.setId(idWorker.nextId() + "")
                .setUserId(user.getId())
                .setUserAvatar(user.getAvatar())
                .setUserName(user.getUserName())
                .setCreateTime(new Date())
                .setUpdateTime(new Date());
        commentMapper.insert(comment);
        //TODO:发邮件通知
        return Result.ok().code(ResultCode.COMMENT_LINK_CONTENT_OK.getCode())
                .message(ResultCode.COMMENT_LINK_CONTENT_OK.getMessage());
    }

    /**
     * 获取评论列表
     *
     * @param commentId 文章id
     * @param page
     * @param size
     * @return
     */
    @Override
    public Result listComments(String commentId, int page, int size) {
        int checkPage = testUtils.checkPage(page);
        int checkSize = testUtils.checkSize(size);
        Page page1 = new Page(checkPage - 1, checkSize);
        Page commentsList = commentMapper.selectPage(page1, new QueryWrapper<Comment>()
                .eq("comment_type", "1")
                .eq("article_id", commentId));
        return Result.ok().code(ResultCode.SUCCESS.getCode())
                .message(ResultCode.SUCCESS.getMessage())
                .data("commentsList", commentsList);
    }
    /**
     * 删除评论
     *
     * @param commentId
     * @return
     */
    @Override
    public Result deleteComment(String commentId) {
        User user = userService.checkUser();
        if (user == null) return Result.error().code(ResultCode.USER_NOT_LOGIN.getCode())
                .message(ResultCode.USER_NOT_LOGIN.getMessage());

        Comment commentById = commentMapper.selectById(commentId);
        if (commentById == null) return Result.error().code(ResultCode.COMMENT_LINK_CONTENT_NOT_EXIST.getCode())
                .message(ResultCode.COMMENT_LINK_CONTENT_NOT_EXIST.getMessage());

        //判断权限
        if (user.getId().equals(commentById.getUserId()) || Constants.User.ROLE_ADMIN.equals(user.getRoles())) {
            commentMapper.deleteById(commentId);
            //删除该评论的二级评论
            commentMapper.deleteCommRes(commentId);
            return Result.ok().code(ResultCode.SUCCESS.getCode())
                    .message(ResultCode.SUCCESS.getMessage());
        } else {
            return Result.error().code(ResultCode.ERROR_403.getCode())
                    .message(ResultCode.ERROR_403.getMessage());
        }
    }

    /**
     * 获取评论列表(所有评论)
     *
     * @param page
     * @param size
     * @return
     */
    @Override
    public Result listCommentsAll(int page, int size) {
        User user = userService.checkUser();
        int checkPage = testUtils.checkPage(page);
        int checkSize = testUtils.checkSize(size);
        Page page1 = new Page(checkPage - 1, checkSize);
        Page commentsListAll = commentMapper.selectPage(page1, new QueryWrapper<Comment>()
                .eq("comment_type", "1")
                .eq("user_id", user.getId()));
        return Result.ok().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage())
                .data("commentsListAll", commentsListAll);
    }

    /**
     * 获取用户评论总数接口
     *
     * @param userId
     * @return
     */
    @Override
    public Result commentTotal(String userId) {
        Integer commentCount = commentMapper.selectCount(new QueryWrapper<Comment>().select("id")
                .eq("comment_type", "1")
                .eq("release_id", userId)
                .eq("state", "1"));
        return Result.ok().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage())
                .data("commentCount", commentCount);
    }

    /**
     * 获取所有评论总数
     *
     * @return
     */
    @Override
    public Result commentTotalPar() {
        Integer commentCount = commentMapper.selectCount(new QueryWrapper<Comment>().select("id")
                .eq("comment_type", "1")
                .eq("state", "1"));
        return Result.ok().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage())
                .data("commentCount", commentCount);
    }

    /**
     * 获取评论回复列表
     *
     * @param releaseId
     * @return
     */
    @Override
    public Result replyList(String releaseId) {
        List<Comment>  replyList= commentMapper.selectList(new QueryWrapper<Comment>()
                .eq("comment_type", "2")
                .eq("release_id", releaseId));
        return Result.ok().code(ResultCode.SUCCESS.getCode())
                .message(ResultCode.SUCCESS.getMessage())
                .data("replyList", replyList);
    }
}
