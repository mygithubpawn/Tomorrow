package com.pawn.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pawn.blog.dao.ArticleUserDao;
import com.pawn.blog.dao.detailArticleVo;
import com.pawn.blog.entity.Article;
import com.pawn.blog.entity.Labels;
import com.pawn.blog.entity.User;
import com.pawn.blog.mapper.ArticleMapper;
import com.pawn.blog.mapper.CategoriesMapper;
import com.pawn.blog.mapper.LabelsMapper;
import com.pawn.blog.response.Result;
import com.pawn.blog.response.ResultCode;
import com.pawn.blog.service.ArticleService;
import com.pawn.blog.service.UserService;
import com.pawn.blog.utils.Constants;
import com.pawn.blog.utils.IdWorker;
import com.pawn.blog.utils.TestUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * <p>
 * 文章管理实现类
 * </p>
 *
 * @author pawn
 * @since 2020-12-17
 */
@Slf4j
@Service
@Transactional
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    @Resource
    ArticleMapper articleMapper;
    @Autowired
    UserService userService;
    @Autowired
    TaskService taskService;
    @Resource
    CategoriesMapper categoriesMapper;
    @Autowired
    IdWorker idWorker;
    @Autowired
    TestUtils testUtils;

    /**
     * 添加文章
     *
     * @param article
     * @return
     */
    @Override
    public Result addArticle(Article article) {
        //检查用户，获取到用户状态
        User user = userService.checkUser();
        //检查文章，并获取文章状态
        String status = article.getStatus();
        if (StringUtils.isEmpty(status))
            return Result.error().code(ResultCode.ARTICLE_LINK_POSITION_STATE.getCode())
                    .message(ResultCode.ARTICLE_LINK_POSITION_STATE.getMessage());
        if (!Constants.Article.STATE_DRAFT.equals(status) && !Constants.Article.STATE_PUBLISH.equals(status))
            return Result.error().code(ResultCode.ARTICLE_LINK_POSITION_OPERATING.getCode())
                    .message(ResultCode.ARTICLE_LINK_POSITION_OPERATING.getMessage());
        //用户发布需校验，草稿不需要校验
        if (Constants.Article.STATE_PUBLISH.equals(status)) {
            if (user == null)
                return Result.error().code(ResultCode.USER_NOT_LOGIN.getCode()).message(ResultCode.USER_NOT_LOGIN.getMessage());
            //检查数据（标题,分类id，内容，类型，摘要，标签。文章状态，发布位置，）
            String title = article.getTitle();
            if (StringUtils.isEmpty(title))
                return Result.error().code(ResultCode.ARTICLE_LINK_TITLE_NULL.getCode())
                        .message(ResultCode.ARTICLE_LINK_TITLE_NULL.getMessage());

            if (title.length() > Constants.Article.TITLE_MAX_LENGTH)
                return Result.error().code(ResultCode.ARTICLE_LINK_TITLE_EXCEED.getCode())
                        .message(ResultCode.ARTICLE_LINK_TITLE_EXCEED.getMessage());

            String content = article.getContent();
            if (StringUtils.isEmpty(content))
                return Result.error().code(ResultCode.ARTICLE_LINK_CONTENT_NULL.getCode())
                        .message(ResultCode.ARTICLE_LINK_CONTENT_NULL.getMessage());

            String type = article.getType();
            if (StringUtils.isEmpty(type))
                return Result.error().code(ResultCode.ARTICLE_LINK_TYPE_EXIST.getCode()).
                        message(ResultCode.ARTICLE_LINK_TYPE_EXIST.getMessage());
            if (!Constants.Article.TYPE_RICH_TEXT.equals(type) && !Constants.Article.TYPE_MARKDOWN.equals(type))
                return Result.error().code(ResultCode.ARTICLE_LINK_POSITION_OPERATING.getCode())
                        .message(ResultCode.ARTICLE_LINK_POSITION_OPERATING.getMessage());

            String summary = article.getSummary();
            if (StringUtils.isEmpty(summary))
                return Result.error().code(ResultCode.ARTICLE_LINK_SUMMARY_NULL.getCode())
                        .message(ResultCode.ARTICLE_LINK_SUMMARY_NULL.getMessage());

            if (summary.length() > Constants.Article.SUMMARY_MAX_LENGTH)
                return Result.error().code(ResultCode.ARTICLE_LINK_SUMMARY_EXCEED.getCode())
                        .message(ResultCode.ARTICLE_LINK_SUMMARY_EXCEED.getMessage());

            String labels = article.getLabels();
            if (StringUtils.isEmpty(labels))
                return Result.error().code(ResultCode.ARTICLE_LINK_LABELS_NULL.getCode())
                        .message(ResultCode.ARTICLE_LINK_LABELS_NULL.getMessage());

            String position = article.getPosition();
            if (StringUtils.isEmpty(position))
                return Result.error().code(ResultCode.ARTICLE_LINK_POSITION_EXIST.getCode())
                        .message(ResultCode.ARTICLE_LINK_POSITION_EXIST.getMessage());

            if (!Constants.Article.POSITION_COMMUNITY.equals(position) && !Constants.Article.POSITION_LOCAL.equals(position))
                return Result.error().code(ResultCode.ARTICLE_LINK_POSITION_OPERATING.getCode())
                        .message(ResultCode.ARTICLE_LINK_POSITION_OPERATING.getMessage());

            log.info("user_getId  ==>" + user.getId());
            log.info("getCategoryId  ==>" + article.getCategoryId());
        }
        //添加文章
        String articleId = article.getId();
        if (StringUtils.isEmpty(articleId)) {
            //补充数据
            article.setId(idWorker.nextId() + "")
                    .setCreateTime(new Date());
            log.info("article ==> " + article);

        } else {
            //保存草稿
            //检查文章状态
            Article selectFromDb = articleMapper.selectById(articleId);
            if (Constants.Article.STATE_PUBLISH.equals(selectFromDb.getState()) &&
                    Constants.Article.STATE_DRAFT.equals(status))
                //已经发布了，不能存为草搞，只能更新；
                return Result.error().code(ResultCode.ARTICLE_LINK_OPERATING_DRAFT.getCode()).message(ResultCode.ARTICLE_LINK_OPERATING_DRAFT.getMessage());
        }
        //补全数据
        article.setUpdateTime(new Date())
                .setUserId(user.getId());
        //添进数据库
        int insert = articleMapper.insert(article);

        //打散标签，入库统计
        this.setupLabels(article.getLabels(), user.getId());
        //TODO:如果前端需要做程序自动保存成草稿(比如说每30秒保存一次，就需要加上这个ID了，否则会创建多个Item)
        return insert > 0 ? Result.ok().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage())
                .data("selectById", article.getId())
                : Result.error().code(ResultCode.COMMON_FAIL.getCode()).message(ResultCode.COMMON_FAIL.getMessage());
    }

    @Resource
    LabelsMapper labelsMapper;

    /**
     * 标签入库
     *
     * @param labels
     */
    public void setupLabels(String labels, String userId) {
        List<String> labelsList = new ArrayList<>();
        if (labels.contains("-")) {
            labelsList.addAll(Arrays.asList(labels.split("-")));
        } else {
            labelsList.add(labels);
        }
        //入库，统计
        for (String Label : labelsList) {
            //如果已存在更新标签数量
            int name = labelsMapper.updateName(Label);
            if (name == 0) {
                Labels labels1 = new Labels()
                        .setId(idWorker.nextId() + "")
                        .setCount(1)
                        .setUserId(userId)
                        .setName(Label)
                        .setCreateTime(new Date())
                        .setUpdateTime(new Date());
                labelsMapper.insert(labels1);
            }

        }

    }

    /**
     * 获取文章列表
     *
     * @param page
     * @param size
     * @param status     文章状态 :1表示已发布，2表示草稿，3表示置顶
     * @param keyword    搜索关建子
     * @param position   发布的位置：1表示发布到社区，2表示发布到本地博客
     * @param categoryId 分类id
     * @return
     */
    @Override
    public Result listArticle(int page, int size, String status, String keyword, String position, String categoryId) {
        User user = userService.checkUser();
        String userId = user.getId();
        //处理page，size
        int checkPage = testUtils.checkPage(page);
        int checkSize = testUtils.checkSize(size);
        Page page1 = new Page(checkPage - 1, checkSize);
        //创建查询添加
        List<Page> articleList = new ArrayList<>();
        if (!StringUtils.isEmpty(status) || !StringUtils.isEmpty(position)
                || !StringUtils.isEmpty(keyword) || !StringUtils.isEmpty(categoryId)) {
            //开始查询，
            if (!StringUtils.isEmpty(status)) {
                Page selectMapsPage = articleMapper.selectMapsPage(page1, new QueryWrapper<Article>()
                        .select(Article.class, info -> !info.getColumn().equals("content"))
                        .eq("status", status)
                        .eq("user_id", userId)
                        .orderByDesc("create_time"));
                articleList.add(selectMapsPage);
            }
            if (!StringUtils.isEmpty(position)) {
                Page selectMapsPage = articleMapper.selectMapsPage(page1, new QueryWrapper<Article>()
                        //不查询content
                        .select(Article.class, info -> !info.getColumn().equals("content"))
                        .eq("position", position)
                        .ne("status", Constants.Article.STATE_DRAFT)
                        .eq("user_id", userId)
                        .orderByDesc("create_time"));
                articleList.add(selectMapsPage);
            }

            if (!StringUtils.isEmpty(keyword)) {
                Page selectMapsPage = articleMapper.selectMapsPage(page1, new QueryWrapper<Article>()
                        .select(Article.class, info -> !info.getColumn().equals("content"))
                        .like("title", keyword)
                        .eq("user_id", userId)
                        .ne("status", Constants.Article.STATE_DRAFT)
                        .orderByDesc("create_time"));
                articleList.add(selectMapsPage);
            }
            if (!StringUtils.isEmpty(categoryId)) {
                Page selectMapsPage = articleMapper.selectMapsPage(page1, new QueryWrapper<Article>()
                        .select(Article.class, info -> !info.getColumn().equals("content"))
                        .eq("category_id", categoryId)
                        .eq("user_id", userId)
                        .ne("status", Constants.Article.STATE_DRAFT)
                        .orderByDesc("create_time"));
                articleList.add(selectMapsPage);
            }
        } else {
            Page selectMapsPage = articleMapper.selectMapsPage(page1, new QueryWrapper<Article>()
                    .select(Article.class, info -> !info.getColumn().equals("content"))
                    .eq("user_id", userId)
                    .ne("status", Constants.Article.STATE_DRAFT)
                    .orderByDesc("create_time"));
            articleList.add(selectMapsPage);
        }

        //返回结果
        return Result.ok().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage())
                .data("articleList", articleList);
    }

    /**
     * 获取文章详情
     *
     * @param articleId
     * @return
     */
    @Override
    public Result getArticle(String articleId) {
        //查询文章
        detailArticleVo selectAerFrom = articleMapper.selectAerFrom(articleId);
        //调用该方法保证标签拆分被加载
        selectAerFrom.getLabels();

        if (selectAerFrom == null)
            return Result.error().code(ResultCode.ARTICLE_LINK_OPERATING_NULL.getCode())
                    .message(ResultCode.ARTICLE_LINK_OPERATING_NULL.getMessage());

        return Result.error().code(ResultCode.SUCCESS.getCode())
                .message(ResultCode.SUCCESS.getMessage())
                .data("selectAerFrom", selectAerFrom);
    }


    /**
     * 文章更新
     * <p>
     * 该接口支持修改类容：标题，标签，类容，分类，摘要
     *
     * @param articleId
     * @param article
     * @return
     */
    @Override
    public Result updateArticle(String articleId, Article article) {
        //获取文章
        Article selectFromDb = articleMapper.selectById(articleId);
        if (selectFromDb == null)
            return Result.error().code(ResultCode.ARTICLE_LINK_OPERATING_NULL.getCode())
                    .message(ResultCode.ARTICLE_LINK_OPERATING_NULL.getMessage());
        //校验权限
        User user = userService.checkUser();
        if (user == null)
            return Result.error().code(ResultCode.USER_NOT_LOGIN.getCode())
                    .message(ResultCode.USER_NOT_LOGIN.getMessage());
        if (!selectFromDb.getUserId().equals(user.getId())) {
            return Result.error().code(ResultCode.NO_PERMISSION_UPDATE.getCode())
                    .message(ResultCode.NO_PERMISSION_UPDATE.getMessage());
        }

        //数据校验
        String categoryId = article.getCategoryId();
        if (!StringUtils.isEmpty(categoryId)) {
            selectFromDb.setCategoryId(categoryId);
        }
        String title = article.getTitle();
        if (!StringUtils.isEmpty(title)) {
            selectFromDb.setTitle(title);
        }
        String position = article.getPosition();
        if (!StringUtils.isEmpty(position)) {
            selectFromDb.setPosition(position);
        }
        String review = article.getReview();
        if (!StringUtils.isEmpty(review)) {
            selectFromDb.setReview(review);
        }
        String status = article.getStatus();
        if (!StringUtils.isEmpty(status)) {
            selectFromDb.setStatus(status);
        }
        String content = article.getContent();
        if (!StringUtils.isEmpty(content)) {
            selectFromDb.setContent(content);
        }
        String summary = article.getSummary();
        if (!StringUtils.isEmpty(summary)) {
            selectFromDb.setSummary(summary);
        }
        String labels = article.getLabels();
        if (!StringUtils.isEmpty(labels)) {
            selectFromDb.setLabels(labels);
        }
        String cover = article.getCover();
        if (!StringUtils.isEmpty(cover)) {
            selectFromDb.setCover(cover);
        }
        selectFromDb.setUpdateTime(new Date());
        //更新
        articleMapper.update(selectFromDb, new UpdateWrapper<Article>().eq("id", articleId));

        return Result.ok().code(ResultCode.SUCCESS.getCode())
                .message(ResultCode.SUCCESS.getMessage());
    }

    /**
     * 文章删除（物理删出）
     *
     * @param articleId
     * @return
     */
    @Override
    public Result deleteArticle(String articleId) {
        int deleteArticle = articleMapper.deleteArticle(articleId);

        return deleteArticle > 0 ? Result.ok().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage())
                : Result.error().code(ResultCode.ARTICLE_LINK_OPERATING_NULL.getCode()).message(ResultCode.ARTICLE_LINK_OPERATING_NULL.getMessage());

    }

    /**
     * 删除文章，逻辑删除
     *
     * @param articleId
     * @return
     */
    @Override
    public Result deleteUpdateArticleSate(String articleId) {
        int deleteById = articleMapper.deleteById(articleId);
        return deleteById > 0 ? Result.ok().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage())
                : Result.error().code(ResultCode.ARTICLE_LINK_OPERATING_NULL.getCode()).message(ResultCode.ARTICLE_LINK_OPERATING_NULL.getMessage());
    }

    /**
     * 文章置顶
     * 只支持本地
     *
     * @param articleId
     * @return
     */
    @Override
    public Result topArticleSate(Article article, String articleId) {
        User user = userService.checkUser();
        if (user == null)
            return Result.error().code(ResultCode.USER_NOT_LOGIN.getCode())
                    .message(ResultCode.USER_NOT_LOGIN.getMessage());
        //获取文章
        Article selectFromDb = articleMapper.selectById(articleId);
        if (selectFromDb == null)
            return Result.error().code(ResultCode.ARTICLE_LINK_OPERATING_NULL.getCode())
                    .message(ResultCode.ARTICLE_LINK_OPERATING_NULL.getMessage());

        //文章置顶
        if (selectFromDb.getStatus().equals(Constants.Article.STATE_PUBLISH)) {
            selectFromDb.setStatus(Constants.Article.STATE_TOP);
            articleMapper.update(selectFromDb, new UpdateWrapper<Article>().eq("id", articleId));
            return Result.ok().code(ResultCode.ARTICLE_LINK_Top.getCode())
                    .message(ResultCode.ARTICLE_LINK_Top.getMessage());
        }
        //取消置顶
        if (selectFromDb.getStatus().equals(Constants.Article.STATE_TOP)) {
            selectFromDb.setStatus(Constants.Article.STATE_PUBLISH);
            articleMapper.update(selectFromDb, new UpdateWrapper<Article>().eq("id", articleId));
            return Result.ok().code(ResultCode.ARTICLE_LINK_NOT_Top.getCode())
                    .message(ResultCode.ARTICLE_LINK_NOT_Top.getMessage());
        }
        return Result.error().code(ResultCode.NO_PERMISSION_OPERATING.getCode())
                .message(ResultCode.NO_PERMISSION_OPERATING.getMessage());
    }

    @Autowired
    Random random;

    /**
     * 获取推荐文章（门户）
     *
     * @param articleId
     * @param size
     * @return
     */
    @Override
    public Result gitRecommendSate(String articleId, int size) {

        //获取标签
        Article article = articleMapper.selectOne(new QueryWrapper<Article>().select("labels", "position").eq("id", articleId));
        String labels = article.getLabels();
        //打散标签
        List<String> labelsList = new ArrayList();
        if (!labels.contains("-")) {
            labelsList.add(labels);
        } else {
            labelsList.addAll(Arrays.asList(labels.split("-")));
        }
        //获取随机标签
        String tarLabels = labelsList.get(random.nextInt(labelsList.size()));
        log.info("tarLabels == >" + tarLabels);
        int checkSize = testUtils.checkSize(size);
        Page page1 = new Page(0, checkSize);
        Page selectPage = articleMapper.selectPage(page1, new QueryWrapper<Article>()
                .like("labels", tarLabels).ne("id", articleId)
                .ne("status", Constants.Article.STATE_DRAFT));

        //文章不够填充处理
        if (selectPage.getSize() < size) {
            int dxSize = (int) (size - selectPage.getSize());
            Page page2 = new Page(0, dxSize);
            Page TimePage = articleMapper.selectPage(page2, new QueryWrapper<Article>()
                    .orderByDesc("create_time").ne("id", articleId)
                    .ne("status", Constants.Article.STATE_DRAFT));
            log.info("pageList == >", TimePage);
            List<Page> peList = Arrays.asList(TimePage);
            selectPage.addOrder(peList);
        }
        return Result.ok().code(ResultCode.SUCCESS.getCode())
                .message(ResultCode.SUCCESS.getMessage()).data("selectPage", selectPage);
    }

    /**
     * ·获取标签云
     *
     * @param size
     * @return
     */
    @Override
    public Result gitLabels(String userId, int size) {
        User user = userService.checkUser();
        //处理page，size
        int checkSize = testUtils.checkSize(size);
        Page page1 = new Page(0, checkSize);
        Page LabelPage = labelsMapper.selectPage(page1, new QueryWrapper<Labels>()
                .eq("user_id", userId)
                .orderByDesc("count"));
        return Result.ok().code(ResultCode.SUCCESS.getCode())
                .message(ResultCode.SUCCESS.getMessage()).data("LabelPage", LabelPage);
    }


    /**
     * 根据分类查询文章
     *
     * @param page
     * @param size
     * @return
     */
    @Override
    public Result listArticleLabels(String labels, int page, int size) {
        //处理page，size
        int checkPage = testUtils.checkPage(page);
        int checkSize = testUtils.checkSize(size);
//        log.info("labels ==>" + labels);
        Page page1 = new Page(checkPage - 1, checkSize);

        Page selectMapsPage = articleMapper.selectMapsPage(page1, new QueryWrapper<Article>()
                .select(Article.class, info -> !info.getColumn().equals("content"))
                .like("labels", labels)
                .ne("status", Constants.Article.STATE_DRAFT)
                .orderByDesc("create_time"));

        return Result.ok().code(ResultCode.SUCCESS.getCode())
                .message(ResultCode.SUCCESS.getMessage()).data("selectMapsPage", selectMapsPage);
    }


    /**
     * 博客首页文章推荐接口
     *
     * @param size
     * @return
     */
    @Override
    public Result getRecommendMend(int size) {
        User user = userService.checkUser();
        int checkSize = testUtils.checkSize(size);
        Page page1 = new Page(0, checkSize);
        Page selectMapsRecommend = articleMapper.selectMapsPage(page1, new QueryWrapper<Article>()
                .ne("status", Constants.Article.STATE_DRAFT)
                .eq("user_id", user.getId())
                .orderByDesc("view_count"));
        return Result.ok().code(ResultCode.SUCCESS.getCode())
                .message(ResultCode.SUCCESS.getMessage()).data("selectMapsRecommend", selectMapsRecommend);
    }

    /**
     * 博客首页文章推荐(社区文章)
     *
     * @param size
     * @return
     */
    @Override
    public Result getRecommendCommunity(int size) {
        Page<ArticleUserDao> page = new Page<>(1, size);
        IPage<ArticleUserDao> productPage = articleMapper.getProductPage(page);
        return Result.ok().code(ResultCode.SUCCESS.getCode())
                .message(ResultCode.SUCCESS.getMessage()).data("selectMapCommunity", productPage.getRecords());
    }


    /**
     * 社区获取获取文章
     *
     * @param page
     * @param size
     * @return
     */
    @Override
    public Result CommListArticle(int page, int size) {
        Page<ArticleUserDao> pageList = new Page<>(page, size);
        IPage<ArticleUserDao> productPageList = articleMapper.getProductPage(pageList);
        return Result.ok().code(ResultCode.SUCCESS.getCode())
                .message(ResultCode.SUCCESS.getMessage()).data("productPageList", productPageList);
    }

    /**
     * 文章审核
     *
     * @param article
     * @param articleId
     * @return
     */
    @Override
    public Result reviewArticle(Article article, String articleId) {
        if (StringUtils.isEmpty(articleId)) {
            return Result.error().code(ResultCode.ARTICLE_LINK_OPERATING_NULL.getCode())
                    .message(ResultCode.ARTICLE_LINK_OPERATING_NULL.getMessage());
        }
        Article selectFromDb = articleMapper.selectById(articleId);
        String review = article.getReview();
        if (!StringUtils.isEmpty(review)) {
            selectFromDb.setReview(review);
        }
        //更新
        articleMapper.update(selectFromDb, new UpdateWrapper<Article>().eq("id", articleId));

        return Result.ok().code(ResultCode.SUCCESS.getCode())
                .message(ResultCode.SUCCESS.getMessage());
    }

    /**
     * 获取待审核文章
     *
     * @return
     */
    @Override
    public Result reviewArticleList(int page, int size) {
        Page<ArticleUserDao> pageList = new Page<>(page, size);
        IPage<ArticleUserDao> reviewList = articleMapper.reviewArticleList(pageList);
        return Result.ok().code(ResultCode.SUCCESS.getCode())
                .message(ResultCode.SUCCESS.getMessage()).data("reviewList", reviewList);
    }

    /**
     * 获取单单篇文章接口（后台）
     *
     * @param articleId
     * @return
     */
    @Override
    public Result getArticleMin(String articleId) {
        //查询文章
        Article article = articleMapper.selectById(articleId);
        //调用该方法保证标签拆分被加载
        article.getLabels();

        if (article == null)
            return Result.error().code(ResultCode.ARTICLE_LINK_OPERATING_NULL.getCode())
                    .message(ResultCode.ARTICLE_LINK_OPERATING_NULL.getMessage());

        return Result.error().code(ResultCode.SUCCESS.getCode())
                .message(ResultCode.SUCCESS.getMessage())
                .data("selectAerFrom", article);
    }

    /**
     * 获取审核未通过文章
     *
     * @param page
     * @param size
     * @return
     */
    @Override
    public Result reviewArticleListDid(int page, int size) {
        int checkPage = testUtils.checkPage(page);
        int checkSize = testUtils.checkSize(size);
        Page page1 = new Page(checkPage - 1, checkSize);

        Page selectMapsPage = articleMapper.selectMapsPage(page1, new QueryWrapper<Article>()
                .select(Article.class, info -> !info.getColumn().equals("content"))
                .eq("review", Constants.Article.REVIEW_RETURN)
                .orderByDesc("create_time"));
        return Result.ok().code(ResultCode.SUCCESS.getCode())
                .message(ResultCode.SUCCESS.getMessage()).data("reviewArticle", selectMapsPage);
    }

    /**
     * 通过分类获取文章列表接口（社区接口）
     *
     * @param capacity
     * @param page
     * @param size
     * @return
     */
    @Override
    public Result capacityArticle(String capacity, int page, int size) {
        Page<ArticleUserDao> pageList = new Page<>(page, size);
        IPage<ArticleUserDao> capacityArticle = articleMapper.capacityPage(pageList, capacity);
        return Result.ok().code(ResultCode.SUCCESS.getCode())
                .message(ResultCode.SUCCESS.getMessage()).data("capacityArticle", capacityArticle);
    }

    /**
     * 通过标题获取文章列表接口
     *
     * @param title
     * @param page
     * @param size
     * @return
     */
    @Override
    public Result capacityTitleArticle(String title, int page, int size) {
        Page<ArticleUserDao> pageList = new Page<>(page, size);
        IPage<ArticleUserDao> capacityTitleArticle = articleMapper.searchTitlePage(pageList, title);
        return Result.ok().code(ResultCode.SUCCESS.getCode())
                .message(ResultCode.SUCCESS.getMessage()).data("capacityTitleArticle", capacityTitleArticle);
    }

    /**
     * 邮箱修改邮件发送
     *
     * @param emailAddress
     * @param name
     * @param request
     * @return
     */
    @Override
    public Result emailUpdate(String emailAddress, String name,
                              HttpServletRequest request) {
        taskService.emailArticle(emailAddress, name);
        return Result.ok().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage());
    }

    /**
     * 密码修改邮箱提示
     *
     * @param emailAddress
     * @param name
     * @param request
     * @return
     */
    @Override
    public Result passwordUpdate(String emailAddress, String name,
                                  HttpServletRequest request) {
        taskService.emailArticleVia(emailAddress, name);
        return Result.ok().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage());
    }

    /**
     * 获取用户文章总数
     *
     * @param userId
     * @return
     */
    @Override
    public Result articleTotal(String userId) {
        Integer articleCount = articleMapper.selectCount(new QueryWrapper<Article>().select("id")
                .eq("user_id", userId).eq("state", "1"));
        return Result.ok().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage())
                .data("articleCount", articleCount);
    }

    /**
     * 获取社区文章总数
     *
     * @return
     */
    @Override
    public Result articleTotalPor() {
        Integer articleCountPor = articleMapper.selectCount(new QueryWrapper<Article>().select("id")
                .eq("review", Constants.Article.REVIEW_THROUGH)
                .eq("position", Constants.Article.POSITION_COMMUNITY)
                .eq("state", "1")
                .ne("status", Constants.Article.STATE_DRAFT));
        return Result.ok().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage())
                .data("articleCountPor", articleCountPor);
    }

    /**
     * 获取数据库所有文章总数
     * content
     * news_name
     * news_aver
     * news_id
     * receiver_id
     * stare
     *
     * @return
     */
    @Override
    public Result articleTotalPar() {
        Integer articleCountPar = articleMapper.selectCount(new QueryWrapper<Article>().select("id")
                .eq("review", Constants.Article.REVIEW_THROUGH)
                .eq("state", "1")
                .ne("status", Constants.Article.STATE_DRAFT));
        return Result.ok().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage())
                .data("articleCountPar", articleCountPar);
    }

    /**
     * 增加文章浏览量
     *
     * @param articleId
     * @return
     */
    @Override
    public Result addViewCount(String articleId) {
        articleMapper.addViewCount(articleId);
        return Result.ok().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage());
    }

    /**
     * 获取回收站文章列表
     *
     * @param userId
     * @param page
     * @param size
     * @return
     */
    @Override
    public Result articleRecycleBin(String userId, int page, int size) {
        Page<Article> pageList = new Page<>(page, size);
        IPage<Article> articleRecycleList = articleMapper.articleRecycleBin(pageList, userId);
        return Result.ok().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage())
                .data("articleRecycleList", articleRecycleList);
    }

    /**
     * 文章恢复
     *
     * @param articleId
     * @return
     */
    @Override
    public Result ArticleRecovery(String articleId) {
        articleMapper.ArticleRecovery(articleId);
        return Result.ok().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage());

    }
}
