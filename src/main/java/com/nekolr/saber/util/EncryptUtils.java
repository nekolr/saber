package com.nekolr.saber.util;

import org.springframework.util.DigestUtils;

/**
 * @author nekolr
 */
public class EncryptUtils {

    /**
     * MD5 加密
     */
    public static String md5(String original) {
        return DigestUtils.md5DigestAsHex(original.getBytes());
    }
}
