package com.pawn.blog.response;

import com.pawn.blog.utils.Constants;

/**
 * @Author: NieChangan
 * @Description: 返回码定义
 * 规定:
 * #200表示成功
 * #1001～1999 区间表示参数错误
 * #2001～2999 区间表示用户错误
 * #3001～3999 区间表示接口异常
 */
public enum ResultCode implements CustomizeResultCode {
    /* 成功 */
    SUCCESS(200, "成功"),

    /* 默认失败 */
    COMMON_FAIL(999, "失败"),

    /* 参数错误：1000～1999 */
    PARAM_NOT_VALID(1001, "参数无效"),
    PARAM_IS_BLANK(1002, "参数为空"),
    PARAM_TYPE_ERROR(1003, "参数类型错误"),
    PARAM_NOT_COMPLETE(1004, "参数缺失"),


    /*表单异常*/
    PARAM_NOT_NAME(1020, "用户名不能为空"),
    PARAM_NOT_PASSWORD(10021, "密码不能为空"),
    PARAM_NOT_ALREADY(1022, "用户名重复"),
    PARAM_NOT_MAILBOX_NULL(1023, "邮箱不能为空"),
    PARAM_NOT_MAILBOX_REPEAT(1024, "邮箱已被注册"),
    PARAM_NOT_MAILBOX_HAVENOT(1025, "邮箱未注册"),

    /* 用户状态异常 */
    USER_NOT_LOGIN(2001, "账号未登录"),
    USER_ACCOUNT_EXPIRED(2002, "账号已过期"),
    USER_CREDENTIALS_ERROR(2003, "密码错误"),
    USER_CREDENTIALS_EXPIRED(2004, "密码过期"),
    USER_ACCOUNT_DISABLE(2005, "账号不可用"),
    USER_ACCOUNT_LOCKED(2006, "账号已被锁定"),
    USER_ACCOUNT_NOT_EXIST(2007, "账号不存在"),
    USER_ACCOUNT_ALREADY_EXIST(2008, "账号已存在"),
    USER_ACCOUNT_USE_BY_OTHERS(2009, "账号下线"),
    USER_ACCOUNT_USE_BY_INITIALIZATION(2010, "账户已经初始化"),
    USER_ACCOUNT_USE_BY_EITHER(2011, "用户名或密码错误"),
    USER_ACCOUNT_USE_BY_NO(2012, "用户不存在"),

    /*请求异常*/
    ERROR_403(4003, "权限不足"),
    ERROR_404(4004, "页面丢失了"),
    ERROR_4000(4000, "数据请求异常"),
    ERROR_500(5000, "请求错误"),
    ERROR_504(5004, "系统繁忙！请稍后重试"),
    ERROR_505(5005, ""),

    /*验证码异常*/
    EMAIL_ACCOUNT_REPEATEDLY(6001, "验证码发送过于频繁"),
    EMAIL_ACCOUNT_FORMAT(6002, "邮箱格式错误"),
    EMAIL_ACCOUNT_SUCCESS(200, "验证码发送成功"),
    EMAIL_ACCOUNT_INVALID(6004, "验证码错误或过期"),
    EMAIL_ACCOUNT_MISTAKE(6005, "验证码错误"),

    /*文章分类异常*/
    CLASSIFICATION_NOT_EXIST(3001, "分类不为空"),
    PINGYIN_NOT_EXIST(3002, "分类拼音不为空"),
    DESCRIPTION_NOT_EXIST(3003, "分类描述不为空"),
    CLASSIFICATION_YES_EXIST(3004, "分类添加成功"),
    CLASSIFICATION_DOES_NOT_EXIST(3005, "分类不存在"),

    /*友情连接异常*/
    FRIENDSHIP_LINK_NOT_NAME(30011, "友情连接name不能为null"),
    FRIENDSHIP_LINK_NOT_LOG(30012, "友情连接log不能为null"),
    FRIENDSHIP_LINK_NOT_URL(30013, "友情连接url不能为null"),
    FRIENDSHIP_LINK_OK(200, "友情连接删除成功"),
    FRIENDSHIP_LINK_NULL(30015, "该友情连接不存在"),

    /*图片操作异常*/
    IMAGE_LINK_NOT_NULL(30021, "图片不能为空"),
    IMAGE_LINK_NOT_EXIST(30022, "文件格式错误"),
    IMAGE_LINK_NOT_SUPPORT(30023, "不支持此文件类型"),
    IMAGE_LINK_FAILURE(30024, "图片上传失败，请稍后重试"),

