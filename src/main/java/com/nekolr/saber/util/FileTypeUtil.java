package com.nekolr.saber.util;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class FileTypeUtil {

    /**
     * 用于建立十六进制字符输出的小写字符数组
     */
    private static final char[] DIGITS_LOWER = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * 用于建立十六进制字符输出的大写字符数组
     */
    private static final char[] DIGITS_UPPER = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * 文件类型字典
     */
    private static final Map<String, String> fileTypeMap;

    static {
        fileTypeMap = new HashMap<>();

        fileTypeMap.put("ffd8ff", "jpg"); // JPEG (jpg)
        fileTypeMap.put("89504e47", "png"); // PNG (png)
        fileTypeMap.put("4749463837", "gif"); // GIF (gif)
        fileTypeMap.put("4749463839", "gif"); // GIF (gif)
        fileTypeMap.put("3C3F786D", "xml"); // XML
        fileTypeMap.put("49492a00227105008037", "tif"); // TIFF (tif)
        fileTypeMap.put("424d228c010000000000", "bmp"); // 16色位图 (bmp)
        fileTypeMap.put("424d8240090000000000", "bmp"); // 24位位图 (bmp)
        fileTypeMap.put("424d8e1b030000000000", "bmp"); // 256色位图 (bmp)
        fileTypeMap.put("41433130313500000000", "dwg"); // CAD (dwg)
        fileTypeMap.put("7b5c727466315c616e73", "rtf"); // Rich Text Format (rtf)
        fileTypeMap.put("38425053000100000000", "psd"); // Photoshop (psd)
        fileTypeMap.put("46726f6d3a203d3f6762", "eml"); // Email [Outlook Express 6] (eml)
        fileTypeMap.put("d0cf11e0a1b11ae10000", "doc"); // MS Excel 注意：word、msi 和 excel 的文件头一样
        fileTypeMap.put("d0cf11e0a1b11ae10000", "vsd"); // Visio 绘图
        fileTypeMap.put("5374616E64617264204A", "mdb"); // MS Access (mdb)
        fileTypeMap.put("252150532D41646F6265", "ps");
        fileTypeMap.put("255044462d312e", "pdf"); // Adobe Acrobat (pdf)
        fileTypeMap.put("2e524d46000000120001", "rmvb"); // rmvb/rm 相同
        fileTypeMap.put("464c5601050000000900", "flv"); // flv 与 f4v 相同
        fileTypeMap.put("00000020667479706d70", "mp4");
        fileTypeMap.put("49443303000000002176", "mp3");
        fileTypeMap.put("000001ba210001000180", "mpg");
        fileTypeMap.put("3026b2758e66cf11a6d9", "wmv"); // wmv 与 asf 相同
        fileTypeMap.put("52494646e27807005741", "wav"); // Wave (wav)
        fileTypeMap.put("52494646d07d60074156", "avi");
        fileTypeMap.put("4d546864000000060001", "mid"); // MIDI (mid)
        fileTypeMap.put("526172211a0700cf9073", "rar"); // WinRAR
        fileTypeMap.put("235468697320636f6e66", "ini");
        fileTypeMap.put("504B03040a0000000000", "jar");
        fileTypeMap.put("504B0304140008000800", "jar");
        fileTypeMap.put("D0CF11E0A1B11AE10", "xls"); // xls 文件
        fileTypeMap.put("504B0304", "zip");
        fileTypeMap.put("4d5a9000030000000400", "exe"); // 可执行文件
        fileTypeMap.put("3c25402070616765206c", "jsp"); // jsp 文件
        fileTypeMap.put("4d616e69666573742d56", "mf"); // MF 文件
        fileTypeMap.put("7061636b616765207765", "java"); // java 文件
        fileTypeMap.put("406563686f206f66660d", "bat"); // bat 文件
        fileTypeMap.put("1f8b0800000000000000", "gz"); // gz 文件
        fileTypeMap.put("cafebabe0000002e0041", "class"); // bat 文件
        fileTypeMap.put("49545346030000006000", "chm"); // bat 文件
        fileTypeMap.put("04000000010000001300", "mxp"); // bat 文件
        fileTypeMap.put("d0cf11e0a1b11ae10000", "wps"); // WPS 文字 wps、表格 et、演示 dps 都是一样的
        fileTypeMap.put("6431303a637265617465", "torrent"); // torrent 文件
        fileTypeMap.put("6D6F6F76", "mov"); // Quicktime (mov)
        fileTypeMap.put("FF575043", "wpd"); // WordPerfect (wpd)
        fileTypeMap.put("CFAD12FEC5FD746F", "dbx"); // Outlook Express (dbx)
        fileTypeMap.put("2142444E", "pst"); // Outlook (pst)
        fileTypeMap.put("AC9EBD8F", "qdf"); // Quicken (qdf)
        fileTypeMap.put("E3828596", "pwl"); // Windows Password (pwl)
        fileTypeMap.put("2E7261FD", "ram"); // Real Audio (ram)
    }

    /**
     * 获取文件类型
     *
     * @param in 输入流
     * @return 文件类型扩展名
     */
    public static String getType(InputStream in) {
        return getType(readHex28Upper(in));
    }

    /**
     * 根据文件流的头部信息获得文件类型
     *
     * @param fileStreamHexHead 文件流头部 16 进制字符串
     * @return 文件类型，未找到为 null
     */
    private static String getType(String fileStreamHexHead) {
        for (Map.Entry<String, String> fileTypeEntry : fileTypeMap.entrySet()) {
            if (StringUtils.startsWithIgnoreCase(fileStreamHexHead, fileTypeEntry.getKey())) {
                return fileTypeEntry.getValue();
            }
        }
        return null;
    }

    /**
     * 从流中读取前 28 个 byte 并转换为 16 进制，字母部分使用大写
     *
     * @param in 输入流
     * @return 16 进制字符串
     */
    private static String readHex28Upper(InputStream in) {
        return readHex(in, 28, false);
    }


    /**
     * 从流中读取指定长度的 byte 并转换为 16 进制
     *
     * @param in          输入流
     * @param length      长度
     * @param toLowerCase 是否转换成小写格式
     * @return 16 进制字符串
     */
    private static String readHex(InputStream in, int length, boolean toLowerCase) {
        return encodeHexStr(readBytes(in, length), toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
    }


    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param data
     * @param toDigits
     * @return
     */
    private static String encodeHexStr(byte[] data, char[] toDigits) {
        return new String(encodeHex(data, toDigits));
    }

    /**
     * 将字节数组转换为十六进制字符数组
     *
     * @param data     byte[]
     * @param toDigits 用于控制输出的 char[]
     * @return 十六进制 char[]
     */
    private static char[] encodeHex(byte[] data, char[] toDigits) {
        final int len = data.length;
        final char[] result = new char[len << 1]; // len*2
        // two characters from the hex value
        for (int i = 0, j = 0; i < len; i++) {
            result[j++] = toDigits[(0xF0 & data[i]) >>> 4]; // 高位
            result[j++] = toDigits[0x0F & data[i]]; // 低位
        }
        return result;
    }


    /**
     * 读取指定长度的 byte 数组，不关闭流
     *
     * @param in     输入流
     * @param length 长度，小于等于 0 返回空 byte 数组
     * @return bytes
     */
    private static byte[] readBytes(InputStream in, int length) {
        if (length <= 0) {
            return new byte[0];
        }
        byte[] b = new byte[length];
        int readLength;
        try {
            readLength = in.read(b);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (readLength > 0 && readLength < length) {
            byte[] b2 = new byte[length];
            System.arraycopy(b, 0, b2, 0, readLength);
            return b2;
        } else {
            return b;
        }
    }
}
