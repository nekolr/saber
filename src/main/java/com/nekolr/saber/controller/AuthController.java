package com.nekolr.saber.controller;

import com.nekolr.saber.security.LoginReq;
import com.nekolr.saber.service.UserService;
import com.nekolr.saber.support.MySecurityContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static com.nekolr.saber.security.LoginReq.Login;
import static com.nekolr.saber.security.LoginReq.Register;

/**
 * 身份凭证控制器
 */
@RestController
@RequestMapping("auth")
public class AuthController {

    @Resource
    private UserService userService;
    @Resource
    private MySecurityContextHolder mySecurityContextHolder;

    /**
     * 用户登录
     *
     * @param loginReq 登录信息
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity login(@Validated({Login.class}) @RequestBody LoginReq loginReq) {
        return ResponseEntity.ok(userService.login(loginReq));
    }

    /**
     * 用户注册
     *
     * @param loginReq
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity register(@Validated({Register.class, Login.class}) @RequestBody LoginReq loginReq) {
        return ResponseEntity.ok(userService.createUser(loginReq));
    }

    /**
     * 获取当前用户信息
     *
     * @return
     */
    @GetMapping("/info")
    public ResponseEntity currentUserInfo() {
        return ResponseEntity.ok(mySecurityContextHolder.getCurrentUser());
    }
}
