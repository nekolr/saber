package com.nekolr.saber.support;

import com.nekolr.saber.exception.BadRequestException;
import com.nekolr.saber.service.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SecurityContextHolder {

    @Autowired
    private I18nUtils i18nUtils;

    /**
     * 获取用户信息
     *
     * @return
     */
    public UserDTO getUser() {
        UserDTO user;
        try {
            user = (UserDTO) org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new BadRequestException(i18nUtils.getMessage("exceptions.unauthorized"));
        }
        return user;
    }
}
