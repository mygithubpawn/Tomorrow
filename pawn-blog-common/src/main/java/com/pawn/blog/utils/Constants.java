package com.pawn.blog.utils;

/***
 * description: 管理员初始化常量
 * @author:美茹冠玉
 * @Return
 * @param
 * @date 2020/12/18 18:23
 */
public interface Constants {
    /**
     * 用户常数
     */
    interface User {
        String ARTICLE_CONTENT="# 博客介绍\n" +
                "1. Tomorrow诞生于2021年3月。\n" +
                "2. Tomorrow主打为个人博客，同时提供一个供各位道友交流的社区。\n" +
                "# 能为您带来什么\n" +
                "1. 为各位道友提供一个记录生活的平台，你可以发表你的技术论文，也可以记录您的生活趣事，或者当作笔记本使用。\n" +
                "2. 提高道友的文笔水平，在长期的文章积累中，愿道友也能有《[鹧鸪天](https://baike.baidu.com/item/%E9%B9%A7%E9%B8%AA%E5%A4%A9/1340238?fr=aladdin)》“忆昔彤庭望日华，匆匆枯笔梦生花。”的妙笔。\n" +
                "3. 为道友寻找到志同道合的江湖挚友。\n" +
                "# 功能介绍\n" +
                "### 文章管理\n" +
                "- 文章发表\n" +
                "- 文章的管理\n" +
                "- 回收站\n" +
                "### 运营管理\n" +
                "- 文章分类管理\n" +
                "- 轮播图管理\n" +
                "- 评论管理\n" +
                "- 相册管理\n" +
                "### 博客设置\n" +
                "- 自定义博客信息\n" +
                "##### 更多功能可前往博客后台体验。\n" +
                "# 注意事项\n" +
                "1. 请文明使用该网站，不可对他人文章进行肆意的贬低，等不道德行为，如辱骂等！\n" +
                "2. 文章的发布，不可发布带有色，赌，谣，恐，等...性质的文章。\n" +
                "3. 不可恶意攻击该网站。\n" +
                "4. 为了各位道友信息的安全，切勿将账号泄露给他人。\n" +
                "5. 望各位道友共同努力，一起提供一个文章且充满生机的江湖。\n" +
                "##### 一经发现以上行为，将禁止该账户对网站的使用。\n" +
                "# 使用技巧\n" +
                "- 博客风格可根据图片的风格统一，不建议使用不同风格的文章封面。建议风格统一。\n" +
                "- 博客风格，小清新，科技，喜庆，卡通。等...\n" +
                "- 轮播图建议使用矩形图片，为防止图片未充满屏幕，或压缩严重，导致图片美感流失。\n" +
                "# 小掌柜的祝语\n" +
                "- 非常感谢道友的入驻，希望道友能在这个非凡的江湖能有所收获，我们一起远航。";
        String ROLE_ADMIN = "role_admin";
        String ROLE_NORMAL = "role_normal";

        //用户初始化数据
        String DEFAULT_AVATAR = "http://zsh-pawn.oss-cn-shenzhen.aliyuncs.com/pawn-bok/5b5e82e0-98f9-4e6d-9800-da8f29070a96.png";
        String DEFAULT_STATE = "1";
        String DEFAULT_OCCUPATION = "江湖游侠";
        String DEFAULT_SIGN = "但行好事，莫问前程。";
        //redis的key
        String KEY_CAPTCHA_CONTENT = "key_captcha_content_";
        String KEY_EMAIL_SEND_IP = "key_email_send_ip_";
        String KEY_EMAIL_CODE_CONTENT = "key_email_code_content_";
        String KEY_EMAIL_SEND_ADDRESS = "key_email_send_address_";
        String KEY_TOKEN = "key-token-";
        String COOKIE_TOKEN_KEY = "pawn-token-reid-";

        //网站信息；
        String WEB_SIZE_INFO_IN = "的店铺";
    }

    interface Settings {
        String SETTINGS_ACCOUNT_INIT_STATE = "settings_account_innit_state";
    }

    /**
     * 分页
     */
    interface Page {
        int DEFAULT_PAGE = 1;
        int MAIN_SIZE = 2;
    }

    /**
     * 单位为秒
     */
    interface TimeValue {
        int MIN = 60;
        int HOUR = 60 * MIN;
        int HOUR_3 = HOUR * 3;
        int DAY = 24 * HOUR;
        int WEEK = 7 * DAY;
        int MONTH = 30 * DAY;
    }

    /**
     * 文章
     */
    interface Article {
        int TITLE_MAX_LENGTH = 128;
        int SUMMARY_MAX_LENGTH = 256;
        //1表示富文本，2表示markdown
        String TYPE_RICH_TEXT = "1";
        String TYPE_MARKDOWN = "2";
        //1表示已发布，2表示草稿，3表示置顶
        String STATE_PUBLISH = "1";
        String STATE_DRAFT = "2";
        String STATE_TOP = "3";
        //1表示文章待审核，2表示审核通过，3表示文章被打回
        String REVIEW_WAIT = "1";
        String REVIEW_THROUGH = "2";
        String REVIEW_RETURN = "3";
        //1表示发布到社区，2表示发布到本地博客
        String POSITION_COMMUNITY = "1";
        String POSITION_LOCAL = "2";

    }
}
