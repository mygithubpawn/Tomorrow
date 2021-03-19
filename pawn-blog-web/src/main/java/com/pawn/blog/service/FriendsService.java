package com.pawn.blog.service;

import com.pawn.blog.entity.Friends;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pawn.blog.response.Result;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author pawn
 * @since 2020-12-17
 */
public interface FriendsService extends IService<Friends> {

    Result addFriends(Friends friends);

    Result listFriends();

    Result getFriends(String friendsId);

    Result deleteFriends(String friendsId);

    Result updateFriends(String friendsId, Friends friends);


    Result friendsTotal(String userId);
}
