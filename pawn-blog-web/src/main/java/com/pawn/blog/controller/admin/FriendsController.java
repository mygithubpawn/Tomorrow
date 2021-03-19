package com.pawn.blog.controller.admin;


import com.pawn.blog.entity.Friends;
import com.pawn.blog.entity.Looper;
import com.pawn.blog.response.Result;
import com.pawn.blog.service.impl.FriendsServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 友情连接控制器
 * </p>
 *
 * @author pawn
 * @since 2020-12-17
 */
@RestController
@RequestMapping("/friends")
@Api(value = "友情连接管理", tags = "友情连接管理Api")
public class FriendsController {

    @Autowired
    FriendsServiceImpl friendsService;

    /**
     * 添加友情连接
     *
     * @param friends
     * @return
     */
    @PreAuthorize("@permission.user()")
    @PostMapping
    @ApiOperation(value = "添加友情连接接口",
            notes = "需要管理员权限，添加友情连接")
    public Result addFriends(@RequestBody Friends friends) {
        return friendsService.addFriends(friends);
    }

    /**
     * 删除友情连接
     *
     * @param friendsId
     * @return
     */
    @PreAuthorize("@permission.user()")
    @DeleteMapping("/delete/{friendsId}")
    @ApiOperation(value = "友情连接删除接口",
            notes = "需要管理员权限，修改友情链接为未使用友情链接")
    public Result deleteFriends(@PathVariable("friendsId") String friendsId) {
        return friendsService.deleteFriends(friendsId);
    }

    /**
     * 修改友情连接
     *
     * @param friendsId
     * @return
     */
    @PreAuthorize("@permission.user()")
    @PutMapping("/update/{friendsId}")
    @ApiOperation(value = "修改友情连接接口",
            notes = "需要管理员权限，修改友情链接")
    public Result updateFriends(@PathVariable("friendsId") String friendsId, @RequestBody Friends friends) {
        return friendsService.updateFriends(friendsId, friends);
    }

    /**
     * 查询友情连接
     *
     * @param friendsId
     * @return
     */
    @PreAuthorize("@permission.user()")
    @GetMapping("/get/{friendsId}")
    @ApiOperation(value = "查询友情连接接口",
            notes = "需要管理员权限，查询单个友情连接")
    public Result getFriends(@PathVariable("friendsId") String friendsId) {
        return friendsService.getFriends(friendsId);
    }

    /**
     * 获取友情连接列表
     *
     * @return
     */
    @PreAuthorize("@permission.user()")
    @GetMapping("/list")
    @ApiOperation(value = "获取友情连接列表接口",
            notes = "需要管理员权限，获取友情连接列表")
    public Result listFriends() {
        return friendsService.listFriends();
    }

    /**
     *
     * @param articleId
     * @param sate
     * @return
     */
    @PreAuthorize("@permission.user()")
    @PutMapping("/sate/{articleId}/{sate}")
    public Result updateFriends(@PathVariable("articleId") String articleId, @PathVariable("sate") String sate) {
        return null;
    }

    /**
     * 获取用户友情链接总数接口
     *
     * @param userId
     * @return
     */
    @PreAuthorize("@permission.user()")
    @GetMapping("/get/total/{userId}")
    @ApiOperation(value = "获取用户友情链接总数接口",
            notes = "获取用户友情链接总数")
    public Result friendsTotal(@PathVariable("userId") String userId) {
        return friendsService.friendsTotal(userId);
    }
}

