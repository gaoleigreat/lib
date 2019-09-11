package com.framework.mybatis.tool;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author : yanglf
 * @version : 1.0
 * @created IntelliJ IDEA.
 * @date : 2019/9/10 17:57
 * @desc :
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface WhereType {

    /**
     * <p>
     * 查询类型
     * </p>
     */
    WhereTypeEnum type() default WhereTypeEnum.EQ;

    /**
     * 判定字段名
     *
     * @return
     */
    String filed() default "";

    /**
     * 是否忽略
     */
    boolean ignore() default false;

    boolean andNew() default false;

}
