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

    @NotBlank(message = "exceptions.user.username_can_not_be_blank", groups = Login.class)
    private String username;

    @NotBlank(message = "exceptions.user.password_can_not_be_blank", groups = Login.class)
    private String password;

    @Email(message = "exceptions.user.email_format_error", groups = Register.class)
    @NotBlank(message = "exceptions.user.email_can_not_be_blank", groups = Register.class)
    private String email;


    public interface Login { }

    public interface Register { }
}
