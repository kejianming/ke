package com.nfdw.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

/**
 * Created by user on 2018/7/12.
 */
public class Base64Utils {
    private static Logger logger = Logger.getLogger(Base64Utils.class);
    private static Base64 encoder = new Base64();

    public static String ioToBase64(InputStream in) throws IOException {
        String strBase64 = null;
        try {
            byte[] bytes = new byte[in.available()];
            // 将文件中的内容读入到数组中
            in.read(bytes);
            strBase64 = encoder.encodeToString(bytes);    //将字节流数组转换为字符串
            in.close();
        } catch (IOException ioe) {
            logger.error("图片转64编码异常",ioe);
        }
        return strBase64;
    }
}
