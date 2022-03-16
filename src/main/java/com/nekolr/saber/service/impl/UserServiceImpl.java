package com.nekolr.saber.service.impl;

import com.nekolr.saber.dao.UserRepository;
import com.nekolr.saber.entity.User;
import com.nekolr.saber.exception.BadRequestException;
import com.nekolr.saber.security.LoginVo;
import com.nekolr.saber.security.LoginReq;
import com.nekolr.saber.security.jwt.TokenProvider;
import com.nekolr.saber.service.UserService;
import com.nekolr.saber.service.dto.UserDTO;
import com.nekolr.saber.service.mapper.UserMapper;
import com.nekolr.saber.support.I18nUtils;
import com.nekolr.saber.util.EncryptUtils;
import com.nekolr.saber.util.RandomUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final I18nUtils i18nUtils;
    private final UserMapper userMapper;
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;

    @Override
    public UserDTO findByUsernameOrEmail(String usernameOrEmail) {
        return userMapper.toDto(userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDTO createUser(LoginReq authUser) {

        User usernameExist = userRepository.findByUsername(authUser.getUsername());
        if (!Objects.isNull(usernameExist)) {
            throw new RuntimeException(i18nUtils.getMessage("exceptions.user.username_exist"));
        }

        User emailExist = userRepository.findByEmail(authUser.getEmail());
        if (!Objects.isNull(emailExist)) {
            throw new RuntimeException(i18nUtils.getMessage("exceptions.user.email_exist"));
        }

        User user = new User();
        user.setUsername(authUser.getUsername());
        user.setEmail(authUser.getEmail());
        String salt = RandomUtils.randomString(6);
        user.setPassword(EncryptUtils.md5(authUser.getPassword() + salt));
        user.setSalt(salt);

        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public LoginVo login(LoginReq loginReq) {

        UserDTO user = findByUsernameOrEmail(loginReq.getUsername());

        if (Objects.isNull(user) ||
                !user.getPassword().equals(EncryptUtils.md5(loginReq.getPassword() + user.getSalt()))) {
            throw new BadRequestException(i18nUtils.getMessage("exceptions.user.invalid_username_or_password"));
        }

        String token = tokenProvider.createToken(user.getUsername());

        return new LoginVo(token, user);
    }
}
