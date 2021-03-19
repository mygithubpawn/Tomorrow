package com.pawn.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pawn.blog.entity.Comment;
import com.pawn.blog.entity.News;
import com.pawn.blog.entity.User;
import com.pawn.blog.mapper.NewsMapper;
import com.pawn.blog.response.Result;
import com.pawn.blog.response.ResultCode;
import com.pawn.blog.service.NewsService;
import com.pawn.blog.service.UserService;
import com.pawn.blog.utils.IdWorker;
import com.pawn.blog.utils.TestUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/***
 * description: 消息实现类
 * @author:美茹冠玉
 * @Return
 * @param
 * @date 2021/2/20 11:05
 */

@Slf4j
@Service
@Transactional
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News> implements NewsService {

    @Autowired
    UserService userService;
    @Resource
    NewsMapper newsMapper;
    @Autowired
    IdWorker idWorker;
    @Autowired
    TestUtils testUtils;

    /**
     * 消息发送
     *
     * @param news
     * @return
     */
    @Override
    public Result addNews(News news) {
        User user = userService.checkUser();
        if (StringUtils.isEmpty(news.getContent())) {
            return Result.error().code(ResultCode.NEWS_CONTENT_NULL.getCode())
                    .message(ResultCode.NEWS_CONTENT_NULL.getMessage());
        }
        if (StringUtils.isEmpty(news.getReceiverId())) {
            return Result.error().code(ResultCode.NEWS_RECEIVER_ID_EXCEED.getCode())
                    .message(ResultCode.NEWS_RECEIVER_ID_EXCEED.getMessage());
        }
        if (StringUtils.isEmpty(news.getAnnouncement())) {
            return Result.error().code(ResultCode.NEWS_ANNOUNCEMENT_EXCEED.getCode())
                    .message(ResultCode.NEWS_ANNOUNCEMENT_EXCEED.getMessage());
        }
        //补全数据
        news.setId(idWorker.nextId() + "")
                .setNewsId(user.getId())
                .setNewsAver(user.getAvatar())
                .setNewsName(user.getUserName())
                .setState("1")
                .setView("1")
                .setCreateTime(new Date())
                .setUpdateTime(new Date());
        int insertNews = newsMapper.insert(news);
        return insertNews > 0 ? Result.ok().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage())
                : Result.ok().code(ResultCode.NEWS_CONTENT_RELEASE_EXCEED.getCode()).message(ResultCode.NEWS_CONTENT_RELEASE_EXCEED.getMessage());
    }


    /**
     * 获取用户所有的消息
     *
     * @param userId
     * @return
     */

    @Override
    public Result NewsList(int page, int size, String userId) {
        if (StringUtils.isEmpty(userId)) {
            return Result.error().code(ResultCode.NEWS_NEWS_EXCEED.getCode())
                    .message(ResultCode.NEWS_NEWS_EXCEED.getMessage());
        }
        int checkPage = testUtils.checkPage(page);
        int checkSize = testUtils.checkSize(size);
        Page page1 = new Page(checkPage - 1, checkSize);
        Page newsList = newsMapper.selectPage(page1, new QueryWrapper<News>()
                .eq("receiver_id", userId)
                .eq("state", "1")
                .orderByAsc("view"));
        return Result.ok().code(ResultCode.SUCCESS.getCode())
                .message(ResultCode.SUCCESS.getMessage())
                .data("newsList", newsList);
    }

    /**
     * 获取用户未查看消息的总数
     *
     * @param userId
     * @return
     */
    @Override
    public Result NewsListContent(String userId) {
        if (StringUtils.isEmpty(userId)) {
            return Result.error().code(ResultCode.NEWS_NEWS_EXCEED.getCode())
                    .message(ResultCode.NEWS_NEWS_EXCEED.getMessage());
        }
        Integer integerContent = newsMapper.selectCount(new QueryWrapper<News>().select("id")
                .eq("receiver_id", userId)
                .eq("state", "1")
                .eq("view", "1"));
        return Result.ok().code(ResultCode.SUCCESS.getCode())
                .message(ResultCode.SUCCESS.getMessage())
                .data("integerContent", integerContent);
    }

    /**
     * 获取用户未查看的消息
     *
     * @param userId
     * @return
     */
    @Override
    public Result NewsListWait(String userId) {
        if (StringUtils.isEmpty(userId)) {
            return Result.error().code(ResultCode.NEWS_NEWS_EXCEED.getCode())
                    .message(ResultCode.NEWS_NEWS_EXCEED.getMessage());
        }
        List<News> newsList = newsMapper.selectList(new QueryWrapper<News>()
                .eq("receiver_id", userId)
                .eq("state", "1")
                .eq("announcement", "2")
                .eq("view", "1"));
        return Result.ok().code(ResultCode.SUCCESS.getCode())
                .message(ResultCode.SUCCESS.getMessage())
                .data("newsList", newsList);
    }

    /**
     * 获取用户未查看的公告
     *
     * @return
     */
    @Override
    public Result AnnouncementList() {
        List<News> newsList = newsMapper.selectList(new QueryWrapper<News>()
                .eq("state", "1")
                .eq("announcement", "1")
                .eq("view", "1"));
        return Result.ok().code(ResultCode.SUCCESS.getCode())
                .message(ResultCode.SUCCESS.getMessage())
                .data("newsList", newsList);
    }

    /**
     * 消息删除
     *
     * @param newsId
     * @return
     */
    @Override
    public Result NewsDelete(String newsId) {
        if (StringUtils.isEmpty(newsId)) {
            return Result.error().code(ResultCode.NEWS_NEWS_EXCEED.getCode())
                    .message(ResultCode.NEWS_NEWS_EXCEED.getMessage());
        }
        newsMapper.deleteById(newsId);
        return Result.ok().code(ResultCode.SUCCESS.getCode())
                .message(ResultCode.SUCCESS.getMessage());
    }

    /**
     * 消息状态修改为已查看
     *
     * @param newsId
     * @return
     */
    @Override
    public Result NewsAlready(String newsId) {
        if (StringUtils.isEmpty(newsId)) {
            return Result.error().code(ResultCode.NEWS_NEWS_EXCEED.getCode())
                    .message(ResultCode.NEWS_NEWS_EXCEED.getMessage());
        }
        newsMapper.update(null, new UpdateWrapper<News>()
                .eq("id", newsId)
                .eq("state", "1")
                .set("view", "2"));
        return Result.ok().code(ResultCode.SUCCESS.getCode())
                .message(ResultCode.SUCCESS.getMessage());
    }

    /**
     * 用户消息一键已读
     *
     * @param userId
     * @return
     */
    @Override
    public Result newsIdOne(String userId) {
        if (StringUtils.isEmpty(userId)) {
            return Result.error().code(ResultCode.NEWS_NEWS_EXCEED.getCode())
                    .message(ResultCode.NEWS_NEWS_EXCEED.getMessage());
        }
        newsMapper.update(null, new UpdateWrapper<News>()
                .eq("receiver_id", userId)
                .eq("state", "1")
                .eq("view", "1")
                .set("view", "2"));
        return Result.ok().code(ResultCode.SUCCESS.getCode())
                .message(ResultCode.SUCCESS.getMessage());
    }
}
