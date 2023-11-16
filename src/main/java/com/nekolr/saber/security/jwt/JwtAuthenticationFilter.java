package com.nekolr.saber.security.jwt;

import com.nekolr.saber.service.UserService;
import com.nekolr.saber.service.dto.UserDTO;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Objects;

import static com.nekolr.saber.support.Saber.TOKEN_HEADER_KEY;
import static com.nekolr.saber.support.Saber.TOKEN_HEADER_VALUE_PREFIX;

@Slf4j
@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String requestURI = request.getRequestURI();
        String jwt = this.resolveToken(request);

        Claims claims;
        if (StringUtils.hasText(jwt) && (claims = tokenProvider.getClaims(jwt)) != null) {
            String username = claims.getSubject();
            // 只有在 Authentication 为空时才会放入
            if (Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
                // 只判断 token 合法有效，真正的用户信息通过查询数据库得到
                UserDTO user = userService.findByUsernameOrEmail(username);
                UsernamePasswordAuthenticationToken authenticationToken
                        = new UsernamePasswordAuthenticationToken(user, null, null);

                log.debug("set Authentication to security context for '{}', uri: {}", username, requestURI);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } else {
            log.debug("no valid JWT token found, uri: {}", requestURI);
        }

        chain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(TOKEN_HEADER_KEY);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_HEADER_VALUE_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
