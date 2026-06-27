package com.springbootinit.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * JWT 配置（绑定 application.yml 中的 jwt 节点）
 *
 */
@Configuration
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtConfig {

    /**
     * 签名密钥
     */
    private String secret;

    /**
     * Token 过期时间（毫秒）
     */
    private Long expire;

    /**
     * 请求头名称
     */
    private String header = "Authorization";

    /**
     * Token 前缀（含末尾空格，如 "Bearer "）
     */
    private String tokenPrefix = "Bearer ";
}
