package com.cheche.eventbus.common;

import java.util.Map;

/**
 * Bus
 * post方法用来发送Event(消息)
 * register方法用来注册Subscriber(消息接收者)
 *
 * @author cheche
 * @date 2022/12/28
 */
public interface Bus {

  /**
   * 将某个对象注册到Bus上，从此之后该类型成为 Subscriber
   */
  void register(Object subscriber);

  /**
   * 将某个对象从Bus上取消注册，之后就不会再接收来自Bus的消息
   */
  void unregister(Object subscriber);

  /**
   * 提交Event 到指定的 topic
   */
  void publish(Object event,String topic);

  /**
   * 提交Event 到默认的 topic
   */
  void publish(Object event);

  /**
   * 关闭bus
   */
  void close();

  /**
   * 获取Bus的标志名称
   */
  String getBusName();

  /**
   * 自动注册
   *
   * @param registrySubscriberMap 有 RegistrySubscriber 注解的所有类
   */
  void autoRegister(Map<String, Object> registrySubscriberMap);

}
