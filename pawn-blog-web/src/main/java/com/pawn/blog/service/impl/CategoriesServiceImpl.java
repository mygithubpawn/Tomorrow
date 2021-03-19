package com.pawn.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pawn.blog.entity.Article;
import com.pawn.blog.entity.Categories;
import com.pawn.blog.entity.User;
import com.pawn.blog.mapper.ArticleMapper;
import com.pawn.blog.mapper.CategoriesMapper;
import com.pawn.blog.response.Result;
import com.pawn.blog.response.ResultCode;
import com.pawn.blog.service.CategoriesService;
import com.pawn.blog.utils.IdWorker;
import com.pawn.blog.utils.TestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * <p>
 * 文章分类实现类
 * </p>
 *
 * @author pawn
 * @since 2020-12-17
 */
@Service
@Transactional
public class CategoriesServiceImpl extends ServiceImpl<CategoriesMapper, Categories> implements CategoriesService {
    @Resource
    CategoriesMapper categoriesMapper;
    @Autowired
    IdWorker idWorker;
    @Autowired
    UserServiceImpl userService;
    @Autowired
    TestUtils testUtils;
    @Resource
    ArticleMapper articleMapper;

    /**
     * 添加文章分类
     *
     * @param categories
     * @return
     */
    @Override
    public Result addCategory(Categories categories) {
        User user = userService.checkUser();
        //检查数据
        //必须填写的数据（分类名称，分类的pingyin，顺序，描述）
        if (StringUtils.isEmpty(categories.getName()))
            return Result.error().code(ResultCode.CLASSIFICATION_NOT_EXIST.getCode()).message(ResultCode.CLASSIFICATION_NOT_EXIST.getMessage());
        if (StringUtils.isEmpty(categories.getPinyin()))
            return Result.error().code(ResultCode.PINGYIN_NOT_EXIST.getCode()).message(ResultCode.PINGYIN_NOT_EXIST.getMessage());
        if (StringUtils.isEmpty(categories.getDescription()))
            return Result.error().code(ResultCode.DESCRIPTION_NOT_EXIST.getCode()).message(ResultCode.DESCRIPTION_NOT_EXIST.getMessage());

        //补全数据
        categories.setId(idWorker.nextId() + "")
                .setUserId(user.getId())
                .setOrder(1)
                .setCreateTime(new Date())
                .setUpdateTime(new Date());
        //保数据
        categoriesMapper.insert(categories);
        //返回结果
        return Result.ok().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage());
    }

    /**
     * 获取分类
     *
     * @param categoryId
     * @return
     */
    @Override
    public Result getCategory(String categoryId) {
        User user = userService.checkUser();

        Categories categories = categoriesMapper.selectOne(new QueryWrapper<Categories>()
                .eq("id", categoryId).eq("user_id", user.getId()));
        return categories == null ? Result.error().code(ResultCode.COMMON_FAIL.getCode()).message(ResultCode.COMMON_FAIL.getMessage())
                : Result.ok().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage())
                .data("categories", categories);
    }

    /**
     * 获取分类列表
     *
     * @param page
     * @param size
     * @return
     */
    @Override
    public Result listCategories(int page, int size) {
        User user = userService.checkUser();
        //参数检查
        //分页约束
        int checkPage = testUtils.checkPage(page);
        int checkSize = testUtils.checkSize(size);

        //获取用户列表（分页查询）
        Page page1 = new Page(checkPage - 1, checkSize);
        //创建条件
        //查询
        Page categoriesList = categoriesMapper.selectPage(page1,
                new QueryWrapper<Categories>().orderByDesc("create_time").eq("user_id", user.getId()));
        //返回结果
        return Result.ok().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage())
                .data("categoriesList", categoriesList);
    }


    /**
     * 分类修改
     *
     * @param categoryId
     * @param categories
     * @return
     */
    @Override
    public Result updateCategory(String categoryId, Categories categories) {
        //确定修改数据
        Categories categoriesFromDb = categoriesMapper.selectById(categoryId);
        if (categoriesFromDb == null)
            return Result.error().code(ResultCode.CLASSIFICATION_DOES_NOT_EXIST.getCode()).message(ResultCode.CLASSIFICATION_DOES_NOT_EXIST.getMessage());
        //字段校验
        String name = categories.getName();
        if (!StringUtils.isEmpty(name))
            categoriesFromDb.setName(name);
        String description = categories.getDescription();
        if (!StringUtils.isEmpty(description))
            categoriesFromDb.setDescription(description);
        String pinyin = categories.getPinyin();
        if (!StringUtils.isEmpty(pinyin))
            categoriesFromDb.setPinyin(pinyin);
        String status = categories.getStatus();
        if (!StringUtils.isEmpty(status))
            categoriesFromDb.setStatus(status);
        //补全数据
        categories.setUpdateTime(new Date());

        //修改数据
        int update = categoriesMapper.update(categories, new UpdateWrapper<Categories>().eq("id", categoryId));
        //返回结果
        return update > 0 ? Result.ok().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage())
                : Result.error().code(ResultCode.COMMON_FAIL.getCode()).message(ResultCode.COMMON_FAIL.getMessage());
    }

    /**
     * 分类删除
     *
     * @param categoryId
     * @return
     */
    @Override
    public Result deleteCategory(String categoryId) {
        int deleteById = categoriesMapper.deleteById(categoryId);
        return deleteById > 0 ? Result.ok().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage())
                : Result.error().code(ResultCode.CLASSIFICATION_DOES_NOT_EXIST.getCode()).message(ResultCode.CLASSIFICATION_DOES_NOT_EXIST.getMessage());
    }

    /**
     * 获取分类推荐列表
     *
     * @param size
     * @return
     */
    @Override
    public Result recommendCategories(int size) {
        int checkSize = testUtils.checkSize(size);
        Page page1 = new Page(0, checkSize);
        Page recommendName = categoriesMapper.selectPage(page1, new QueryWrapper<Categories>()
                .select("name")
                .orderByDesc("create_time"));

        return Result.ok().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage())
                .data("recommendName",recommendName);
    }

    /**
     * 获取用户分类总数
     *
     * @param userId
     * @return
     */
    @Override
    public Result categoriesTotal(String userId) {
        Integer categoriesCount = categoriesMapper.selectCount(new QueryWrapper<Categories>().select("id").eq("user_id", userId));
        return Result.ok().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage())
                .data("categoriesCount",categoriesCount);
    }
}
