package com.cheche.eventbus.exception;

import com.cheche.eventbus.common.EventContext;

/**
 * event 异常回调接口
 *
 * @author cheche
 * @date 2022/12/28
 */
public interface EventExceptionHandler {

  /**
   * 异常回调方法
   * @param cause cause
   * @param context context
   */
  void handle(Throwable cause, EventContext context);

}
