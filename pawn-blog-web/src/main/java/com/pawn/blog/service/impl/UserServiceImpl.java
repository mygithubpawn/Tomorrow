package com.pawn.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.pawn.blog.ClaimUtils.ClaimsUtils;
import com.pawn.blog.entity.RefreshToken;
import com.pawn.blog.entity.Settings;
import com.pawn.blog.entity.User;
import com.pawn.blog.mapper.RefreshTokenMapper;
import com.pawn.blog.mapper.SettingsMapper;
import com.pawn.blog.mapper.UserMapper;
import com.pawn.blog.response.Result;
import com.pawn.blog.response.ResultCode;
import com.pawn.blog.service.UserService;
import com.pawn.blog.utils.*;
import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.GifCaptcha;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author pawn
 * @since 2020-12-17
 */
@Slf4j
@Service
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private IdWorker idWorker;
    @Resource
    private SettingsMapper settingsMapper;
    @Resource
    RefreshTokenMapper refreshTokenMapper;
    @Resource
    private UserMapper userMapper;
    @Autowired
    private WebInfoServiceImpl webInfoService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Resource
    private Random random;
    @Autowired
    private RedisUtil redisUtil;
    @Resource
    JavaMailSenderImpl mailSender;
    @Autowired
    TaskService taskService;
    @Autowired
    JWTUtil jwtUtil;
    @Autowired
    CookieUtils cookieUtils;
    @Autowired
    Gson gson;
    @Autowired
    TestUtils testUtils;

    /**
     * 邮件验证码发送
     * 1，注册(register)
     * 2，找回密码(forget)
     * 3，更换邮箱(update)
     *
     * @param emailAddress
     * @return
     */
    @Override
    public Result senEmail(String type, String emailAddress, HttpServletRequest request) {
        if (StringUtils.isEmpty(emailAddress))
            return Result.error().code(ResultCode.PARAM_NOT_MAILBOX_NULL.getCode()).message(ResultCode.PARAM_NOT_MAILBOX_NULL.getMessage());
        //判断用户操作状态
        if ("register".equals(type) || "update".equals(type)) {
            User selectOneEmail = userMapper.selectOne(new QueryWrapper<User>().select("email").eq("email", emailAddress));
            if (selectOneEmail != null)
                return Result.error().code(ResultCode.PARAM_NOT_MAILBOX_REPEAT.getCode()).message(ResultCode.PARAM_NOT_MAILBOX_REPEAT.getMessage());
        }
        if ("forget".equals(type)) {
            User selectOneEmail = userMapper.selectOne(new QueryWrapper<User>().select("email").eq("email", emailAddress));
            if (selectOneEmail == null)
                return Result.error().code(ResultCode.PARAM_NOT_MAILBOX_HAVENOT.getCode()).message(ResultCode.PARAM_NOT_MAILBOX_HAVENOT.getMessage());
        }
        //1、防止暴力发送，就是不断发地送:同个邮箱， 间隔要超过30秒发一次，同一个Ip, 1小时内 最多只能发10次(如果是短信， 你最多只能发5次)
        String remoteAddr = request.getRemoteAddr();
        //替换冒号
        if (remoteAddr != null) {
            remoteAddr = remoteAddr.replace(":", "_");
            log.info("sendEmail == > ip == >" + remoteAddr);
        }
        //判断验证码频繁发送
        Integer ipSendTim;
        String ipSendTimeValue = (String) redisUtil.get(Constants.User.KEY_EMAIL_SEND_IP + remoteAddr);
        if (ipSendTimeValue != null) {
            ipSendTim = Integer.parseInt(ipSendTimeValue);
        } else {
            ipSendTim = 1;
        }
        log.info("ipSendTim ==>" + ipSendTim);
        if (ipSendTim > 10)
            return Result.error().code(ResultCode.EMAIL_ACCOUNT_REPEATEDLY.getCode()).message(ResultCode.EMAIL_ACCOUNT_REPEATEDLY.getMessage());
        Object hasEmailSend = redisUtil.get(Constants.User.KEY_EMAIL_SEND_ADDRESS + emailAddress);
        //如果验证码存在不进行重复发送
        if (hasEmailSend != null)
            return Result.error().code(ResultCode.EMAIL_ACCOUNT_REPEATEDLY.getCode()).message(ResultCode.EMAIL_ACCOUNT_REPEATEDLY.getMessage());
        //2、检查邮箱地址是否正确
        if (!(TestUtils.isEmailAddressOk(emailAddress)))
            return Result.error().code(ResultCode.EMAIL_ACCOUNT_FORMAT.getCode()).message(ResultCode.EMAIL_ACCOUNT_FORMAT.getMessage());
        //3、发送验证码,6位数100000~999999
        int code = random.nextInt(999999);
        if (code < 100000) {
            code += 100000;
        }
        log.info("sedEmail == > code == > " + code);
        //通过异步任务发送邮件
        taskService.sendEmailVerifyCode(String.valueOf(code), emailAddress);
        //4，
        if (ipSendTim == null) {//记录验证码状态,
            ipSendTim = 0;
        }
        ipSendTim++;
        redisUtil.set(Constants.User.KEY_EMAIL_SEND_IP + remoteAddr, String.valueOf(ipSendTim), 60 * 60);
        redisUtil.set(Constants.User.KEY_EMAIL_SEND_ADDRESS + emailAddress, "true", 30);
        //保存code=>1o分钟有效
        redisUtil.set(Constants.User.KEY_EMAIL_CODE_CONTENT + emailAddress, String.valueOf(code), 60 * 10);
        return Result.ok().code(ResultCode.EMAIL_ACCOUNT_SUCCESS.getCode())
                .message(ResultCode.EMAIL_ACCOUNT_SUCCESS.getMessage())
                .data("sedEmail", code);
    }

    /**
     * 用户初始化
     *
     * @param user
     * @param request
     * @return
     */
    @Override
    public Result initManagerAccount(User user, HttpServletRequest request) {
        //检查是否有初始化
        Settings settingsMapperOneByKey = settingsMapper.findOneByKey(Constants.Settings.SETTINGS_ACCOUNT_INIT_STATE);
        if (settingsMapperOneByKey != null)
            return Result.error().code(ResultCode.USER_ACCOUNT_USE_BY_INITIALIZATION.getCode()).message(ResultCode.USER_ACCOUNT_USE_BY_INITIALIZATION.getMessage());
        //检查数据
        user.setId(String.valueOf(idWorker.nextId()));
        if (StringUtils.isEmpty(user.getUserName()))
            return Result.error().code(ResultCode.PARAM_IS_BLANK.getCode()).message(ResultCode.PARAM_IS_BLANK.getMessage());
        if (StringUtils.isEmpty(user.getPassword()))
            return Result.error().code(ResultCode.PARAM_NOT_PASSWORD.getCode()).message(ResultCode.PARAM_NOT_PASSWORD.getMessage());
        if (StringUtils.isEmpty(user.getEmail()))
            return Result.error().code(ResultCode.PARAM_NOT_MAILBOX_NULL.getCode()).message(ResultCode.PARAM_NOT_MAILBOX_NULL.getMessage());
        //补充数据
        String localAddr = request.getLocalAddr();
        String remoteAddr = request.getRemoteAddr();
        user.setRoles(Constants.User.ROLE_ADMIN)
                .setAvatar(Constants.User.DEFAULT_AVATAR)
                .setState(Constants.User.DEFAULT_STATE)
                .setLoginIp(localAddr)
                .setRegIp(remoteAddr)
                .setCreateTime(new Date())
                .setUpdateTime(new Date());
        //对密码进行加密
        String encodePassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        //添加到数据库
        userMapper.insert(user);
        //修改标记
        Settings settings = new Settings().setId(idWorker.nextId() + "")
                .setCreateTime(new Date())
                .setUpdateTime(new Date())
                .setKey(Constants.Settings.SETTINGS_ACCOUNT_INIT_STATE)
                .setValue("1");
        settingsMapper.insert(settings);
        return Result.ok().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage());
    }


    public static final int[] captcha_font_types = {
            Captcha.FONT_1,
            Captcha.FONT_2,
            Captcha.FONT_3,
            Captcha.FONT_4,
            Captcha.FONT_5,
            Captcha.FONT_6,
            Captcha.FONT_7,
            Captcha.FONT_8,
            Captcha.FONT_9,
            Captcha.FONT_10};

    /**
     * 图灵验证生成
     *
     * @param response
     * @param captchakey
     * @throws IOException
     * @throws FontFormatException
     */
    @Override
    public void createCaptcha(HttpServletResponse response, String captchakey) throws
            IOException, FontFormatException {
        if (StringUtils.isEmpty(captchakey) || captchakey.length() < 13) return;
        long key = 01;
        try {
            key = Long.parseLong(captchakey);
        } catch (Exception e) {
            return;
        }
        //开始使用
        // 设置请求头为输出图片类型
        response.setContentType("image/gif");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        Captcha gifCaptcha = null;
        int captchaType = random.nextInt(3);
        if (captchaType == 0) {
            // 三个参数分别为宽、高、位数
            gifCaptcha = new SpecCaptcha(120, 40, 5);
        } else if (captchaType == 1) {
            // gif类型
            gifCaptcha = new GifCaptcha(120, 40);
        } else {
            // 算术类型
            gifCaptcha = new ArithmeticCaptcha(120, 40);
            gifCaptcha.setLen(2);  // 几位数运算，默认是两位
        }

        String content = gifCaptcha.text().toLowerCase();//结果
        log.info(" captcha content  ==> " + content);

        int index = captcha_font_types.length;
        log.info("captcha_font_types ==> " + index);
        //随机生成验证码
        gifCaptcha.setFont(random.nextInt(index));
        gifCaptcha.setCharType(Captcha.TYPE_DEFAULT);
        //保存到redis里
        //十分钟有效
        //用完删除
        redisUtil.set(Constants.User.KEY_CAPTCHA_CONTENT + key, content, 60 * 10);
        // 输出图片流
        gifCaptcha.out(response.getOutputStream());
    }


    /**
     * 用户注册
     *
     * @param user        name用户名，password密码，邮箱
     * @param emailCode   邮箱验证码
     * @param captchaCode 图灵验证码
     * @param captchakey  图灵验证码key
     * @param request
     * @return
     */
    @Override
    public Result register(User user,
                           String emailCode,
                           String captchaCode,
                           String captchakey,
                           HttpServletRequest request) {
        log.info("邮箱验证码：" + emailCode);
        log.info("图灵验证码" + captchaCode);
        log.info("用户名" + user.getUserName());
        //第一一步:检查当前用户名是否已经注册
        String userName = user.getUserName();
        if (StringUtils.isEmpty(userName))
            return Result.error().code(ResultCode.PARAM_NOT_NAME.getCode()).message(ResultCode.PARAM_NOT_NAME.getMessage());
        User selectOneUserName = userMapper.selectOne(new QueryWrapper<User>().select("user_name").eq("user_name", userName));
        if (selectOneUserName != null)
            return Result.error().code(ResultCode.PARAM_NOT_ALREADY.getCode()).message(ResultCode.PARAM_NOT_ALREADY.getMessage());
        //第二步:检查邮箱格式是否正确
        String email = user.getEmail();
        if (StringUtils.isEmpty(email))
            return Result.error().code(ResultCode.PARAM_NOT_MAILBOX_NULL.getCode()).message(ResultCode.PARAM_NOT_MAILBOX_NULL.getMessage());
        if (!(TestUtils.isEmailAddressOk(email)))
            return Result.error().code(ResultCode.EMAIL_ACCOUNT_FORMAT.getCode()).message(ResultCode.EMAIL_ACCOUNT_FORMAT.getMessage());
        //第三步:检查该邮箱是否已经注册
        User selectOneEmail = userMapper.selectOne(new QueryWrapper<User>().select("email").eq("email", email));
        if (selectOneEmail != null)
            return Result.error().code(ResultCode.PARAM_NOT_MAILBOX_REPEAT.getCode()).message(ResultCode.PARAM_NOT_MAILBOX_REPEAT.getMessage());
        //第四步:检查邮箱验证码是否正确
        String emailVerifyCode = (String) redisUtil.get(Constants.User.KEY_EMAIL_CODE_CONTENT + email);
        if (StringUtils.isEmpty(emailVerifyCode))
            return Result.error().code(ResultCode.EMAIL_ACCOUNT_INVALID.getCode()).message(ResultCode.EMAIL_ACCOUNT_INVALID.getMessage());
        if (!emailCode.equals(emailCode)) {
            return Result.error().code(ResultCode.EMAIL_ACCOUNT_MISTAKE.getCode()).message(ResultCode.EMAIL_ACCOUNT_MISTAKE.getMessage());
        } else {
            //干掉redis里的验证码
            redisUtil.del(Constants.User.KEY_EMAIL_CODE_CONTENT + email);
        }
        //第五步:检查图灵验证码是否正确
        String captchaVerifyCode = (String) redisUtil.get(Constants.User.KEY_CAPTCHA_CONTENT + captchakey);
        if (StringUtils.isEmpty(captchaVerifyCode))
            return Result.error().code(ResultCode.EMAIL_ACCOUNT_INVALID.getCode()).message(ResultCode.EMAIL_ACCOUNT_INVALID.getMessage());
        if (!captchaVerifyCode.equals(captchaCode)) {
            return Result.error().code(ResultCode.EMAIL_ACCOUNT_MISTAKE.getCode()).message(ResultCode.EMAIL_ACCOUNT_MISTAKE.getMessage());
        } else {
            //干掉redis里的验证码
            redisUtil.del(Constants.User.KEY_CAPTCHA_CONTENT + captchakey);
        }
        //达到可以注册的条件
        //第六步:对密码进行加密
        String password = user.getPassword();
        if (StringUtils.isEmpty(password))
            return Result.error().code(ResultCode.PARAM_NOT_PASSWORD.getCode()).message(ResultCode.PARAM_NOT_PASSWORD.getMessage());
        String encodePassword = bCryptPasswordEncoder.encode(password);
        user.setPassword(encodePassword);

        //第七步:补全数据
        //包括:注册IP，登录IP,角色，头像,创建时间,更新时间
        String remoteAddr = request.getRemoteAddr();
        log.info("setRegIp====>" + remoteAddr);
        user.setRoles(Constants.User.ROLE_NORMAL)
                .setAvatar(Constants.User.DEFAULT_AVATAR)
                .setState(Constants.User.DEFAULT_STATE)
                .setOccupation(Constants.User.DEFAULT_OCCUPATION)
                .setSign(Constants.User.DEFAULT_SIGN)
                .setLoginIp(remoteAddr)
                .setRegIp(remoteAddr)
                .setState("1")
                .setId(idWorker.nextId() + "")
                .setCreateTime(new Date())
                .setUpdateTime(new Date());
        //第八步:保存到数据库中
        int insert = userMapper.insert(user);
        log.info("成功" + insert);
        //初始化网站信息
        webInfoService.initializationWebSize(user.getId(), user.getUserName());
        //第九步:返回结果
        return Result.ok().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage());

    }

    /**
     * 用户登陆
     *
     * @param captcha
     * @param captchaKey
     * @param user
     * @param response
     * @return
     */
    @Override
    public Result doLogin(String captcha,
                          String captchaKey,
                          User user,
                          HttpServletResponse response
    ) {
        //判断验证码
        String captchaValue = (String) redisUtil.get(Constants.User.KEY_CAPTCHA_CONTENT + captchaKey);
        if (!captcha.equals(captchaValue))
            return Result.error().code(ResultCode.EMAIL_ACCOUNT_MISTAKE.getCode()).message(ResultCode.EMAIL_ACCOUNT_MISTAKE.getMessage());
        //验证通过删除redis里的验证码
        redisUtil.del(Constants.User.KEY_CAPTCHA_CONTENT + captchaKey);
        String userName = user.getUserName();
        if (StringUtils.isEmpty(userName))
            return Result.error().code(ResultCode.PARAM_NOT_NAME.getCode()).message(ResultCode.PARAM_NOT_NAME.getMessage());
        String password = user.getPassword();
        if (StringUtils.isEmpty(password))
            return Result.error().code(ResultCode.PARAM_NOT_PASSWORD.getCode()).message(ResultCode.PARAM_NOT_PASSWORD.getMessage());

        //可能是用户名，也可能是邮箱
        User userFromDb = userMapper.selectOne(new QueryWrapper<User>().eq("user_name", userName));
        log.info("userFromDb == >" + userFromDb);
        if (userFromDb == null)
            //如果用户名不存在，查找邮箱
            userFromDb = userMapper.selectOne(new QueryWrapper<User>().eq("email", userName));
        if (userFromDb == null)
            return Result.error().code(ResultCode.USER_ACCOUNT_NOT_EXIST.getCode()).message(ResultCode.USER_ACCOUNT_NOT_EXIST.getMessage());
        log.info("password == >" + password);
        log.info("userFromDb_password == >" + userFromDb.getPassword());
        //用户存在,对比密码
        boolean matches = bCryptPasswordEncoder.matches(password, userFromDb.getPassword());
        log.info("matches == >" + matches);
        if (!matches) {
            return Result.error().code(ResultCode.USER_CREDENTIALS_ERROR.getCode()).message(ResultCode.USER_CREDENTIALS_ERROR.getMessage());
        }
        //验证通过,判断用户状态
        if (!"1".equals(userFromDb.getState())) {
            return Result.error().code(ResultCode.USER_ACCOUNT_LOCKED.getCode()).message(ResultCode.USER_ACCOUNT_LOCKED.getMessage());
        }
        //抽离的方法(写入数据库，和redis)
        createToken(response, userFromDb);

        return Result.ok().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage());
    }

    /**
     * 抽离出来的方法
     * <p>
     * 返回tokenKey
     *
     * @param response
     * @param userFromDb
     */
    private String createToken(HttpServletResponse response, User userFromDb) {
        int deleteById = refreshTokenMapper.deleteByUserId(userFromDb.getId());
        log.info("createToken deleteById ...== >" + deleteById);
        // 生成token(通过封装工具类实现)
        Map<String, Object> claims = ClaimsUtils.user2Claims(userFromDb);
        // token默认有效三小时
        String token = jwtUtil.createJwt(claims);
        log.info("token == >" + token);
        //返回token的md5值,token会保存到redis里
        //前端访问的时候，携带token的md5key, 从redis中获取即可
        String tokenKey = DigestUtils.md5DigestAsHex(token.getBytes());
        log.info("createToken _ tokenKey ==> " + tokenKey);
        //保存token到redis，有效期为三小时
        redisUtil.set(Constants.User.KEY_TOKEN + tokenKey, token, Constants.TimeValue.HOUR_3 * 1000);
        //把tokenKey写入cookies里,这个通过动态获取，可以从request里获取（通过工具类实现）
        cookieUtils.setupCookie(response, Constants.User.COOKIE_TOKEN_KEY, tokenKey);
        //生成refreshToken有效期为60天
        String refreshTokenValue = jwtUtil.createJwt(userFromDb.getId());
        log.info("createToken _ refreshTokenValue ==> " + refreshTokenValue);
        //保存到数据库
        RefreshToken refreshToken = new RefreshToken()
                .setId(idWorker.nextId() + "")
                .setRefreshToken(refreshTokenValue)
                .setUserId(userFromDb.getId())
                .setTokenKey(tokenKey)
                .setCreateTime(new Date())
                .setUpdateTime(new Date());
        refreshTokenMapper.insert(refreshToken);
        return tokenKey;
    }

    /**
     * 校验是否为当前用户操作
     *
     * @param userById
     * @return
     */
    @Override
    public Boolean calibrationUser(String userById) {
        User user = this.checkUser();
        log.info("user" + user.getId());
        log.info("user_wbe_id" + userById);
        if (!userById.equals(user.getId())) {
            return false;
        }
        return true;
    }

    /**
     * 判断用户状态
     *
     * @param
     */
    @Override
    public User checkUser() {
        //拿到token
        String tokenKey = cookieUtils.getCookie(getRequest(), Constants.User.COOKIE_TOKEN_KEY);
        log.info("checkUser_tokenKey == >" + tokenKey);
        User user = parseByTokenKey(tokenKey);
        RefreshToken refreshToken;
        if (user == null) {
            //解析出错，说明过期
            //1，去mysql查,如果存在解析refreshToken
            refreshToken = refreshTokenMapper.selectOne(new QueryWrapper<RefreshToken>().eq("token_key", tokenKey));
            //2，如果不存在，即用户未登录
            if (refreshToken == null) {
                return null;
            }
            try {
                //3，如果存在，解析refreshToken，
                jwtUtil.parseJwt(refreshToken.getRefreshToken());
                //4，如果refreshToken有效，创建新的token，和新的refreshToken；
                String userId = refreshToken.getUserId();
                log.info("checkUser userId ==> " + userId);
                User userFromDb = userMapper.selectOne(new QueryWrapper<User>().eq("id", userId));
                log.info("userFromDb ==> " + userFromDb);
                //重新写入
                String newTokenKey = createToken(getResponse(), userFromDb);
                //返回token
                return parseByTokenKey(newTokenKey);
            } catch (Exception ex) {
                //5，如果refreshToken过期，提示用户登陆
                return null;
            }
        }
        return user;
    }


    /**
     * 获取用户信息
     *
     * @param usesId
     * @return
     */
    @Override
    public Result getUserInfo(String usesId) {
//        calibrationUser(usesId);
        //重从数据库里查询
        User user = userMapper.selectById(usesId);
        //判断用户权限
        //判断结果
        if (user == null)
            return Result.error().code(ResultCode.USER_ACCOUNT_USE_BY_NO.getCode()).message(ResultCode.USER_ACCOUNT_USE_BY_NO.getMessage());
        //复制结果，清除密码，Email，登陆id，注册id；
        String UserJson = gson.toJson(user);
        User fromJsonUser = gson.fromJson(UserJson, User.class);
        fromJsonUser.setPassword("");
        fromJsonUser.setEmail("");
        fromJsonUser.setLoginIp("");
        //返回结果
        return Result.ok().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage()).data("fromJsonUser", fromJsonUser);
    }

    /**
     * 检查邮箱是否已经注册
     *
     * @param email
     * @return
     */
    @Override
    public Result checkEmail(String email) {
        User userEmail = userMapper.selectOne(new QueryWrapper<User>().select("email").eq("email", email));
        return userEmail == null ? Result.error().code(ResultCode.PARAM_NOT_MAILBOX_HAVENOT.getCode()).message(ResultCode.PARAM_NOT_MAILBOX_HAVENOT.getMessage())
                : Result.ok().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage());
    }

    /**
     * 检查用户名的合法性
     *
     * @param userName
     * @return
     */
    @Override
    public Result checkUserName(String userName) {
        User checkUserName = userMapper.selectOne(new QueryWrapper<User>().select("user_name").eq("user_name", userName));
        return checkUserName == null ? Result.ok().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage())
                : Result.error().code(ResultCode.USER_ACCOUNT_NOT_EXIST.getCode()).message(ResultCode.USER_ACCOUNT_NOT_EXIST.getMessage());
    }

    /**
     * 修改用户信息
     *
     * @param userId
     * @param user
     * @return
     */
    @Override
    public Result updateUserInfo(String userId, User user) {
        //从token中解析出来的user，为校验权限
        User userTokenKey = this.checkUser();
        //获取当前用户的信息
        User userFrom = userMapper.selectById(userTokenKey.getId());
        if (!userFrom.getId().equals(userTokenKey.getId()))
            return Result.error().code(ResultCode.ERROR_403.getCode()).message(ResultCode.ERROR_403.getMessage());

        //判断用户id是否一致
        if (!userFrom.getId().equals(userId)) {
            return Result.error().code(ResultCode.NO_PERMISSION_UPDATE.getCode()).message(ResultCode.NO_PERMISSION_UPDATE.getMessage());
        }
        //可修改项
        //用户名
        String userName = user.getUserName();
        if (!StringUtils.isEmpty(userName) && !userName.equals(userFrom.getUserName())) {
            //检查是否已经存在
            User selectUserName = userMapper.selectOne(new QueryWrapper<User>().select("user_name").eq("user_name", userName));
            if (selectUserName != null)
                return Result.error().code(ResultCode.PARAM_NOT_ALREADY.getCode()).message(ResultCode.PARAM_NOT_ALREADY.getMessage());
            userFrom.setUserName(userName);
        }
        //头像
        if (!StringUtils.isEmpty(user.getAvatar()))
            userFrom.setAvatar(user.getAvatar());
        //简介，可以为空
        userFrom.setSign(user.getSign());
        userFrom.setOccupation(user.getOccupation());

        //干掉redis里的token, 下一次请求，需要解析token的，就会根据refreshToken重新创建一个。
        String toToken = cookieUtils.getCookie(getRequest(), Constants.User.COOKIE_TOKEN_KEY);
        redisUtil.del(Constants.User.KEY_TOKEN + toToken);
        //修改数据
        userMapper.update(userFrom, new UpdateWrapper<User>().eq("id", userId));
        return Result.ok().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage());
    }

    /**
     * 用户删除
     * 并不是真的删除，而是修改状态
     *
     * @param userId
     * @return
     */
    @Override
    public Result deleteUserById(String userId) {
        //修改用户状态
        int deleteById = userMapper.deleteById(userId);
        log.info("deleteUserById_ deleteById ==> " + deleteById);

        return deleteById > 0 ? Result.ok().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage())
                : Result.error().code(ResultCode.USER_ACCOUNT_USE_BY_NO.getCode()).message(ResultCode.USER_ACCOUNT_USE_BY_NO.getMessage());
    }

    /**
     * 获取用户列表
     * 需要管理员权限
     *
     * @param page
     * @param size
     * @return
     */
    @Override
    public Result listUsers(int page, int size, String userName, String email) {
        //分页约束
        int checkPage = testUtils.checkPage(page);
        int checkSize = testUtils.checkSize(size);
        //获取用户列表（分页查询）
        Page page1 = new Page(checkPage - 1, checkSize);

        List<Page> userList = new ArrayList<>();
        if (!StringUtils.isEmpty(userName) || !StringUtils.isEmpty(email)) {
            if (!StringUtils.isEmpty(userName)) {
                Page create_time = userMapper.selectMapsPage(page1, new QueryWrapper<User>()
                        //不查询password
                        .select(User.class, info -> !info.getColumn().equals("password"))
                        .like("user_name", userName)
                        .orderByDesc("create_time"));
                userList.add(create_time);
            }

            if (!StringUtils.isEmpty(email)) {
                Page create_time = userMapper.selectMapsPage(page1, new QueryWrapper<User>()
                        //不查询password
                        .select(User.class, info -> !info.getColumn().equals("password"))
                        .eq("email", email)
                        .orderByDesc("create_time"));
                userList.add(create_time);
            }
        } else {
            Page create_time = userMapper.selectMapsPage(page1, new QueryWrapper<User>()
                    //不查询password
                    .select(User.class, info -> !info.getColumn().equals("password"))
                    .orderByDesc("create_time"));
            userList.add(create_time);
        }

        return Result.ok().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage())
                .data("user_time", userList);

    }

    /**
     * 修改密码
     *
     * @param verifyCode
     * @param user
     * @return
     */
    @Override
    public Result updateUserPassword(String verifyCode, User user) {
        //检查邮箱
        String email = user.getEmail();
        if (StringUtils.isEmpty(email))
            return Result.error().code(ResultCode.PARAM_NOT_MAILBOX_NULL.getCode()).message(ResultCode.PARAM_NOT_MAILBOX_NULL.getMessage());
        //2、检查邮箱格式是否正确
        if (!(TestUtils.isEmailAddressOk(email)))
            return Result.error().code(ResultCode.EMAIL_ACCOUNT_FORMAT.getCode()).message(ResultCode.EMAIL_ACCOUNT_FORMAT.getMessage());
        //验证邮箱验证码（去redis里拿验证）
        String redisVerifyCode = (String) redisUtil.get(Constants.User.KEY_EMAIL_CODE_CONTENT + email);
        log.info("redisVerifyCode ==> " + redisVerifyCode);
        if (redisVerifyCode == null || !redisVerifyCode.equals(verifyCode))
            return Result.error().code(ResultCode.EMAIL_ACCOUNT_INVALID.getCode()).message(ResultCode.EMAIL_ACCOUNT_INVALID.getMessage());
        //干掉redis里对应的资源
        redisUtil.del(Constants.User.KEY_EMAIL_CODE_CONTENT + email);
        //对密码进行加密
        String encodePassword = bCryptPasswordEncoder.encode(user.getPassword());
        //修改密码
        int UpdatePassword = userMapper.update(null, new UpdateWrapper<User>()
                .eq("email", user.getEmail()).set("password", encodePassword));
        return UpdatePassword > 0 ? Result.error().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage()) :
                Result.error().code(ResultCode.COMMON_FAIL.getCode()).message(ResultCode.COMMON_FAIL.getMessage());
    }

    /**
     * 更换邮箱
     *
     * @param email
     * @param verifyCode
     * @return
     */

    @Override
    public Result updateUserEmail(String email, String verifyCode) {
        //确定以登陆
        User user = this.checkUser();
        if (user == null)
            return Result.error().code(ResultCode.USER_NOT_LOGIN.getCode()).message(ResultCode.USER_NOT_LOGIN.getMessage());
        //对比验证码
        String redisVerifyCode = (String) redisUtil.get(Constants.User.KEY_EMAIL_CODE_CONTENT + email);
        if (StringUtils.isEmpty(redisVerifyCode) || !redisVerifyCode.equals(verifyCode))
            return Result.error().code(ResultCode.EMAIL_ACCOUNT_INVALID.getCode()).message(ResultCode.EMAIL_ACCOUNT_INVALID.getMessage());

        //校验通过，删除redis里的验证码
        redisUtil.del(Constants.User.KEY_EMAIL_CODE_CONTENT + email);
        //可以修改邮箱
        int UpdateEmail = userMapper.update(null, new UpdateWrapper<User>()
                .eq("id", user.getId()).set("email", email));
        return UpdateEmail > 0 ? Result.error().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage()) :
                Result.error().code(ResultCode.COMMON_FAIL.getCode()).message(ResultCode.COMMON_FAIL.getMessage());
    }

    /**
     * 退出登陆
     *
     * @return
     */
    @Override
    public Result doLogout() {
        //拿到token_key
        String tokenKey = cookieUtils.getCookie(getRequest(), Constants.User.COOKIE_TOKEN_KEY);
        if (StringUtils.isEmpty(tokenKey))
            return Result.error().code(ResultCode.USER_NOT_LOGIN.getCode()).message(ResultCode.USER_NOT_LOGIN.getMessage());
        //删除redis里面的token；
        redisUtil.del(Constants.User.KEY_TOKEN + tokenKey);
        //删除mysql里的refreshToken
        Map deleteMap = new HashMap();
        deleteMap.put("token_key", tokenKey);
        int deleteByMap = refreshTokenMapper.deleteByMap(deleteMap);
        //删除cookie里面的token
        cookieUtils.deleteCookie(getResponse(), Constants.User.COOKIE_TOKEN_KEY);
        return deleteByMap > 0 ? Result.error().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage()) :
                Result.error().code(ResultCode.COMMON_FAIL.getCode()).message(ResultCode.COMMON_FAIL.getMessage());
    }

    /**
     * 通过token拿到用户信息
     *
     * @return
     */
    @Override
    public Result parseToken() {
        User user = this.checkUser();
        if (user == null)
            return Result.error().code(ResultCode.USER_NOT_LOGIN.getCode())
                    .message(ResultCode.USER_NOT_LOGIN.getMessage());
        return Result.ok().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage())
                .data("userFromToken", user);
    }

    /**
     * 重置密码
     *
     * @param userId
     * @param password
     * @return
     */
    @Override
    public Result resetPassword(String userId, String password) {
        //校验用户
        User user = userMapper.selectById(userId);
        if (user == null)
            return Result.error().code(ResultCode.USER_ACCOUNT_NOT_EXIST.getCode()).message(ResultCode.USER_ACCOUNT_NOT_EXIST.getMessage());
        //密码加密
        user.setPassword(bCryptPasswordEncoder.encode(password));
        //处理结果
        userMapper.update(user, new UpdateWrapper<User>().eq("id", userId));
        return Result.ok().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage());
    }

    /**
     * 用户列表（已停用用户）
     *
     * @param page
     * @param size
     * @return
     */
    @Override
    public Result adMinListUsers(int page, int size) {
        Page<User> pageList = new Page<>(page, size);
        IPage<User> adminUserList = userMapper.listUser(pageList);
        return Result.ok().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage())
                .data("adminUserList", adminUserList);
    }

    /**
     * 取消用户禁用接口
     *
     * @param userId
     * @param userState
     * @return
     */
    @Override
    public Result enableUser(String userId, String userState) {
        if (StringUtils.isEmpty(userId)) {
            return Result.error().code(ResultCode.USER_ACCOUNT_USE_BY_NO.getCode())
                    .message(ResultCode.USER_ACCOUNT_USE_BY_NO.getMessage());
        }
        if (StringUtils.isEmpty(userState)) {
            return Result.error().code(ResultCode.PARAM_NOT_COMPLETE.getCode())
                    .message(ResultCode.PARAM_NOT_COMPLETE.getMessage());
        }
        userMapper.enableUser(userId, userState);

        return Result.ok().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage());
    }

    /**
     * 获取用户总数
     *
     * @return
     */
    @Override
    public Result userTotal() {
        Integer userCount = userMapper.selectCount(new QueryWrapper<User>().select("id").eq("state", "1"));
        return Result.ok().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage())
                .data("userCount", userCount);
    }

    /**
     * 用户反馈
     *
     * @param userName
     * @param content
     * @return
     */
    @Override
    public Result feedback(String userName, String userEmail, String content) {
        taskService.feedbackContent(userEmail, userName, String.valueOf(content));
        return Result.ok().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage());
    }

    /**
     * 管理员邮件发送(前台定义发送内容)
     *
     * @param userName
     * @param userEmail
     * @param content
     * @return
     */
    @Override
    public Result userOperating(String userName, String userEmail, String content) {
        taskService.userOperating(userEmail, userName, String.valueOf(content));
        return Result.ok().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage());
    }

    /**
     * 用户留言
     *
     * @param userName
     * @param userEmail
     * @param content
     * @return
     */
    @Override
    public Result leaveMessage(String userName, String userEmail, String content) {
        taskService.leaveMessageContent(userEmail, userName, String.valueOf(content));
        return Result.ok().code(ResultCode.SUCCESS.getCode()).message(ResultCode.SUCCESS.getMessage());
    }


    /**
     * 获取容器里的Request
     *
     * @return
     */
    private HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes.getRequest();
    }

    /**
     * 获取容器里的Response
     *
     * @return
     */
    private HttpServletResponse getResponse() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes.getResponse();
    }

    /**
     * 解析token
     *
     * @param tokenKey
     * @return
     */
    private User parseByTokenKey(String tokenKey) {
        //重redis中获取
        String token = (String) redisUtil.get(Constants.User.KEY_TOKEN + tokenKey);
        if (token != null) {
            try {
                Claims claims = jwtUtil.parseJwt(token);
                return ClaimsUtils.claims2User(claims);
            } catch (Exception e) {
                log.info("parseByTokenKey token == >" + token + "===>过期了");
                return null;
            }
        }
        return null;
    }

}
