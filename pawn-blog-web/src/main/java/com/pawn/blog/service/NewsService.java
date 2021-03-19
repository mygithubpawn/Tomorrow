package com.pawn.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pawn.blog.entity.News;
import com.pawn.blog.response.Result;

/***
 * description: 消息服务类
 * @author:美茹冠玉
 * @Return
 * @param
 * @date 2021/2/20 11:04
 */

public interface NewsService extends IService<News> {
    Result addNews(News news);

    Result NewsList(int page, int size, String userId);

    Result NewsListContent(String userId);

    Result NewsListWait(String userId);

    Result AnnouncementList();

    Result NewsDelete(String newsId);

    Result NewsAlready(String newsId);

    Result newsIdOne(String userId);
}
