package com.nekolr.saber.service;

import com.nekolr.saber.security.LoginVo;
import com.nekolr.saber.security.LoginRequest;
import com.nekolr.saber.service.dto.UserDTO;


public interface UserService {

    /**
     * 创建用户
     */
    UserDTO createUser(LoginRequest authUser);

    /**
     * 登录
     */
    LoginVo login(LoginRequest authUser);

}
