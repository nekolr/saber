package com.nekolr.saber.support;

import com.nekolr.saber.config.ThreadLocalContext;
import com.nekolr.saber.exception.BadRequestException;
import com.nekolr.saber.service.dto.UserDTO;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

@Component
public class MySecurityContextHolder {

    @Resource
    private I18nUtils i18nUtils;

    /**
     * 获取用户信息
     */
    public UserDTO getCurrentUser() {
        UserDTO user;
        try {
//            user = (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            user = ThreadLocalContext.getUser();
        } catch (Exception e) {
            throw new BadRequestException(i18nUtils.getMessage("exceptions.unauthorized"));
        }
        return user;
    }
}
