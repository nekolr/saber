package com.nekolr.saber.controller;

import com.nekolr.saber.security.AuthenticationInfo;
import com.nekolr.saber.security.AuthenticationUser;
import com.nekolr.saber.service.UserService;
import com.nekolr.saber.service.dto.UserDTO;
import com.nekolr.saber.support.I18nUtils;
import com.nekolr.saber.support.JwtUtils;
import com.nekolr.saber.support.SecurityContextHolder;
import com.nekolr.saber.util.EncryptUtils;
import com.nekolr.saber.util.IdGenerator;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private UserService userService;
    @Autowired
    private I18nUtils i18nUtils;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
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
            throw new AccountExpiredException(i18nUtils.getMessage("exceptions.user.invalid_username_or_password"));
        }

        // 校验完成后签发 Token
        String token = jwtUtils.issueJwt(IdGenerator.randomUUID(), authUser.getUsername(),
                "", period.getSeconds(), "", SignatureAlgorithm.HS512);

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
