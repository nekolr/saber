package com.nekolr.saber.util;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    /**
     * 格式化返回当前日期，不包含时间
     */
    public static String currentDate() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    }

    /**
     * 获取以当前日期为格式的文件路径
     */
    public static String currentSaveDate() {
        String currentDate = currentDate();
        String[] dateSplits = currentDate.split("/");
        return dateSplits[0] + File.separator + dateSplits[1] + File.separator + dateSplits[2];
    }
}
