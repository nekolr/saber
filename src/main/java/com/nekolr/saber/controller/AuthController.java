package com.nekolr.saber.controller;

import com.nekolr.saber.security.AuthenticationUser;
import com.nekolr.saber.service.UserService;
import com.nekolr.saber.support.SecurityContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static com.nekolr.saber.security.AuthenticationUser.Login;
import static com.nekolr.saber.security.AuthenticationUser.Register;

/**
 * 身份凭证控制器
 */
@RestController
@RequestMapping("auth")
public class AuthController {

    @Resource
    private UserService userService;
    @Resource
    private SecurityContextHolder securityContextHolder;

    /**
     * 用户登录
     *
     * @param authUser 登录信息
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity login(@Validated({Login.class}) @RequestBody AuthenticationUser authUser) {
        return ResponseEntity.ok(userService.login(authUser));
    }

    /**
     * 用户注册
     *
     * @param authUser
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity register(@Validated({Register.class, Login.class}) @RequestBody AuthenticationUser authUser) {
        return ResponseEntity.ok(userService.createUser(authUser));
    }

    /**
     * 获取当前用户信息
     *
     * @return
     */
    @GetMapping("/info")
    public ResponseEntity currentUserInfo() {
        return ResponseEntity.ok(securityContextHolder.getUser());
    }
}
