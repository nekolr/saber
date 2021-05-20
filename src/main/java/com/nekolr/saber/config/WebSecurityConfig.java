package com.nekolr.saber.config;

import com.nekolr.saber.security.JwtAuthenticationEntryPoint;
import com.nekolr.saber.security.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private JwtAuthenticationEntryPoint authenticationEntryPoint;
    @Resource
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // X-Frame-Options: SAMEORIGIN
                .headers().frameOptions().sameOrigin().and()
                // X-Content-Type-Options: nosniff
                .headers().contentTypeOptions().and().and()
                // X-XSS-Protection: 1; mode=block
                .headers().xssProtection().xssProtectionEnabled(true).and().and()
                // 关闭 csrf
                .csrf().disable()
                // 授权异常处理
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint).and()
                // 不需要 session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                // 过滤请求
                .authorizeRequests()
                // OPTIONS 预检请求可以匿名访问
                .antMatchers(HttpMethod.OPTIONS, "/**").anonymous()
                // 登录请求不拦截（如果登录请求头包含 Authorization: Bearer 任意字符，那么还是会进行校验）
                .antMatchers(HttpMethod.POST, "/auth/login").permitAll()
                // 图片路径可以匿名访问
                .antMatchers(HttpMethod.GET, "/images/**").anonymous()
                // 静态资源可以匿名访问
                .antMatchers(HttpMethod.GET, "/js/**").anonymous()
                .antMatchers(HttpMethod.GET, "/css/**").anonymous()
                .antMatchers(HttpMethod.GET, "/img/**").anonymous()
                .antMatchers(HttpMethod.GET, "/fonts/**").anonymous()
                .antMatchers(HttpMethod.GET, "/favicon.png").anonymous()
                // 主页可以匿名访问
                .antMatchers(HttpMethod.GET, "/").anonymous()
                // 所有请求都要经过验证
                .anyRequest().authenticated();

        httpSecurity
                // 添加权限校验过滤器
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
