package com.lego.survey.lib.log.annotation;

import java.lang.annotation.*;

/**
 * @author yanglf
 * @description
 * @since 2018/12/21
 **/

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {

    String value() default "";

    boolean requared() default true;

}
