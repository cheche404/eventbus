package com.cheche.eventbus.annotation;

import java.lang.annotation.*;

/**
 * @author cheche
 * @date 2022/12/29
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RegistrySubscriber {

  String value() default "";

  String description() default "";

}
