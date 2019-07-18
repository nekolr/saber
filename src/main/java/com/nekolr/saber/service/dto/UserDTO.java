package com.nekolr.saber.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

/**
 * User DTO
 */
@Data
public class UserDTO implements Serializable {
    /**
     * 用户 ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    @JsonIgnore
    private String password;

    /**
     * 随机盐（用于加密密码）
     */
    @JsonIgnore
    private String salt;

    /**
     * 邮箱
     */
    private String email;
}
