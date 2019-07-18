package com.nekolr.saber.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * User Entity
 *
 * @author nekolr
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "user")
public class User implements Serializable {

    /**
     * 用户 ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户名
     */
    @NotBlank
    @Column(unique = true)
    private String username;

    /**
     * 密码
     */
    @NotBlank
    private String password;

    /**
     * 随机盐（用于加密密码）
     */
    private String salt;

    /**
     * 邮箱
     */
    @Column(unique = true)
    private String email;
}
