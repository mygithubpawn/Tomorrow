package com.pawn.blog.service;

import com.pawn.blog.entity.Categories;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pawn.blog.response.Result;

/**
 * <p>
 * 分类服务类
 * </p>
 *
 * @author pawn
 * @since 2020-12-17
 */
public interface CategoriesService extends IService<Categories> {

    Result addCategory(Categories categories);

    Result getCategory(String categoryId);

    Result listCategories(int page, int size);


    Result updateCategory(String categoryId, Categories categories);


    Result deleteCategory(String categoryId);


    Result recommendCategories(int size);

    Result categoriesTotal(String userId);
}
