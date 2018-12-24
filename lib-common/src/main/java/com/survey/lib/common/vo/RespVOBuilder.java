package com.survey.lib.common.vo;

import com.survey.lib.common.consts.RespConsts;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
/**
 * @author yanglf
 * @description
 * @since 2018/12/22
 **/
public class RespVOBuilder {

    /**
     * 成功
     *
     * @param <T>
     * @return
     */
    public static <T> RespVO<T> success() {
        return new RespVO<>(RespConsts.SUCCESS, "请求成功", (T) new HashMap(0));
    }

    /**
     * 成功
     *
     * @param info
     * @param <T> POJO
     * @return
     */
    public static <T> RespVO<T> success(T info) {
        return new RespVO<>(RespConsts.SUCCESS, "请求成功", info);
    }

    /**
     * 成功
     * @param info
     * @param <T> List
     * @return
     */
    public static <T> RespVO<RespDataVO<T>> success(List<T> info) {
        return new RespVO<>(RespConsts.SUCCESS, "请求成功", new RespDataVO<>(info));
    }

    /**
     * 失败
     *
     * @param <T>
     * @return
     */
    public static <T> RespVO<T> failure() {
        return failure(RespConsts.Failure.class);
    }

    /**
     * 失败
     *
     * @param msg
     * @param <T>
     * @return
     */
    public static <T> RespVO<T> failure(String msg) {
        return new RespVO<>(RespConsts.FAILURE, msg, (T) new HashMap(0));
    }

    /**
     * 失败
     *
     * @param code
     * @param msg
     * @param <T>
     * @return
     */
    public static <T> RespVO<T> failure(String code, String msg) {
        return new RespVO<>(code, msg, (T) new HashMap<>(0));
    }

    /**
     * 失败并返回数据
     * @param msg
     * @param info
     * @param <T>
     * @return
     */
    public static <T> RespVO<T> failureData(String msg, T info) {
        return new RespVO<>(RespConsts.FAILURE, msg, info);
    }

    /**
     * 失败
     *
     * @param clazz *Consts中定义的接口
     * @param <T>
     * @return
     */
    public static <T> RespVO<T> failure(Class clazz) {
        return failure(clazz, (T) new HashMap<>(0));
    }

    /**
     * 失败
     *
     * @param clazz *Consts中定义的接口
     * @param info  需要返回的业务数据
     * @param <T>
     * @return
     */
    public static <T> RespVO<T> failure(Class clazz, T info) {
        String ret = null;
        String msg = null;
        Field[] fields = clazz.getDeclaredFields();
        try {
            for (Field field : fields) {
                if (field.getName().equals("ret")) {
                    ret = String.valueOf(field.get(clazz));
                }
                if (field.getName().equals("msg")) {
                    msg = String.valueOf(field.get(clazz));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new RespVO<>(ret, msg, info);
    }
}
