package com.pawn.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pawn.blog.entity.Article;
import com.pawn.blog.entity.Looper;
import com.pawn.blog.entity.User;
import com.pawn.blog.entity.WebInfo;
import com.pawn.blog.mapper.ArticleMapper;
import com.pawn.blog.mapper.LooperMapper;
import com.pawn.blog.mapper.WebInfoMapper;
import com.pawn.blog.response.Result;
import com.pawn.blog.response.ResultCode;
import com.pawn.blog.service.WebInfoService;
import com.pawn.blog.utils.Constants;
import com.pawn.blog.utils.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/***
 * description: 服务实现类
 * @author:美茹冠玉
 * @Return
 * @param
 * @date 2021/1/12 12:39
 */
@Slf4j
@Service
@Transactional
public class WebInfoServiceImpl extends ServiceImpl<WebInfoMapper, WebInfo> implements WebInfoService {
    @Autowired
    UserServiceImpl userService;
    @Resource
    LooperMapper looperMapper;
    @Resource
    ArticleMapper articleMapper;
    @Autowired
    private IdWorker idWorker;
    @Resource
    WebInfoMapper webInfoMapper;

    /**
     * 初始化网站信息
     *
     * @param userId
     * @return
     */
    public Result initializationWebSize(String userId, String userName) {
        //网站信息初始
        WebInfo webInfo = new WebInfo()
                .setId(idWorker.nextId() + "")
                .setKeyword("key")
                .setIntroduction(userName + "的网站")
                .setUserId(userId)
                .setValue(userName + Constants.User.WEB_SIZE_INFO_IN)
                .setCreateTime(new Date())
                .setUpdateTime(new Date());
        webInfoMapper.insert(webInfo);
        //轮播图初始
        Looper looper = new Looper()
                .setId(idWorker.nextId() + "")
                .setUserId(userId)
                .setTitle("Tomorrow")
                .setImageUrl("http://zsh-pawn.oss-cn-shenzhen.aliyuncs.com/pawn-bok/024b6b39-d3de-471d-84a5-b1d75ebb1b61.jpg")
                .setTargetUrl("https://img.tukuppt.com//ad_preview/00/11/82/5c994ffec3bbf.jpg!/fw/780")
                .setIntroduction("但行好事 - 莫问前程")
                .setOrder(1)
                .setCreateTime(new Date())
                .setUpdateTime(new Date());
        looperMapper.insert(looper);
        //文章初始化
        Article article = new Article()
                .setId(idWorker.nextId() + "")
                .setUserId(userId)
                .setTitle("Tomorrow介绍【必阅】")
                .setUserId(userId)
                .setCategoryId("Tomorrow")
                .setContent(Constants.User.ARTICLE_CONTENT)
                .setViewCount(1)
                .setType("2")
                .setReview("2")
                .setState("1")
                .setStatus("1")
                .setCover("http://zsh-pawn.oss-cn-shenzhen.aliyuncs.com/pawn-bok/57793b0a-5c7e-42ea-aaa1-b3292eea792b.jpg")
                .setPosition("2")
                .setSummary("Tomorrow介绍！")
                .setCreateTime(new Date())
                .setUpdateTime(new Date());
        article.setLabels("博客-简介");
        articleMapper.insert(article);

        return Result.ok().code(ResultCode.SUCCESS.getCode())
                .message(ResultCode.SUCCESS.getMessage());
    }

    /**
     * 获取网站标题
     *
     * @return
     */
    @Override
    public Result getWebSizeTitle() {
        User user = userService.checkUser();
        WebInfo webInfo = webInfoMapper.selectOne(new QueryWrapper<WebInfo>().eq("user_id", user.getId()));
        if (webInfo == null)
            return Result.error().code(ResultCode.ERROR_4000.getCode())
                    .message(ResultCode.ERROR_4000.getMessage());

        return Result.ok().code(ResultCode.SUCCESS.getCode())
                .message(ResultCode.SUCCESS.getMessage()).data("webInfo", webInfo);
    }

