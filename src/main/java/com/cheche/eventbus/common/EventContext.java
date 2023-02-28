package com.cheche.eventbus.common;

import java.lang.reflect.Method;

/**
 * 消息上下文接口
 *
 * @author cheche
 * @date 2022/12/28
 */
public interface EventContext {

  /**
   * 获取消息源
   *
   * @return 消息源
   */
  String getSource();

  /**
   * 获取消息接收者
   *
   * @return 消息接收者
   */
  Object getSubscriber();

  /**
   * 获取接收消息时的回调方法
   *
   * @return 回调方法
   */
  Method getSubscribe();

  /**
   * 获取消息
   *
   * @return 消息
   */
  Object getEvent();

}
