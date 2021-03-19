package com.pawn.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pawn.blog.entity.Looper;
import com.pawn.blog.entity.User;
import com.pawn.blog.mapper.LooperMapper;
import com.pawn.blog.response.Result;
import com.pawn.blog.response.ResultCode;
import com.pawn.blog.service.LooperService;
import com.pawn.blog.utils.IdWorker;
import com.pawn.blog.utils.OssUtil;
import com.pawn.blog.utils.TestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * <p>
 * 服务实现类
 * </p>2
 *
 * @author pawn
 * @since 2020-12-17
 */
@Service
@Transactional
public class LooperServiceImpl extends ServiceImpl<LooperMapper, Looper> implements LooperService {

    @Autowired
    private OssUtil ossUtil;
    @Autowired
    private UserServiceImpl userService;
    @Resource
    private LooperMapper looperMapper;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private TestUtils testUtils;

    /**
     * 添加轮播图
     *
     * @param looper
     * @return
     */
    @Override
    public Result addLooper(Looper looper) {
        User user = userService.checkUser();
        //检查数据
        if (StringUtils.isEmpty(looper.getTitle())) return Result.error().code(ResultCode.CAROUSEL_TITLE_NULL.getCode())
                .message(ResultCode.CAROUSEL_TITLE_NULL.getMessage());

        if (StringUtils.isEmpty(looper.getTargetUrl()))
            return Result.error().code(ResultCode.CAROUSEL_TARGET_URL_NULL.getCode())
                    .message(ResultCode.CAROUSEL_TARGET_URL_NULL.getMessage());

        if (StringUtils.isEmpty(looper.getImageUrl()))
            return Result.error().code(ResultCode.CAROUSEL_IMAGE_URL_NULL.getCode())
                    .message(ResultCode.CAROUSEL_IMAGE_URL_NULL.getMessage());

        //填充数据
        looper.setId(idWorker.nextId() + "")
                .setUserId(user.getId())
                .setState("1")
                .setCreateTime(new Date())
                .setUpdateTime(new Date());
        //添加数据
        int insert = looperMapper.insert(looper);
        //返回结果
        return insert > 0 ? Result.ok().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage())
                : Result.ok().code(ResultCode.CAROUSEL_ADD_EXCEED.getCode())
                .message(ResultCode.CAROUSEL_ADD_EXCEED.getMessage());
    }

    /**
     * 轮播图删除
     *
     * @param looperId
     * @return
     */
    @Override
    public Result deleteLooper(String looperId) {
        User user = userService.checkUser();
        if (StringUtils.isEmpty(looperId))
            return Result.error().code(ResultCode.CAROUSEL_IMAGE_URL_NULL.getCode())
                    .message(ResultCode.CAROUSEL_IMAGE_URL_NULL.getMessage());
        Looper looper = looperMapper.selectById(looperId);
        if (looper == null)
            return Result.error().code(ResultCode.CAROUSEL_IMAGE_EXCEED.getCode())
                    .message(ResultCode.CAROUSEL_IMAGE_EXCEED.getMessage());
        if (!looper.getUserId().equals(user.getId()))
            return Result.error().code(ResultCode.NO_PERMISSION.getCode())
                    .message(ResultCode.NO_PERMISSION.getMessage());
        //删除数据
        int deleteById = looperMapper.deleteById(looperId);

        return deleteById > 0 ? Result.ok().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage())
                : Result.ok().code(ResultCode.CAROUSEL_DELETE_EXCEED.getCode())
                .message(ResultCode.CAROUSEL_DELETE_EXCEED.getMessage());
    }

    /**
     * 更新轮播图
     *
     * @param looperId
     * @param looper
     * @return
     */
    @Override
    public Result updateLooper(String looperId, Looper looper) {
        if (StringUtils.isEmpty(looperId))
            return Result.error().code(ResultCode.CAROUSEL_IMAGE_URL_NULL.getCode())
                    .message(ResultCode.CAROUSEL_IMAGE_URL_NULL.getMessage());
        User user = userService.checkUser();
        Looper looperFrom = looperMapper.selectById(looperId);
        if (looperFrom == null)
            return Result.error().code(ResultCode.CAROUSEL_IMAGE_EXCEED.getCode())
                    .message(ResultCode.CAROUSEL_IMAGE_EXCEED.getMessage());
        String userId = user.getId();
        if (!looperFrom.getUserId().equals(userId))
            return Result.error().code(ResultCode.NO_PERMISSION.getCode())
                    .message(ResultCode.NO_PERMISSION.getMessage());
        String title = looper.getTitle();
        if (!StringUtils.isEmpty(title)) {
            looperFrom.setTitle(title);
        }
        String introduction = looper.getIntroduction();
        if (!StringUtils.isEmpty(introduction)) {
            looperFrom.setIntroduction(introduction);
        }
        String imageUrl = looper.getImageUrl();
        if (!StringUtils.isEmpty(imageUrl)) {
            looperFrom.setImageUrl(imageUrl);
        }
        String targetUrl = looper.getTargetUrl();
        if (!StringUtils.isEmpty(targetUrl)) {
            looperFrom.setTargetUrl(targetUrl);
        }
        looperFrom.setUpdateTime(new Date());
        //跟新图片
        looperMapper.update(looperFrom, new UpdateWrapper<Looper>()
                .eq("id", looperId).eq("user_id", userId));
        return Result.ok().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage());
    }


    /**
     * 获取单个轮播图
     *
     * @param looperId
     * @return
     */
    @Override
    public Result getLooper(String looperId) {
        User user = userService.checkUser();
        if (StringUtils.isEmpty(looperId))
            return Result.error().code(ResultCode.CAROUSEL_IMAGE_URL_NULL.getCode())
                    .message(ResultCode.CAROUSEL_IMAGE_URL_NULL.getMessage());
        Looper looperFrom = looperMapper.selectById(looperId);
        if (looperFrom == null)
            return Result.error().code(ResultCode.CAROUSEL_IMAGE_EXCEED.getCode())
                    .message(ResultCode.CAROUSEL_IMAGE_EXCEED.getMessage());

        if (!looperFrom.getUserId().equals(user.getId()))
            return Result.error().code(ResultCode.NO_PERMISSION.getCode())
                    .message(ResultCode.NO_PERMISSION.getMessage());

        return Result.ok().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage())
                .data("looper", looperFrom);
    }

    /**
     * 获取轮播图列表
     *
     * @param page
     * @param size
     * @return
     */
    @Override
    public Result listLooper(int page, int size) {
        User user = userService.checkUser();
        //分页约束
        int checkPage = testUtils.checkPage(page);
        int checkSize = testUtils.checkSize(size);
        //获取用户列表（分页查询）
        Page page1 = new Page(checkPage - 1, checkSize);
        Page looperList = looperMapper.selectPage(page1, new QueryWrapper<Looper>()
                .eq("user_id", user.getId())
                .orderByDesc("create_time"));

        return Result.ok().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage())
                .data("looperList", looperList);
    }
}
