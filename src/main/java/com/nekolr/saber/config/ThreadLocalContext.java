package com.nekolr.saber.config;

import com.nekolr.saber.service.dto.UserDTO;

public class ThreadLocalContext {

    private final static ThreadLocal<UserDTO> threadLocal = new ThreadLocal<>();

    public static UserDTO getUser() {
        return threadLocal.get();
    }

    public static void setUser(UserDTO user) {
        threadLocal.set(user);
    }

    public static void removeUser() {
        threadLocal.remove();
    }
}
