package com.nekolr.saber.util;

import java.util.Random;

/**
 * @author nekolr
 */
public class RandomUtils {

    private static final char[] ORIGINAL_CHARS = {
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    /**
     * 获取随机字符串
     *
     * @param length 字符串的长度
     * @return
     */
    public static String randomString(int length) {
        final int len = ORIGINAL_CHARS.length;
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(ORIGINAL_CHARS[random.nextInt(len)]);
        }
        return builder.toString();
    }
}
