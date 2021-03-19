package com.pawn.blog.controller.protal;

import com.pawn.blog.response.Result;
import com.pawn.blog.service.impl.CategoriesServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/***
 * description: 门户分类控制层
 * @author:美茹冠玉
 * @Return
 * @param
 * @date 2021/1/23 13:53
 */
@RestController
@RequestMapping("/portal/categories")
@Api(value = "门户（分类列表）", tags = "门户分类管理API")
public class CategoriesPostalApi {
    @Autowired
    CategoriesServiceImpl categoriesService;

    /**
     * 获取分类
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/gte/{categoryId}")
    @ApiOperation(value = "分类获取接口",
            notes = "需要管理员权限，获取单个分类")
    public Result getCategory(@PathVariable("categoryId") String categoryId) {
        return categoriesService.getCategory(categoryId);
    }

    /**
     * 获取分类列表
     *
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/list/{page}/{size}")
    @ApiOperation(value = "分类列表获取接口",
            notes = "获取分类列表")
    public Result listCategories(@PathVariable("page") int page, @PathVariable("size") int size) {
        return categoriesService.listCategories(page, size);
    }

    /**
     * 获取分类列表推荐
     *
     * @param size
     * @return
     */
    @GetMapping("/recommend/list/{size}")
    @ApiOperation(value = "分类推荐列表获取接口",
            notes = "获取分类推荐列表")
    public Result recommendCategories(@PathVariable("size") int size) {
        return categoriesService.recommendCategories(size);
    }

}
