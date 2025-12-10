package com.nekolr.saber.support;

import com.nekolr.saber.exception.BadRequestException;
import com.nekolr.saber.service.dto.UserDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ContextHolder {

    private final I18nUtils i18nUtils;

    private final static ScopedValue<UserDTO> USER = ScopedValue.newInstance();

    private UserDTO getUser() {
        return USER.isBound() ? USER.get() : null;
    }

    /**
     * 在指定作用域内执行操作，期间 USER 绑定到指定用户
     *
     * @param user     用户信息
     * @param runnable 要执行的操作
     */
    public void runWithUser(UserDTO user, Runnable runnable) {
        ScopedValue.where(USER, user).run(runnable);
    }

    /**
     * 在指定作用域内执行操作并返回结果，期间 USER 绑定到指定用户
     *
     * @param user 用户信息
     * @param op   要执行的操作
     * @return 操作结果
     */
    public <R, X extends Throwable> R callWithUser(UserDTO user, ScopedValue.CallableOp<? extends R, X> op) throws X {
        return ScopedValue.where(USER, user).call(op);
    }

    /**
     * 获取用户信息
     */
    public UserDTO getCurrentUser() {
        UserDTO user;
        try {
            user = getUser();
        } catch (Exception e) {
            String message = i18nUtils.getMessage("exception.auth.unauthorized");
            throw new BadRequestException(message);
        }
        return user;
    }
}
