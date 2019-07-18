package com.nekolr.saber.service.impl;

import com.nekolr.saber.dao.UserRepository;
import com.nekolr.saber.entity.User;
import com.nekolr.saber.security.AuthenticationUser;
import com.nekolr.saber.service.UserService;
import com.nekolr.saber.service.dto.UserDTO;
import com.nekolr.saber.service.mapper.UserMapper;
import com.nekolr.saber.support.I18nUtils;
import com.nekolr.saber.util.EncryptUtils;
import com.nekolr.saber.util.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Objects;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Resource
    private UserMapper userMapper;
    @Autowired
    private I18nUtils i18nUtils;

    @Override
    public UserDTO findByUsernameOrEmail(String usernameOrEmail) {
        return userMapper.toDto(userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDTO createUser(AuthenticationUser authUser) {

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
        String salt = RandomUtils.randomString(6, false);
        user.setPassword(EncryptUtils.md5(authUser.getPassword() + salt));
        user.setSalt(salt);

        return userMapper.toDto(userRepository.save(user));
    }
}
