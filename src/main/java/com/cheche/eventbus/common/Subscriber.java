package com.cheche.eventbus.common;

import java.lang.reflect.Method;

/**
 * 封装对象实例和@Subscribe注解标记的方法，一个对象实例可能包含多个Subscriber(消息接收者)
 *
 * @author cheche
 * @date 2022/12/28
 */
public class Subscriber {

  /**
   * 实例对象
   */
  private final Object subscribeObject;

  /**
   * 注解方法
   */
  private final Method subscribeMethod;

  /**
   * 消息接收者是否失效  false代表没有失效  true 代表失效
   */
  private boolean disable = false;

  public Subscriber(Object subscribeObject, Method subscribeMethod) {
    this.subscribeObject = subscribeObject;
    this.subscribeMethod = subscribeMethod;
  }

  public Object getSubscribeObject() {
    return subscribeObject;
  }

  public Method getSubscribeMethod() {
    return subscribeMethod;
  }

  public boolean isDisable() {
    return disable;
  }

  public void setDisable(boolean disable) {
    this.disable = disable;
  }

}
