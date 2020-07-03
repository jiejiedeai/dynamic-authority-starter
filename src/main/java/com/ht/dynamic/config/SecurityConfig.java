package com.ht.dynamic.config;

import com.ht.dynamic.authority.DynamicSecurityInterceptor;
import com.ht.dynamic.authority.SecurityBeanConfig;
import com.ht.dynamic.oauth.TokenConfig;
import com.ht.dynamic.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

@Configuration
@Import({SecurityBeanConfig.class, RedisTemplateConfig.class, TokenConfig.class})
@EnableConfigurationProperties({SecurityProperties.class})
@EnableResourceServer
public class SecurityConfig extends ResourceServerConfigurerAdapter {

    //系统配置
    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private DynamicSecurityInterceptor dynamicSecurityInterceptor;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private TokenStore tokenStore;

    /** 资源名称 **/
    @Value("${spring.application.name}")
    private String name;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        //当前服务器的资源id，认证服务器会认证客户端有没有访问资源id的权限
        resources.resourceId(name)
                .tokenStore(tokenStore);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults());
        http.csrf().disable();
        http.addFilterAfter(dynamicSecurityInterceptor, FilterSecurityInterceptor.class);
        http.authorizeRequests().antMatchers(securityProperties.getStatics()).permitAll();
        http
                .exceptionHandling()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated();
    }

}
