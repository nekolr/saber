package com.nekolr.saber.service;

import com.nekolr.saber.service.dto.UserDTO;

public interface UserQueryService {

    /**
     * 根据用户名或邮箱获取用户信息
     */
    UserDTO findByUsernameOrEmail(String usernameOrEmail);
}
