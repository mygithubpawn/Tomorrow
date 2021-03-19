package com.pawn.blog.utils;

import com.google.gson.Gson;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * description:自定义工具类
 * @author:美茹冠玉
 * @Return
 * @param
 * @date 2020/12/19 15:26
 */
@Component
public class TestUtils {
    public static final String regEX = "^[0-9a-zA-Z]+([\\.\\-_]*[0-9a-zA-Z]+)*@([0-9a-zA-Z]+[\\-_]*[0-9a-zA-Z]+\\.)+[0-9a-zA-Z]{2,6}$";

    /**
     * 非空判断
     *
     * @param text
     * @return
     */
    public static boolean isEmpty(String text) {
        return text == null || text.length() == 0;
    }

    /**
     * 邮箱正则匹配工具类
     *
     * @param emailAddress
     * @return
     */
    public static Boolean isEmailAddressOk(String emailAddress) {
        Pattern p = Pattern.compile(regEX);
        Matcher m = p.matcher(emailAddress);
        return m.matches();
    }

    /**
     * 生成随机数
     *
     * @return
     */
    @Bean
    public Random random() {
        return new Random();
    }

    /**
     * Gson对象
     *
     * @return
     */
    public Gson gson() {
        return new Gson();
    }

    /**
     * 分页
     *
     * @param page
     * @return
     */
    public int checkPage(int page) {
        if (page < Constants.Page.DEFAULT_PAGE)
            page = Constants.Page.DEFAULT_PAGE;
        return page;
    }

    public int checkSize(int size) {
        if (size < Constants.Page.MAIN_SIZE)
            size = Constants.Page.MAIN_SIZE;
        return size;
    }
}
