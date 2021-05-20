package com.nekolr.saber.controller;

import com.nekolr.saber.exception.BadRequestException;
import com.nekolr.saber.security.AuthenticationInfo;
import com.nekolr.saber.security.AuthenticationUser;
import com.nekolr.saber.service.UserService;
import com.nekolr.saber.service.dto.UserDTO;
import com.nekolr.saber.support.I18nUtils;
import com.nekolr.saber.support.JwtUtils;
import com.nekolr.saber.support.SecurityContextHolder;
import com.nekolr.saber.util.EncryptUtils;
import com.nekolr.saber.util.IdGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.Objects;

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
    private I18nUtils i18nUtils;
    @Resource
    private SecurityContextHolder securityContextHolder;

    @Value("${jwt.period}")
    private Duration period;

    /**
     * 用户登录
     *
     * @param authUser 登录信息
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity login(@Validated({Login.class})
                                @RequestBody AuthenticationUser authUser) {

        UserDTO user = userService.findByUsernameOrEmail(authUser.getUsername());

        if (Objects.isNull(user) ||
                !user.getPassword().equals(EncryptUtils.md5(authUser.getPassword() + user.getSalt()))) {
            throw new BadRequestException(i18nUtils.getMessage("exceptions.user.invalid_username_or_password"));
        }

        // 校验完成后签发 Token
        String token = JwtUtils.issueJwt(IdGenerator.randomUUID(), authUser.getUsername(),
                "", period.getSeconds(), "");

        return ResponseEntity.ok(new AuthenticationInfo(token, user));
    }

    /**
     * 用户注册
     *
     * @param authUser
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity register(@Validated({Register.class, Login.class})
                                   @RequestBody AuthenticationUser authUser) {
        UserDTO user = userService.createUser(authUser);
        return ResponseEntity.ok(user);
    }

    /**
     * 获取当前用户信息
     *
     * @return
     */
    @GetMapping("/info")
    public ResponseEntity currentUserInfo() {
        UserDTO user = securityContextHolder.getUser();
        return ResponseEntity.ok(user);
    }
}
