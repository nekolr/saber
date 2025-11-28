package com.nekolr.saber.service.impl;

import com.nekolr.saber.dao.UserRepository;
import com.nekolr.saber.service.UserQueryService;
import com.nekolr.saber.service.dto.UserDTO;
import com.nekolr.saber.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "user")
public class UserQueryServiceImpl implements UserQueryService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Cacheable(key = "'usernameOrEmail:' + #p0")
    public UserDTO findByUsernameOrEmail(String usernameOrEmail) {
        return userMapper.toDto(userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail));
    }
}
