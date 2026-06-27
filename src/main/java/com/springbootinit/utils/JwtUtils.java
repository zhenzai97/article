package com.springbootinit.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.springbootinit.common.ErrorCode;
import com.springbootinit.config.JwtConfig;
import com.springbootinit.exception.BusinessException;
import java.util.Date;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * JWT 工具类
 *
 */
@Component
public class JwtUtils {

    /**
     * Token 载荷中的用户 id 字段名
     */
    private static final String CLAIM_USER_ID = "userId";

    /**
     * Token 载荷中的用户角色字段名
     */
    private static final String CLAIM_USER_ROLE = "userRole";

    @Resource
    private JwtConfig jwtConfig;

    /**
     * 生成 JWT Token（从 application.yml 读取 secret、expire）
     *
     * @param userId   用户 id
     * @param userRole 用户角色（user / admin / ban）
     * @return 签名后的 Token 字符串
     */
    public String generateToken(Long userId, String userRole) {
        return generateToken(userId, userRole, jwtConfig.getSecret(), jwtConfig.getExpire());
    }

    /**
     * 生成 JWT Token
     *
     * @param userId   用户 id
     * @param userRole 用户角色（user / admin / ban）
     * @param secret   签名密钥
     * @param expireMs 过期时间（毫秒）
     * @return 签名后的 Token 字符串
     */
    public String generateToken(Long userId, String userRole, String secret, long expireMs) {
        Date expireAt = new Date(System.currentTimeMillis() + expireMs);
        return JWT.create()
                .withClaim(CLAIM_USER_ID, userId)
                .withClaim(CLAIM_USER_ROLE, userRole)
                .withExpiresAt(expireAt)
                .sign(Algorithm.HMAC256(secret));
    }

    /**
     * 解析并校验 JWT Token（从 application.yml 读取 secret）
     *
     * @param token 待解析的 Token
     * @return 解码后的 JWT，可通过 getClaim 读取载荷
     * @throws BusinessException Token 无效、过期或签名不匹配时抛出 NOT_LOGIN_ERROR
     */
    public DecodedJWT decodeToken(String token) {
        return decodeToken(token, jwtConfig.getSecret());
    }

    /**
     * 解析并校验 JWT Token（校验签名与过期时间）
     *
     * @param token  待解析的 Token
     * @param secret 签名密钥
     * @return 解码后的 JWT，可通过 getClaim 读取载荷
     * @throws BusinessException Token 无效、过期或签名不匹配时抛出 NOT_LOGIN_ERROR
     */
    public DecodedJWT decodeToken(String token, String secret) {
        try {
            return JWT.require(Algorithm.HMAC256(secret))
                    .build()
                    .verify(token);
        } catch (JWTVerificationException e) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "Token 无效或已过期");
        }
    }

    /**
     * 从 Token 中解析用户 id
     *
     * @param token JWT 字符串
     * @return 用户 id
     */
    public Long getUserIdFromToken(String token) {
        DecodedJWT jwt = decodeToken(token);
        Long userId = jwt.getClaim(CLAIM_USER_ID).asLong();
        if (userId == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return userId;
    }

    /**
     * 从请求头中提取 Token 并解析用户 id
     *
     * @param request HTTP 请求
     * @return 用户 id；未携带 Token 时返回 null
     */
    public Long getUserIdFromRequest(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        if (StringUtils.isBlank(token)) {
            return null;
        }
        return getUserIdFromToken(token);
    }

    /**
     * 从请求头中提取 Token（从 application.yml 读取 header、token-prefix）
     *
     * @param request HTTP 请求
     * @return 去掉前缀后的 Token；未携带或格式不符时返回 null
     */
    public String getTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader(jwtConfig.getHeader());
        String prefix = jwtConfig.getTokenPrefix();
        if (StringUtils.isNotBlank(header) && StringUtils.isNotBlank(prefix) && header.startsWith(prefix)) {
            return header.substring(prefix.length());
        }
        return null;
    }
}
