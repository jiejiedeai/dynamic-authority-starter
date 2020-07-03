package com.ht.dynamic.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * security 系统配置封装类
 */
@Component
@ConfigurationProperties(prefix = "spring.security")
@Data
public class SecurityProperties {

    /** 不登录即可访问的接口 **/
    private String [] permits;

    /** 放行的静态资源 **/
    private String [] statics ={"/swagger-ui.html",
            "/swagger/**",
            "/webjars/**",
            "/swagger-resources/**",
            "/v2/**"};

    /** url在redis中不存在 是否校验权限开关**/
    private boolean noMatcherPermit = false;
}
