package com.cheche.eventbus.common;

import com.cheche.eventbus.exception.EventExceptionHandler;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Bus 的异步实现类
 *
 * @author cheche
 * @date 2022/12/28
 */
public class EventBusAsync extends EventBus {

  EventBusAsync(String busName, EventExceptionHandler exceptionHandler, ThreadPoolExecutor executor){
    super(busName,exceptionHandler,executor);
  }

  public EventBusAsync(String busName,ThreadPoolExecutor executor){
    this(busName,null,executor);
  }

  public EventBusAsync(ThreadPoolExecutor executor){
    this("default-async",null,executor);
  }

  public EventBusAsync(EventExceptionHandler exceptionHandler, ThreadPoolExecutor executor){
    this("default-async",exceptionHandler,executor);
  }

}