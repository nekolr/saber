package com.nekolr.saber.config;

import com.nekolr.saber.security.JwtAuthenticationEntryPoint;
import com.nekolr.saber.security.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
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

                // 所有请求都要经过验证
                .anyRequest().authenticated();

        httpSecurity
                // 添加登录和权限校验的两个过滤器
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
