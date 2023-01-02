package cn.liyu.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Jwt参数配置
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class SecurityProperties {

    /**
     * 必须使用最少88位的Base64对该令牌进行编码
     */
    private String base64Secret;

    /**
     * 令牌过期时间 此处单位/毫秒
     */
    private Long tokenValidityInSeconds;


    /**
     * token 续期检查
     */
    private Long detect;

    /**
     * 续期时间
     */
    private Long renew;

    /**
     * 在线人数
     */
    private Long onlineCount;

}
