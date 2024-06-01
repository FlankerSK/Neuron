package com.practicum.neuron.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Jwt 工具类
 */
@Slf4j
@Component
public class JwtUtil {
    // Claim 中的用户名
    private static final String CLAM_KEY_USERNAME = "sub";

    // Claim 中的创建时间
    private static final String CLAM_KEY_CREATED = "iat";

    // JWT 密钥
    @Value("${jwt.secret}")
    private String secret;

    // JWT refresh_token 过期时间
    @Value("${jwt.refresh_expiration}")
    private Long refreshExpiration;

    // JWT access_token 过期时间
    @Value("${jwt.access_expiration}")
    private Long accessExpiration;

    // JWT 令牌请求头（Authorization）
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;

    // JWT 令牌前缀（Bearer）
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    // Redis访问支持
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    //================ private methods ==================

    /**
     * 根据负载生成 JWT 的 refreshToken
     *
     * @param claims 负载
     * @return JWT 的 token
     */
    private String createRefreshToken(Map<String,Object> claims){
        return Jwts.builder()
                .setClaims(claims)  // 设置负载
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(generateExpirationDate(refreshExpiration))    // 设置过期时间
                // 设置签名使用的签名算法和签名使用的秘钥
                .signWith(
                        new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"),
                        SignatureAlgorithm.HS256
                )
                .compact();
    }

    /**
     * 根据负载生成 JWT 的 accessToken
     *
     * @param claims 负载
     * @return JWT 的 token
     */
    private String createAccessToken(Map<String,Object> claims){
        return Jwts.builder()
                .setClaims(claims)  // 设置负载
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(generateExpirationDate(accessExpiration))    // 设置过期时间
                // 设置签名使用的签名算法和签名使用的秘钥
                .signWith(
                        new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"),
                        SignatureAlgorithm.HS256
                )
                .compact();
    }

