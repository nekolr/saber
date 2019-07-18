package com.nekolr.saber.util;

import cn.hutool.core.io.FileTypeUtil;

import java.io.*;

public class FileUtils {

    /**
     * 通过读取文件头的方式获取文件类型
     *
     * @param in 文件流
     * @return 文件类型
     */
    public static String getSuffix(InputStream in) {
        return FileTypeUtil.getType(in);
    }
}
