package com.framework.mybatis.utils;

import com.framework.common.utils.DateUtils;
import org.springframework.util.StringUtils;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author : yanglf
 * @version : 1.0
 * @created IntelliJ IDEA.
 * @date : 2019/9/3 18:45
 * @desc :
 */
public class TableUtils {

    /**
     * 类型转换
     *
     * @param category
     * @return
     */
    public static String getColumnType(Integer category) {
        switch (category) {
            case 1:
                // input
            case 2:
                // textarea
            case 4:
                // 图片
            case 5:
                // 附件
            case 7:
                // 多选
                return "VARCHAR(256)";
            case 3:
                // date
                return "TIMESTAMP";
            case 9:
                // 整数
            case 6:
                // 单选
                return "BIGINT";
            case 14:
                // 小数
                return "DOUBLE(255, 5)";
            default:
                break;
        }
        return null;
    }


    public static boolean isColumnType(Integer category) {
        switch (category) {
            case 1:
                // input
            case 2:
                // textarea
            case 4:
                // 图片
            case 5:
                // 附件
            case 7:
                // 多选
            case 3:
                // date
            case 9:
                // 整数
            case 6:
                // 单选
            case 14:
                // 小数
                return true;
            default:
                break;
        }
        return false;
    }


    /**
     * 类型转换   string  转换   object
     *
     * @param category
     * @return
     */
    public static Object getColumnValue(Integer category, String value) {
        switch (category) {
            case 1:
                // input
            case 2:
                // textarea
            case 4:
                // 图片
            case 5:
                // 附件
            case 7:
                // 多选
                return value;
            case 3:
                // date
                LocalDateTime localDateTime = DateUtils.parseStringToDateTime(value, DateUtils.HTML_DATETIME_PATTERN);
                return DateUtils.localDateTimeToDate(localDateTime);
            case 9:
                // 整数
            case 6:
                // 单选
                return Long.valueOf(value);
            case 14:
                // 小数
                return Double.valueOf(value);
            default:
                break;
        }
        return null;
    }


    /**
     * 类型转换  object 转换   string
     *
     * @param category
     * @return
     */
    public static String getColumnValueStr(Integer category, Object value) {
        switch (category) {
            case 1:
                // input
            case 2:
                // textarea
            case 4:
                // 图片
            case 5:
                // 附件
            case 7:
                // 多选
                return String.valueOf(value);
            case 3:
                // date
                Date data = (Date) value;
                return DateUtils.date2String(data, DateUtils.EXCEL_DATETIME_PATTERN);
            case 9:
                // 整数
            case 6:
                // 单选
                return String.valueOf(value);
            case 14:
                // 小数
                return String.valueOf(value);
            default:
                break;
        }
        return null;
    }


    /**
     * 是否为空
     *
     * @param isRequired
     * @return
     */
    public static String getIsNull(Integer isRequired) {
        if (isRequired != null && isRequired == 2) {
            return "NOT NULL";
        }
        return null;
    }

    /**
     * 获取默认值
     *
     * @param defaultValue
     * @return
     */
    public static String getDefaultValue(String defaultValue) {
        if (StringUtils.isEmpty(defaultValue)) {
            return null;
        }
        return "DEFAULT " + defaultValue;
    }

    /**
     * 获取描述信息
     *
     * @param desc
     * @return
     */
    public static String getComment(String desc) {
        if (StringUtils.isEmpty(desc)) {
            return "COMMENT ''";
        }
        return "COMMENT '" + desc + "'";
    }
}
