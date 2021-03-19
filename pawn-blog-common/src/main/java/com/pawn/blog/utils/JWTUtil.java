package com.pawn.blog.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/***
 * description: Jwt工具类
 * @author:美茹冠玉
 * @Return
 * @param
 * @date 2020/12/22 18:01
 */

@Component
public class JWTUtil {


    public static final String SUBJECT = "LiXunXun";

    /**
     * 过期时间，毫秒，三小时
     */
    public static long EXPIRE = Constants.TimeValue.HOUR_3 * 1000;
    /**
     * 六十天
     */
    public static long EXPIRE_6 = Constants.TimeValue.HOUR_3 * 1000 * 8 * 60;

    /**
     * 密钥
     */
    public static final String key = "43fa335bba9a7a4589fde2b36ff6c97c";

    /**
     * 生成jwt
     *
     * @param map 需要生成的token的对象
     * @return 生成的token
     */
    public static String createJwt(Map<String, Object> map) {

        if (map == null || map.isEmpty()) {
            return null;
        }
        JwtBuilder jwtBuilder = Jwts.builder().setSubject(SUBJECT);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            jwtBuilder.claim(entry.getKey(), entry.getValue());
        }
        return jwtBuilder.setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
                .signWith(SignatureAlgorithm.HS256, key).compact();
    }

    public static String createJwt(String userId) {

        JwtBuilder jwtBuilder = Jwts.builder().setSubject(SUBJECT);
        return jwtBuilder.setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_6))
                .setId(userId)
                .signWith(SignatureAlgorithm.HS256, key).compact();
    }

    /**
     * jwt生成的token
     *
     * @param token
     * @return
     */
    public Claims parseJwt(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token).getBody();
        return claims;
    }

}
