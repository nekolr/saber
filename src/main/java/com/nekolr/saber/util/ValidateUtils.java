package com.nekolr.saber.util;

import cn.hutool.core.lang.Validator;

public class ValidateUtils {

    /**
     * 判断是否是邮箱格式
     *
     * @param value
     * @return
     */
    public static boolean isEmail(CharSequence value) {
        return Validator.isEmail(value);
    }
}
