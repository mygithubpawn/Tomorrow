package com.pawn.blog.controller.protal;

import com.pawn.blog.response.Result;
import com.pawn.blog.service.impl.CategoriesServiceImpl;
import com.pawn.blog.service.impl.FriendsServiceImpl;
import com.pawn.blog.service.impl.LooperServiceImpl;
import com.pawn.blog.service.impl.WebInfoServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/***
 * description: 获取网站信息
 * @author:美茹冠玉
 * @Return
 * @param
 * @date 2020/12/18 15:58
 */
@RestController
@RequestMapping("/portal/web_size_info")
@Api(value = "门户基本信息管理", tags = "门户基本信息管理API")
public class WebSizeInfoPortalApi {

    /**
     * 友情连接
     */
    @Autowired
    FriendsServiceImpl friendsService;
    /**
     * 网站信息
     */
    @Autowired
    WebInfoServiceImpl webInfoService;
    /**
     * 轮播图
     */
    @Autowired
    LooperServiceImpl looperService;
    /**
     * 文章分类
     */
    @Autowired
    CategoriesServiceImpl categoriesService;

    /**
     * 获取分类
     *
     * @return
     */
    @GetMapping("/categories/{page}/{size}")
    @ApiOperation(value = "获取分类列表接口",
            notes = "门户获取分类列表")
    public Result getCategories(@PathVariable("page") int page, @PathVariable("size") int size) {
        return categoriesService.listCategories(page, size);
    }

    /**
     * 获取网站标题title
     *
     * @return
     */
    @GetMapping("/title")
    @ApiOperation(value = "获取网站标题接口",
            notes = "门户获取网站标题")
    public Result getWebSizeTitle() {
        return webInfoService.getWebSizeTitle();
    }

    /**
     * 获取view统计信息
     *
     * @return
     */
    @GetMapping("/view_count")
    @ApiOperation(value = "获取view统计信息接口",
            notes = "门户获取view统计信息")
    public Result getWebSizeViewCount() {
        return null;
    }

    /**
     * 获取seo信息
     *
     * @return
     */
    @GetMapping("/seo")
    @ApiOperation(value = "获取seo信息接口",
            notes = "门户获取seo信息")
    public Result getSeoInfo() {
        return webInfoService.getWebSizeTitle();
    }

    /**
     * 获取轮播图列表
     *
     * @return
     */
    @GetMapping("/loop/{page}/{size}")
    @ApiOperation(value = "获取轮播图列表接口",
            notes = "门户获取轮播图列表")
    public Result getLoops(@PathVariable("page") int page, @PathVariable("size") int size) {
        return looperService.listLooper(page, size);
    }

    /**
     * 获取友情连接列表
     *
     * @return
     */
    @GetMapping("/friend_links")
    @ApiOperation(value = "获取友情连接列表接口",
            notes = "门户获取友情连接列表")
    public Result getLinks() {
        return friendsService.listFriends();
    }


}
