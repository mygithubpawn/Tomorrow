package com.pawn.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pawn.blog.entity.Comment;
import com.pawn.blog.entity.Friends;
import com.pawn.blog.entity.User;
import com.pawn.blog.mapper.FriendsMapper;
import com.pawn.blog.response.Result;
import com.pawn.blog.response.ResultCode;
import com.pawn.blog.service.FriendsService;
import com.pawn.blog.utils.Constants;
import com.pawn.blog.utils.IdWorker;
import com.pawn.blog.utils.TestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 友情连接服务实现类
 * </p>
 *
 * @author pawn
 * @since 2020-12-17
 */
@Service
@Transactional
public class FriendsServiceImpl extends ServiceImpl<FriendsMapper, Friends> implements FriendsService {

    @Autowired
    IdWorker idWorker;
    @Resource
    FriendsMapper friendsMapper;
    @Autowired
    TestUtils testUtils;
    @Autowired
    UserServiceImpl userService;

    /**
     * 添加友情连接
     *
     * @param friends
     * @return
     */
    @Override
    public Result addFriends(Friends friends) {
        User user = userService.checkUser();
        //检查数据
        String name = friends.getName();
        if (StringUtils.isEmpty(name))
            return Result.error().code(ResultCode.FRIENDSHIP_LINK_NOT_NAME.getCode()).message(ResultCode.FRIENDSHIP_LINK_NOT_NAME.getMessage());
        String logo = friends.getLogo();
        if (StringUtils.isEmpty(logo))
            return Result.error().code(ResultCode.FRIENDSHIP_LINK_NOT_LOG.getCode()).message(ResultCode.FRIENDSHIP_LINK_NOT_LOG.getMessage());
        String url = friends.getUrl();
        if (StringUtils.isEmpty(url))
            return Result.error().code(ResultCode.FRIENDSHIP_LINK_NOT_URL.getCode()).message(ResultCode.FRIENDSHIP_LINK_NOT_URL.getMessage());
        //补全数据
        friends.setId(idWorker.nextId() + "")
                .setState("1")
                .setUserId(user.getId())
                .setCreateTime(new Date())
                .setUpdateTime(new Date());

        //保存数据
        int insert = friendsMapper.insert(friends);
        //返回结果
        return insert > 0 ? Result.ok().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage())
                : Result.ok().code(ResultCode.COMMON_FAIL.getCode()).message(ResultCode.COMMON_FAIL.getMessage());
    }

    /**
     * 获取友情链接列表
     *
     * @return
     */
    @Override
    public Result listFriends() {
        User user = userService.checkUser();
        //查询
        List<Friends> friendsList = friendsMapper.selectList(new QueryWrapper<Friends>().eq("user_id", user.getId()));
        //返回结果
        return Result.ok().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage())
                .data("friendsList", friendsList);
    }

    /**
     * 查询友情链接
     *
     * @param friendsId
     * @return
     */
    @Override
    public Result getFriends(String friendsId) {
        User user = userService.checkUser();
        Friends friends = friendsMapper.selectOne(new QueryWrapper<Friends>()
                .eq("id", friendsId).eq("user_id", user.getId()));
        return friends == null ? Result.error().code(ResultCode.FRIENDSHIP_LINK_NULL.getCode()).message(ResultCode.FRIENDSHIP_LINK_NULL.getMessage())
                : Result.ok().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage())
                .data("friends", friends);
    }

    /**
     * 删除友情链接
     *
     * @param friendsId
     * @return
     */
    @Override
    public Result deleteFriends(String friendsId) {
        int deleteById = friendsMapper.deleteById(friendsId);
        return deleteById > 0 ? Result.ok().code(ResultCode.FRIENDSHIP_LINK_OK.getCode()).message(ResultCode.FRIENDSHIP_LINK_OK.getMessage())
                : Result.error().code(ResultCode.FRIENDSHIP_LINK_NULL.getCode()).message(ResultCode.FRIENDSHIP_LINK_NULL.getMessage());
    }

    /**
     * 修改友情链接
     *
     * @param friendsId
     * @param friends
     * @return
     */
    @Override
    public Result updateFriends(String friendsId, Friends friends) {
        //确定修改数据
        Friends friendsFromDb = friendsMapper.selectById(friendsId);
        if (friendsFromDb == null)
            return Result.error().code(ResultCode.FRIENDSHIP_LINK_NULL.getCode()).message(ResultCode.FRIENDSHIP_LINK_NULL.getMessage());
        //字段校验
        String name = friends.getName();
        if (!StringUtils.isEmpty(name))
            friendsFromDb.setName(name);
        String logo = friends.getLogo();
        if (!StringUtils.isEmpty(logo))
            friendsFromDb.setLogo(logo);
        String url = friends.getUrl();
        if (!StringUtils.isEmpty(url))
            friendsFromDb.setUrl(url);

        String state = friends.getState();
        if (!StringUtils.isEmpty(state))
            friendsFromDb.setState(state);

        //修改数据
        int update = friendsMapper.update(friends, new UpdateWrapper<Friends>().eq("id", friendsId));
        //返回结果
        return update > 0 ? Result.ok().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage())
                : Result.error().code(ResultCode.COMMON_FAIL.getCode()).message(ResultCode.COMMON_FAIL.getMessage());
    }

    /**
     * 获取用户友情链接总数
     *
     * @param userId
     * @return
     */
    @Override
    public Result friendsTotal(String userId) {
        Integer friendsCount = friendsMapper.selectCount(new QueryWrapper<Friends>().select("id")
                .eq("user_id", userId).eq("state", "1"));
        return Result.ok().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage())
                .data("friendsCount", friendsCount);
    }
}
