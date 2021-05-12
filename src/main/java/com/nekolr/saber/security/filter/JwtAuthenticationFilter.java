package com.nekolr.saber.security.filter;

import com.nekolr.saber.support.Saber;
import com.nekolr.saber.security.JwtUser;
import com.nekolr.saber.service.UserService;
import com.nekolr.saber.service.dto.UserDTO;
import com.nekolr.saber.support.JwtUtils;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * 自定义访问权限校验过滤器
 *
 * @author nekolr
 */
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Resource
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String header = request.getHeader(Saber.TOKEN_HEADER_KEY);

        if (StringUtils.isBlank(header) || !header.startsWith(Saber.TOKEN_HEADER_VALUE_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        String jwt = StringUtils.replace(header, Saber.TOKEN_HEADER_VALUE_PREFIX, "");
        try {
            // 只判断 token 合法有效，真正的用户信息通过查询数据库得到
            JwtUser jwtUser = JwtUtils.parseJwt(jwt);
            // 只有在 Authentication 为空时才会放入
            if (Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
                UserDTO user = userService.findByUsernameOrEmail(jwtUser.getUsername());
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(user, null, null);

                log.info("Authorized user '{}', setting security context", jwtUser.getUsername());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (ExpiredJwtException e) {
            // token 过期
            log.error("Token has expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            // token 格式错误
            log.error("Token format error: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            // token 构造错误
            log.error("Token construct error: {}", e.getMessage());
        } catch (SignatureException e) {
            // 签名失败
            log.error("Signature failed: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            // 非法参数
            log.error("Illegal argument: {}", e.getMessage());
        } catch (JwtException e) {
            // 其他异常
            log.error("Other exception: {}", e.getMessage());
        }
        chain.doFilter(request, response);
    }
}
