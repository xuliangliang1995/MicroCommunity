package com.java110.utils.util;

import org.apache.commons.net.util.Base64;

import java.io.*;

public class Base64Convert {

    private void Base64Convert() {

    }

    /**
     * 流转换为字符串
     *
     * @param in
     * @return
     * @throws IOException
     */
    public static String ioToBase64(InputStream in) throws IOException {
        String strBase64 = null;
        try {
            // in.available()返回文件的字节长度
            byte[] bytes = new byte[in.available()];
            // 将文件中的内容读入到数组中
            in.read(bytes);
            strBase64 = Base64.encodeBase64String(bytes);
        } finally {
            if (in != null) {
                in.close();
            }
        }

        return strBase64;
    }


    /**
     * 将base64 转为字节
     *
     * @param strBase64
     * @return
     * @throws IOException
     */
    public static byte[] base64ToByte(String strBase64) throws IOException {
        // 解码，然后将字节转换为文件
        byte[] bytes = Base64.decodeBase64(strBase64);
        return bytes;
    }
}
