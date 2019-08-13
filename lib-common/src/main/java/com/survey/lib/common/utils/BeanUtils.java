package com.survey.lib.common.utils;

import java.lang.reflect.Field;

/**
 * @author yanglf
 * @description
 * @since 2019/8/13
 **/
public class BeanUtils {

    /**
     * @param obj
     * @return
     * @throws Exception
     * @desc 判断该对象是否: 返回ture表示所有属性为null  返回false表示不是所有属性都是null
     */
    public static boolean isAllFieldNull(Object obj) throws Exception {
        Class stuCla = obj.getClass();
        Field[] fs = stuCla.getDeclaredFields();
        boolean flag = true;
        for (Field f : fs) {
            // 设置属性是可以访问的(私有的也可以)
            f.setAccessible(true);
            Object val = f.get(obj);
            if (val != null) {
                flag = false;
                break;
            }
        }
        return flag;
    }


}
