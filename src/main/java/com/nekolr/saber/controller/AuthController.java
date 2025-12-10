package com.nekolr.saber.controller;

import com.nekolr.saber.security.LoginRequest;
import com.nekolr.saber.security.LoginVo;
import com.nekolr.saber.service.UserService;
import com.nekolr.saber.service.dto.UserDTO;
import com.nekolr.saber.support.ContextHolder;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.nekolr.saber.security.LoginRequest.Login;
import static com.nekolr.saber.security.LoginRequest.Register;

/**
 * 身份凭证控制器
 */
@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final ContextHolder contextHolder;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public ResponseEntity<@NonNull LoginVo> login(@Validated({Login.class}) @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(userService.login(loginRequest));
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public ResponseEntity<@NonNull UserDTO> register(@Validated({Register.class, Login.class}) @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(userService.createUser(loginRequest));
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/info")
    public ResponseEntity<@NonNull UserDTO> currentUserInfo() {
        return ResponseEntity.ok(contextHolder.getCurrentUser());
    }
}
