package com.nekolr.saber.util;

import java.security.SecureRandom;
import java.util.Random;

/**
 * @author nekolr
 */
public class RandomUtils {

    /**
     * 获取随机字符串
     *
     * @param length    字符串的长度
     * @param useSecure 是否使用 SecureRandom
     * @return
     */
    public static String randomString(int length, boolean useSecure) {
        final String origin = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        final int len = origin.length();
        Random random;
        if (useSecure) {
            random = new SecureRandom();
        } else {
            random = new Random();
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(origin.charAt(random.nextInt(len)));
        }
        return builder.toString();
    }
}
