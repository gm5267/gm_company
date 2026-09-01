package com.gm.ace.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * JWT 配置属性（前缀 jwt）。
 * 采用 @Component + @ConfigurationProperties 的组合：由组件扫描注册为 Spring Bean，
 * 并由 ConfigurationPropertiesBindingPostProcessor 将环境变量绑定到字段（最稳妥的方式）。
 *
 * @author guoym
 */
@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    /** HS256 签名密钥（>= 256bit） */
    private String secret;

    /** 访问令牌有效期（秒） */
    private Long expireSeconds = 7200L;

    /** 刷新令牌有效期（秒） */
    private Long refreshSeconds = 604800L;
}
