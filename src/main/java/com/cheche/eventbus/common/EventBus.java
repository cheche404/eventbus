package com.cheche.eventbus.common;

import com.cheche.eventbus.exception.EventExceptionHandler;

import java.util.Map;
import java.util.concurrent.Executor;

/**
 * Bus的同步推送实现类
 *
 * @author cheche
 * @date 2022/12/28
 */
public class EventBus implements Bus {

  /**
   * 用于维护Subscriber 消息接收者的 注册表
   */
  private final Registry registry = new Registry();

  /**
   * Event Bus 的名称
   */
  private String busName;

  /**
   * Event Bus默认值
   */
  private final static String DEFAULT_BUS_NAME = "default";

  /**
   * topic的默认值
   */
  private final static String DEFAULT_TOPIC = "default";

  /**
   * 分发消息到消息接收者的处理器
   */
  private final Dispatcher dispatcher;

  public EventBus(){
    this(DEFAULT_BUS_NAME,null, Dispatcher.SEQ_EXECUTOR_SERVICE);
  }

  public EventBus(String busName){
    this(busName,null, Dispatcher.SEQ_EXECUTOR_SERVICE);
  }

  EventBus(String busName, EventExceptionHandler exceptionHandler, Executor executor){
    this.busName = busName;
    this.dispatcher = Dispatcher.newDispatcher(exceptionHandler, executor);
  }

  public EventBus(EventExceptionHandler exceptionHandler){
    this(DEFAULT_BUS_NAME,exceptionHandler, Dispatcher.SEQ_EXECUTOR_SERVICE);
  }

  /**
   * 注册消息接收者的动作交给 registry
   *
   * @param subscriber 消息接收者
   */
  @Override
  public void register(Object subscriber) {
    this.registry.bind(subscriber);
  }
  @Override
  public void unregister(Object subscriber) {
    this.registry.unbind(subscriber);
  }

  /**
   * 通过处理器将消息发送给消息接收者
   *
   * @param event event
   * @param topic topic
   */
  @Override
  public void publish(Object event, String topic) {
    this.dispatcher.dispatch(this, registry, event, topic);
  }

  @Override
  public void publish(Object event) {
    this.publish(event, DEFAULT_TOPIC);
  }

  /**
   * 关闭处理器
   */
  @Override
  public void close() {
    this.dispatcher.close();
  }

  @Override
  public String getBusName() {
    return this.busName;
  }

  @Override
  public void autoRegister(Map<String, Object> registrySubscriberMap) {
    for (Map.Entry<String, Object> entry : registrySubscriberMap.entrySet()) {
      this.register(entry.getValue());
    }
  }
}
