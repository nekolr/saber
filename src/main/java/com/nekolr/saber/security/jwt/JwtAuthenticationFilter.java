package com.nekolr.saber.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nekolr.saber.service.UserQueryService;
import com.nekolr.saber.service.dto.UserDTO;
import com.nekolr.saber.support.ContextHolder;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Objects;

@Slf4j
@Component
@NullMarked
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final ContextHolder contextHolder;
    private final UserQueryService userQueryService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    // 白名单路径模式
    private static final String[] WHITE_LIST_PATTERNS = {
            "/",
            "/images/**",
            "/assets/**",
            "/index.html",
            "/favicon.ico",
            "/favicon.png"
    };

    // 需要特定 HTTP 方法的白名单路径
    private static final String METHOD_SPECIFIC_LOGIN = "/auth/login";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String requestURI = request.getRequestURI();
        String jwt = this.resolveToken(request);

        // 检查是否为不需要认证的路径
        if (isAnonymous(requestURI, request.getMethod())) {
            chain.doFilter(request, response);
            return;
        }

        Claims claims;
        if (StringUtils.hasText(jwt) && (claims = tokenProvider.getClaims(jwt)) != null) {
            String username = claims.getSubject();
            UserDTO userDTO = contextHolder.getCurrentUser();
            if (Objects.isNull(userDTO)) {

                // 只判断 token 合法有效，真正的用户信息通过查询数据库得到
                UserDTO user = userQueryService.findByUsernameOrEmail(username);
                if (Objects.nonNull(user)) {
                    // 使用 ScopedValue 在整个过滤器链执行期间绑定用户信息
                    contextHolder.runWithUser(user, () -> {
                        log.debug("set Authentication to scoped context for '{}', uri: {}", username, requestURI);
                        try {
                            chain.doFilter(request, response);
                        } catch (IOException | ServletException e) {
                            log.error("Error processing filter chain", e);
                            throw new RuntimeException(e);
                        }
                    });
                } else {
                    sendUnauthorizedResponse(response);
                }
            } else {
                // 用户已存在，直接执行过滤器链
                chain.doFilter(request, response);
            }
        } else {
            log.debug("no valid JWT token found, uri: {}", requestURI);
            sendUnauthorizedResponse(response);
        }
    }

    private boolean isAnonymous(String requestURI, String method) {
        // OPTIONS 预检请求可以匿名访问
        if ("OPTIONS".equals(method)) {
            return true;
        }

        // 登录请求不拦截（只允许 POST 方法）
        if (pathMatcher.match(METHOD_SPECIFIC_LOGIN, requestURI) && "POST".equals(method)) {
            return true;
        }

        // 检查是否匹配白名单路径模式
        for (String pattern : WHITE_LIST_PATTERNS) {
            if (pathMatcher.match(pattern, requestURI)) {
                return true;
            }
        }

        return false;
    }

    private void sendUnauthorizedResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        ResponseEntity<Void> responseEntity = new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        response.getWriter().write(objectMapper.writeValueAsString(responseEntity));
    }

    private @Nullable String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
