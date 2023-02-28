package com.cheche.eventbus.annotation;

import java.lang.annotation.*;

/**
 * 注册对象给Event Bus的时候需要指定接收消息时的回调方法
 * 采用注解的方式执行回调方法
 *
 * @author cheche
 * @date 2022/12/28
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Subscribe {

  /**
   * 设置topic信息
   * @return topic 名称
   */
  String[] topic() default "";

}
