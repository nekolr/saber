package com.nekolr.saber.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ThrowableUtils {

    /**
     * 获取异常的堆栈信息
     */
    public static String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)) {
            throwable.printStackTrace(pw);
            return sw.toString();
        }
    }
}
