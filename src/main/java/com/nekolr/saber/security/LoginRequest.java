package com.nekolr.saber.security;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 接收需要校验凭证的用户信息
 */
@Getter
@Setter
@ToString
public class LoginRequest implements Serializable {

    @NotBlank(message = "exception.user.username_required", groups = Login.class)
    private String username;

    @NotBlank(message = "exception.user.password_required", groups = Login.class)
    private String password;

    @Email(message = "exception.user.email_invalid", groups = Register.class)
    @NotBlank(message = "exception.user.email_required", groups = Register.class)
    private String email;


    public interface Login { }

    public interface Register { }
}
