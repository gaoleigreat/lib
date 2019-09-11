package com.framework.mybatis.tool;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : yanglf
 * @version : 1.0
 * @created IntelliJ IDEA.
 * @date : 2019/9/10 17:54
 * @desc :
 */
public class WhereEntityTool {

    private static Map<WhereTypeEnum, WhereFun> typeFunc;

    static {
        if (typeFunc == null) {
            typeFunc = new HashMap<>();
            typeFunc.put(WhereTypeEnum.EQ, (w, k, v) -> {
                w.eq(k, v);
            });
            typeFunc.put(WhereTypeEnum.NEQ, (w, k, v) -> {
                w.ne(k, v);
            });
            typeFunc.put(WhereTypeEnum.IN, (w, k, v) -> {
                if (v instanceof Collection) {
                    w.in(k, (Collection<?>) v);
                } else if (v instanceof Object[]) {
                    w.in(k, (Object[]) v);
                } else {
                    w.in(k, v.toString());
                }
            });
            typeFunc.put(WhereTypeEnum.LIKE, (w, k, v) -> {
                w.like(k, v.toString());
            });
            typeFunc.put(WhereTypeEnum.LE, (w, k, v) -> {
                w.le(k, v);
            });
            typeFunc.put(WhereTypeEnum.LT, (w, k, v) -> {
                w.lt(k, v);
            });
            typeFunc.put(WhereTypeEnum.GE, (w, k, v) -> {
                w.ge(k, v);
            });
            typeFunc.put(WhereTypeEnum.GT, (w, k, v) -> {
                w.gt(k, v);
            });
        }

    }

    /**
     * 封装成需要的wrapper
     *
     * @param t 实体对象
     * @return
     */
    public static Wrapper invoke(Object t) {
        QueryWrapper wrapper = new QueryWrapper();
        excute(t, wrapper);
        // 获取
        return wrapper;
    }

    public static void invoke(Object t, QueryWrapper wrapper) {
        excute(t, wrapper);
    }

    /**
     * 执行
     *
     * @param t       obj
     * @param wrapper
     */
    public static void excute(Object t, QueryWrapper wrapper) {
        //反射获取属性
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object val = field.get(t);
                String colum = "";
                if (val != null && !"".equals(val.toString())) {
                    WhereType whereType = field.getAnnotation(WhereType.class);
                    //没有注解，取默认为下划线拼接
                    if (whereType == null) {
                        colum = camelToUnderline(field.getName());
                        // 执行方法
                        typeFunc.get(WhereTypeEnum.EQ).whereFunc(wrapper, colum, val);
                    } else {
                        if (whereType.ignore()) {
                            continue;
                        } else {
                            //没有定义查询属性，取默认
                            if (!StringUtils.isBlank(whereType.filed())) {
                                colum = whereType.filed();
                            } else {
                                colum = camelToUnderline(field.getName());
                            }
                            // 是否另取一个where
                           /* if (whereType.andNew()) {
                                wrapper.andNew();
                            }*/
                            // 执行方法
                            typeFunc.get(whereType.type()).whereFunc(wrapper, colum, val);
                        }
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param param
     * @return
     */
    public static String camelToUnderline(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append("_");
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
