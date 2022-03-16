package com.nekolr.saber.security;

import com.nekolr.saber.service.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * 签发令牌后返回给前端的实体
 */
@Getter
@AllArgsConstructor
public class LoginVo implements Serializable {

    private String token;

    private UserDTO user;
}