    /**
     * 生成 token 的过期时间
     *
     * @param expiration 过期时间 (Long 类型)
     * @return token 的过期时间 (Date 类型)
     */
    private Date generateExpirationDate(Long expiration){
        /*
            Date 构造器接受格林威治时间，推荐使用 System.currentTimeMillis() 获取当前时间距离 1970-01-01 00:00:00 的毫秒数
            而我们在配置文件中配置的是秒数，所以需要乘以 1000。
            一般而言 Token 的过期时间为 7 天，因此我们一般在 Spring Boot 的配置文件中将 jwt.refresh_expiration 设置为 604800，
            即 7 * 24 * 60 * 60 = 604800 秒。
         */
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    /**
     * 从 token 中获取 JWT 中的负载
     * @param token JWT 的 token
     * @return JWT 中的负载
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims;
        // 解析 JWT 的 token
        claims = Jwts.parserBuilder()
                .setSigningKey(secret.getBytes(StandardCharsets.UTF_8)) // 指定签名使用的密钥（会自动推断签名的算法）
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims;
    }

    /**
     * 验证 token 是否过期
     * @param token JWT 的 token
     * @return token 是否过期 true：过期 false：未过期
     */
    private boolean isTokenExpired(String token){
        Date expiredDate = getExpiredDateFromToken(token);
        return expiredDate.before(new Date());
    }

    /**
     * 从 token 中获取过期时间
     * @param token JWT 的 token
     * @return 过期时间
     */
    private Date getExpiredDateFromToken(String token){
        return getClaimsFromToken(token).getExpiration();
    }


    //================ public methods ==================
    /**
     * 从 请求 中获取token
     *
     * @param request HTTP 请求
     * @return 登录用户名 string
     */
    public String getToken(HttpServletRequest request) {
        // 从请求中获取 JWT 令牌的请求头（即：Authorization）
        String authHeader = request.getHeader(this.tokenHeader);
        // 如果请求头为空，或者不以 JWT 令牌前缀（即：Bearer）开头
        if (authHeader == null || !authHeader.startsWith(this.tokenHead)){
            return null;
        }
        // 去掉 JWT 令牌前缀后, 返回剩下的内容
        return authHeader.substring(this.tokenHead.length());
    }

    /**
     * 从 token 中获取登录用户名
     *
     * @param token JWT 的 token
     * @return 登录用户名 string
     */
    public String getUserNameFromToken(String token){
        String username;
        // 从 token 中获取 JWT 中的负载
        Claims claims = getClaimsFromToken(token);
        // 从负载中获取用户名
        username = claims.get(CLAM_KEY_USERNAME, String.class);
        return username;
    }

    /**
     * 验证 token 是否有效
     *
     * @param token    JWT 的 token
     * @param username 从数据库中查询出来的用户名
     * @return token 是否有效 true：有效 false：无效
     */
    public boolean validateToken(String token, String username) {
        // 从 token 中获取用户名
        String tokenUsername = getUserNameFromToken(token);
        // 条件一：用户名不为 null
        // 条件二：用户名和从数据库中查询出来的用户名一致
        // 条件三：token 未过期
        // 条件四: token 未被注销
        ValueOperations<String, String> op = redisTemplate.opsForValue();
        return tokenUsername.equals(username)
                && !( isTokenExpired(token)
                    || token.equals(op.get(username + "_refresh"))
                    || token.equals(op.get(username + "_access")));
    }

    /**
     * 根据用户信息生成 refreshToken
     *
     * @param username 用户名
     * @return token 字符串
     */
    public String createRefreshToken(String username) {
        // 创建负载
        Map<String,Object> claims = new HashMap<>();
        // 设置负载中的用户名
        claims.put(CLAM_KEY_USERNAME,username);
        // 设置负载中的创建时间
        claims.put(CLAM_KEY_CREATED,new Date());
        // 根据负载生成 token
        return createRefreshToken(claims);
    }

    /**
     * 根据用户信息生成 accessToken
     *
     * @param username 用户名
     * @return token 字符串
     */
    public String createAccessToken(String username) {
        // 创建负载
        Map<String,Object> claims = new HashMap<>();
        // 设置负载中的用户名
        claims.put(CLAM_KEY_USERNAME, username);
        // 设置负载中的创建时间
        claims.put(CLAM_KEY_CREATED, new Date());
        // 根据负载生成 token
        return createAccessToken(claims);
    }

    /**
     * 注销一对 refreshToken 和 accessToken
     *
     * @param refreshToken Jwt refreshToken
     * @param accessToken Jwt accessToken
     * @param username 用户名
     */
    public void blockToken(String refreshToken, String accessToken, String username) {
        // 采用黑名单机制, 将此对 token 置于 Redis 中"拉黑"
        // 将 refreshToken 插入到Redis中, 键为用户名加"_refresh", 过期时间为 Token 记录的有效截止日期与当前时间的差
        String refreshTokenKey = username + "_refresh";
        Long refreshExpiresTime = this.getExpiredDateFromToken(refreshToken).getTime();
        Long nowTime = new Date(System.currentTimeMillis()).getTime();
        if (refreshExpiresTime > nowTime){
            redisTemplate.opsForValue().set(refreshTokenKey, refreshToken);
            redisTemplate.expire(refreshTokenKey, refreshExpiresTime - nowTime, TimeUnit.MILLISECONDS);
        }

        // 将 accessToken 插入到Redis中, 键为用户名加"_access", 过期时间为 Token 记录的有效截止日期与当前时间的差
        String accessTokenKey = username + "_access";
        Long accessExpiresTime = this.getExpiredDateFromToken(accessToken).getTime();
        if (accessExpiresTime > nowTime){
            redisTemplate.opsForValue().set(accessTokenKey, accessToken);
            redisTemplate.expire(accessTokenKey, accessExpiresTime - nowTime, TimeUnit.MILLISECONDS);
        }
    }
}
