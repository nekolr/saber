package com.nekolr.saber.service;

import com.nekolr.saber.security.AuthenticationUser;
import com.nekolr.saber.service.dto.UserDTO;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

@CacheConfig(cacheNames = "user")
public interface UserService {

    /**
     * 根据用户名或邮箱获取用户信息
     *
     * @param usernameOrEmail 用户名或邮箱
     * @return
     */
    @Cacheable(key = "'usernameOrEmail:' + #p0")
    UserDTO findByUsernameOrEmail(String usernameOrEmail);

    /**
     * 创建用户
     *
     * @param authUser
     * @return
     */
    @CacheEvict(allEntries = true)
    UserDTO createUser(AuthenticationUser authUser);

}
