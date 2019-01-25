package com.survey.lib.common.utils;
import com.survey.lib.common.vo.HeaderVo;
import org.springframework.util.StringUtils;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.Map;

/**
 * @author yanglf
 * @description
 * @since 2018/12/22
 **/
public class HttpUtils {


    /**
     * @param request
     * @return
     */
    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (!StringUtils.isEmpty(ip)) {
            ip = ip.split(",")[0];
        }
        return ip;
    }

    /**
     * @param request
     * @return
     */
    public static String getRequestParams(HttpServletRequest request) {
        Map<String, String[]> paramMap = request.getParameterMap();
        StringBuilder sb = new StringBuilder();
        int k = 0;
        for (String key : paramMap.keySet()) {
            if (k != 0) {
                sb.append(", ");
            }
            sb.append(key).append(" = ");
            String[] vals = paramMap.get(key);
            for (int i = 0; i < vals.length; i++) {
                String val = vals[i];
                if (i != 0) {
                    sb.append(", ");
                }
                sb.append(val);
            }
            sb.append(" ");
            k++;
        }
        return sb.toString();
    }


    /**
     * requestBody
     *
     * @param request
     * @return
     */
    public static String getRequestBody(HttpServletRequest request) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder("");
        try {
            br = request.getReader();
            String str;
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }


    public static String getHeaderVo(HttpServletRequest request) {
        HeaderVo headerVo = HeaderUtils.parseHeader(request);
        if (headerVo == null) {
            return null;
        }
        return headerVo.toJsonObject();

    }


    public static String getRequestHeaders(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        StringBuilder sb = new StringBuilder();
        int k = 0;
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            if (k != 0) {
                sb.append(", ");
            }
            sb.append(headerName).append("=[");
            String headerValue = request.getHeader(headerName);
            sb.append(headerValue);
            sb.append("]");
            k++;
        }
        return sb.toString();

    }

    public static  String getBodyString(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        InputStream inputStream = null;
        BufferedReader reader = null;
        try {
            inputStream = request.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString().trim();
    }
}
