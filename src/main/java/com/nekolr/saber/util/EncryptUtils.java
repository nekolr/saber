package com.nekolr.saber.util;

import cn.hutool.crypto.SecureUtil;

/**
 * @author nekolr
 */
public class EncryptUtils {

    /**
     * MD5 加密
     *
     * @param original
     * @return
     */
    public static String md5(String original) {
        return SecureUtil.md5(original);
    }
}
