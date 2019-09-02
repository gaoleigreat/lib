package com.framework.common.utils;

import org.apache.commons.lang.StringUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @auther xiaodao
 * @date 2019/9/2 10:49
 */
public class FileUtil {
    /**
     * 关闭输入流
     *
     * @param is
     */
    public static void close(InputStream is) {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭输出流
     *
     * @param os
     */
    public static void close(OutputStream os) {
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取文件输入流 可以使url也可以是文件具体路径
     *
     * @param picPath
     * @return
     * @throws Exception
     */

    public static InputStream getFileInputStream(String picPath) throws Exception {
        if (StringUtils.isNotBlank(picPath) && (picPath.startsWith("http") || picPath.startsWith("https"))) {
            URL url = new URL(picPath);
            //打开链接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置请求方式为"GET"
            conn.setRequestMethod("GET");
            //超时响应时间为5秒
            conn.setConnectTimeout(5 * 1000);
            //通过输入流获取图片数据
            InputStream is = conn.getInputStream();
            return is;
        } else {
            InputStream is = new FileInputStream(picPath);
            return is;
        }
    }




}