    /**
     * 修改网站信息
     * <p>
     * 只支持修改网站标题
     *
     * @param title
     * @return
     */
    @Override
    public Result updateWebSizeTitle(String title) {
        User user = userService.checkUser();
        if (StringUtils.isEmpty(title))
            return Result.error().code(ResultCode.WEB_SIZE_INFO_TITLE.getCode())
                    .message(ResultCode.WEB_SIZE_INFO_TITLE.getMessage());
        //判断网站标题
        Boolean aBoolean = webInfoTitle(title);
        if (aBoolean == false)
            return Result.error().code(ResultCode.WEB_SIZE_INFO_TITLE_REPEAT.getCode())
                    .message(ResultCode.WEB_SIZE_INFO_TITLE_REPEAT.getMessage());
        //填充数据
        WebInfo webInfo = new WebInfo()
                .setValue(title)
                .setUpdateTime(new Date());
        //修改数据
        int update = webInfoMapper.update(webInfo, new UpdateWrapper<WebInfo>().eq("user_id", user.getId()));
        return update > 0 ? Result.ok().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage())
                : Result.error().code(ResultCode.WEB_SIZE_INFO_TITLE_EXCEED.getCode()).message(ResultCode.WEB_SIZE_INFO_TITLE_EXCEED.getMessage());
    }

    /**
     * 校验网站标题
     *
     * @param value
     * @return
     */

    public Boolean webInfoTitle(String value) {
        User user = userService.checkUser();
        WebInfo WebInfoFrom = webInfoMapper.selectOne(new QueryWrapper<WebInfo>().select("value").eq("value", value));
        WebInfo WebInfoId = webInfoMapper.selectOne(new QueryWrapper<WebInfo>().select("value").eq("user_id", user.getId()));
        if (!WebInfoId.getValue().equals(value) && WebInfoFrom != null) {
            return false;
        }
        return true;
    }

    /**
     * 更新网站信息
     *
     * @param webInfo
     * @return
     */
    @Override
    public Result putSeoInfo(WebInfo webInfo) {
        User user = userService.checkUser();
        //获取当前用户的网站信息
        WebInfo webInfoFrom = webInfoMapper.selectOne(new QueryWrapper<WebInfo>().eq("user_id", user.getId()));
        //检查数据
        String value = webInfo.getValue();
        if (!StringUtils.isEmpty(value)) {
            //判断网站标题
            Boolean aBoolean = webInfoTitle(value);
            if (aBoolean == false) {
                return Result.error().code(ResultCode.WEB_SIZE_INFO_TITLE_REPEAT.getCode())
                        .message(ResultCode.WEB_SIZE_INFO_TITLE_REPEAT.getMessage());
            }

            webInfoFrom.setValue(value);
        }
        String webInfoKey = webInfo.getKeyword();
        if (!StringUtils.isEmpty(webInfoKey)) {
            webInfoFrom.setKeyword(webInfoKey);
        }
        String introduction = webInfo.getIntroduction();
        if (!StringUtils.isEmpty(introduction)) {
            webInfoFrom.setIntroduction(introduction);
        }
        //补全数据
        webInfoFrom.setUpdateTime(new Date());
        //修改数据
        int update = webInfoMapper.update(webInfoFrom, new UpdateWrapper<WebInfo>().eq("user_id", user.getId()));
        return update > 0 ? Result.ok().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage())
                : Result.error().code(ResultCode.WEB_SIZE_INFO_EXCEED.getCode()).message(ResultCode.WEB_SIZE_INFO_EXCEED.getMessage());
    }

    /**
     * 校验用户标题接口
     *
     * @param value
     * @return
     */
    @Override
    public Result checkWebInfoTitle(String value) {
        //判断网站标题
        Boolean aBoolean = webInfoTitle(value);
        if (aBoolean == false)
            return Result.error().code(ResultCode.WEB_SIZE_INFO_TITLE_REPEAT.getCode())
                    .message(ResultCode.WEB_SIZE_INFO_TITLE_REPEAT.getMessage());

        return Result.ok().code(ResultCode.SUCCESS.getCode())
                .message(ResultCode.SUCCESS.getMessage());

    }
}
