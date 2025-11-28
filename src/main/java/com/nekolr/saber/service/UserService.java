package com.nekolr.saber.service;

import com.nekolr.saber.security.LoginVo;
import com.nekolr.saber.security.LoginRequest;
import com.nekolr.saber.service.dto.UserDTO;


public interface UserService {

    /**
     * 根据用户名或邮箱获取用户信息
     */
    UserDTO findByUsernameOrEmail(String usernameOrEmail);

    /**
     * 创建用户
     */
    UserDTO createUser(LoginRequest authUser);

    /**
     * 登录
     */
    LoginVo login(LoginRequest authUser);

}