    /*文章异常*/
    ARTICLE_LINK_TITLE_NULL(30031, "文章标题不能为空"),
    ARTICLE_LINK_TITLE_EXCEED(3002, "文章标题不能超过" + Constants.Article.TITLE_MAX_LENGTH + "个字符"),
    ARTICLE_LINK_CONTENT_NULL(30033, "文章内容不能为空"),
    ARTICLE_LINK_TYPE_EXIST(30034, "未选择文章类型"),
    ARTICLE_LINK_SUMMARY_NULL(30035, "文章摘要不能为空"),
    ARTICLE_LINK_LABELS_NULL(30036, "文章标签不能为空"),
    ARTICLE_LINK_POSITION_EXIST(30037, "未选择文章须发布位置"),
    ARTICLE_LINK_POSITION_STATE(30038, "未选择文章未保存或发布"),
    ARTICLE_LINK_POSITION_OPERATING(30039, "格式错误操作"),
    ARTICLE_LINK_SUMMARY_EXCEED(30040, "文章摘要不能超过" + Constants.Article.SUMMARY_MAX_LENGTH + "个字符"),
    ARTICLE_LINK_OPERATING_DRAFT(30041, "已经发布的文章不支持存为草稿"),
    ARTICLE_LINK_OPERATING_NULL(30042, "该文章不存在"),
    ARTICLE_LINK_COMPETENCE(30043, "无权访问"),
    ARTICLE_LINK_Top(30044, "文章置顶成功"),
    ARTICLE_LINK_NOT_Top(30045, "取消置顶成功"),

    /*评论异常*/
    COMMENT_LINK_NULL(30061, "文章id不能为空"),
    COMMENT_LINK_EXCEED(30065, "文章异常，可能是非法操作"),
    COMMENT_LINK_CONTENT_NULL(30062, "评论类容不能为空"),
    COMMENT_LINK_TYPE(30065, "评论类型错误"),
    COMMENT_LINK_CONTENT_OK(30063, "评论成功"),
    COMMENT_LINK_CONTENT_NOT_EXIST(30064, "该评论不存在"),

    /*网站信息异常*/
    WEB_SIZE_INFO_TITLE(30071, "网站标题不能为空"),
    WEB_SIZE_INFO_TITLE_REPEAT(30072, "该网站标题已被使用"),
    WEB_SIZE_INFO_TITLE_EXCEED(30073, "网站标题修改异常"),
    WEB_SIZE_INFO_EXCEED(30074, "网站信息保存异常"),

    /*文件上传异常异常*/
    UPLOAD_PICTURE_NULL(30081, "上传文件不可以为空"),
    UPLOAD_PICTURE_EXCEED(30082, "上传文件过大"),
    UPLOAD_PICTURE_ID_NULL(30083, "图片id不能为空"),
    UPLOAD_PICTURE_IMAGE_NULL(30084, "图片不存在"),
    UPLOAD_PICTURE_DELETE_EXCEED(30085, "图片删除异常"),

    /*轮播图异常*/
    CAROUSEL_TITLE_NULL(30091, "轮播图标题不能为空"),
    CAROUSEL_TARGET_URL_NULL(30092, "轮播图目标地址不能为空"),
    CAROUSEL_IMAGE_URL_NULL(30093, "轮播图地址不能为空"),
    CAROUSEL_ADD_EXCEED(30094, "轮播图添加异常"),
    CAROUSEL_DELETE_EXCEED(30096, "轮播图删除异常"),
    CAROUSEL_IMAGE_EXCEED(30095, "该轮播图不存在"),

    /*相册管理异常*/
    PHOTO_TITLE_NULL(40011, "照片描述不能为空"),
    PHOTO_IMAGE_URL_NULL(40012, "照片地址不能为空"),
    PHOTO_ADD_EXCEED(40013, "照片添加异常"),
    PHOTO_DELETE_EXCEED(40014, "照片删除异常"),
    PHOTO_IMAGE_EXCEED(4005, "该照片不存在"),
    /*消息（公告）异常*/
    NEWS_CONTENT_NULL(40021, "内容不能为空"),
    NEWS_CONTENT_RELEASE_EXCEED(40022, "消息发送异常"),
    NEWS_NEWS_EXCEED(40023, "消息获取异常"),
    NEWS_RECEIVER_ID_EXCEED(40024, "消息发送人数据-id异常，可能存在被攻击风险"),
    NEWS_ANNOUNCEMENT_EXCEED(40025, "消息状态异常，无法辨别该消息类型"),

    /* 业务异常*/
    NO_PERMISSION(3001, "没有操作权限"),

    NO_PERMISSION_UPDATE(3002, "没有修改权限"),

    NO_PERMISSION_OPERATING(3003, "不支持该操作"),

    /*运行时异常*/
    ARITHMETIC_EXCEPTION(9001, "算数异常");

    private Integer code;

    private String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
