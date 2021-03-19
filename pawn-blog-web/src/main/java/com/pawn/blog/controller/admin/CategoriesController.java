package com.pawn.blog.controller.admin;


import com.pawn.blog.entity.Categories;
import com.pawn.blog.response.Result;
import com.pawn.blog.service.impl.CategoriesServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 文章分类控制器
 * </p>
 *
 * @author pawn
 * @since 2020-12-17
 */
@RestController
@RequestMapping("/categories")
@Api(value = "分类管理", tags = "分类管理Api")
public class CategoriesController {
    @Autowired
    CategoriesServiceImpl categoriesService;

    /**
     * 添加分类
     * <p>
     * 需要管理员权限
     *
     * @param categories
     * @return
     */
    @PreAuthorize("@permission.user()")
    @PostMapping
    @ApiOperation(value = "添加分类接口",
            notes = "需要管理员权限，添加分类")
    public Result addCategory(@RequestBody Categories categories) {
        return categoriesService.addCategory(categories);
    }

    /**
     * 删除分类
     *
     * @param categoryId
     * @return
     */

    @PreAuthorize("@permission.user()")
    @DeleteMapping("/delete/{categoryId}")
    @ApiOperation(value = "分类删除接口",
            notes = "需要管理员权限，修改分类为不可用分类")
    public Result deleteCategory(@PathVariable("categoryId") String categoryId) {
        return categoriesService.deleteCategory(categoryId);
    }
    /**
     * 获取用户分类总数接口
     *
     * @param userId
     * @return
     */
    @PreAuthorize("@permission.user()")
    @GetMapping("/get/total/{userId}")
    @ApiOperation(value = "获取用户分类总数接口",
            notes = "获取用户分类总数")
    public Result categoriesTotal(@PathVariable("userId") String userId) {
        return categoriesService.categoriesTotal(userId);
    }

    /**
     * 修改分类
     *
     * @param categoryId
     * @param categories
     * @return
     */
    @PreAuthorize("@permission.user()")
    @PutMapping("/update/{categoryId}")
    @ApiOperation(value = "分类修改接口",
            notes = "需要管理员权限，修改分类")
    public Result updateCategory(@PathVariable("categoryId") String categoryId, @RequestBody Categories categories) {
        return categoriesService.updateCategory(categoryId, categories);
    }

    /**
     * 获取分类
     *
     * @param categoryId
     * @return
     */
    @PreAuthorize("@permission.user()")
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
    @PreAuthorize("@permission.user()")
    @GetMapping("/list/{page}/{size}")
    @ApiOperation(value = "分类列表获取接口",
            notes = "需要管理员权限，获取分类列表")
    public Result listCategories(@PathVariable("page") int page, @PathVariable("size") int size) {
        return categoriesService.listCategories(page, size);
    }


}

