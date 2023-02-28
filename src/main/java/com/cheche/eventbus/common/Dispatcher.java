package com.cheche.eventbus.common;

import com.cheche.eventbus.exception.EventExceptionHandler;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

/**
 * 分发器 Dispatcher
 * 将消息(Event) 推送到每个注册到topic上的消息接收者(Subscriber)上
 *
 * @author cheche
 * @date 2022/12/28
 */
public class Dispatcher {

  /**
   * 线程池
   */
  private final Executor executorService;

  /**
   * 异常回调接口
   */
  private final EventExceptionHandler exceptionHandler;

  /**
   * 顺序执行的ExecutorService  线程池  静态内部类实现单例
   */
  public static final Executor SEQ_EXECUTOR_SERVICE = SeqExecutorService.INSTANCE;

  /**
   * 每个消息推送都开辟一个线程执行的 线程池 holder方式实现
   */
  public static final Executor PRE_THREAD_EXECUTOR_SERVICE = PreThreadExecutorService.INSTANCE;

  private Dispatcher(Executor executorService, EventExceptionHandler exceptionHandler) {
    this.executorService = executorService;
    this.exceptionHandler = exceptionHandler;
  }

  /**
   * 将消息分发分发给 订阅topic的 消息接收者 并执行关联的方法
   * @param bus bus
   * @param registry registry
   * @param event event
   * @param topic topic
   */
  public void dispatch(Bus bus, Registry registry, Object event, String topic){
    // 根据topic获取所有的Subscriber（消息接收者）消息
    ConcurrentLinkedDeque<Subscriber> subscribers = registry.scanSubscriber(topic);
//    if (Objects.isNull(subscribers)) {
//      subscribers = registry.scanSubscriber(EventBusUtils.topicNameExpand(topic));
//    }
    // 当没有消息接收者时
    if (Objects.isNull(subscribers)){
      if(Objects.nonNull(exceptionHandler)){
        exceptionHandler.handle(new IllegalArgumentException("这个topic没有绑定消息接收者"),
          new BaseEventContext(bus.getBusName(), null, event));
      }
      return;
    }

    // 遍历所有方法 并通过反射方式进行方法调用
    subscribers.stream()
      .filter(subscriber -> !subscriber.isDisable())
      .filter(subscriber -> {
        Method subscribeMethod = subscriber.getSubscribeMethod();
        // 获取方法的第一个参数（为返回值）的class消息
        Class<?> aClass = subscribeMethod.getParameterTypes()[0];
        // 是否可以转化为event的类型
        return (aClass.isAssignableFrom(event.getClass()));
      }).forEach(subscriber -> realInvokeSubscribe(subscriber, event, bus));
  }

  /**
   * 用线程池使用Runnable执行对应回调方法
   *
   * @param subscriber subscriber
   * @param event event
   * @param bus bus
   */
  private void realInvokeSubscribe(Subscriber subscriber, Object event, Bus bus) {
    Method subscribeMethod = subscriber.getSubscribeMethod();
    Object subscribeObject = subscriber.getSubscribeObject();
    executorService.execute(() -> {
      try{
        subscribeMethod.invoke(subscribeObject, event);
      }catch (Exception e){
        // runnable逻辑执行单元不能抛出异常 要使用异常回到接口处理异常
        if (null != exceptionHandler){
          exceptionHandler.handle(e,new BaseEventContext(bus.getBusName(), subscriber, event));
        }
      }
    });
  }

  /**
   * 关闭方法
   */
  public void close(){
    if(executorService instanceof ExecutorService){
      ((ExecutorService) executorService).shutdown();
    }
  }

  /**
   * 外部调用方法
   */
  static Dispatcher newDispatcher(EventExceptionHandler exceptionHandler,Executor executor){
    return new Dispatcher(executor,exceptionHandler);
  }
  static Dispatcher seqDispatcher(EventExceptionHandler exceptionHandler){
    return new Dispatcher(SEQ_EXECUTOR_SERVICE,exceptionHandler);
  }
  static Dispatcher perThreadDispatcher(EventExceptionHandler exceptionHandler){
    return new Dispatcher(PRE_THREAD_EXECUTOR_SERVICE,exceptionHandler);
  }

  /**
   * 默认的EventContext实现
   */
  private static class BaseEventContext implements EventContext {

    /**
     * 消息中间件名称
     */
    private final String eventBusName;

    /**
     * 消息接收者
     */
    private final Subscriber subscriber;

    /**
     * 消息
     */
    private final Object event;

    private BaseEventContext(String eventBusName, Subscriber subscriber, Object event) {
      this.eventBusName = eventBusName;
      this.subscriber = subscriber;
      this.event = event;
    }

    @Override
    public String getSource() {
      return this.eventBusName;
    }

    @Override
    public Object getSubscriber() {
      return subscriber != null ? subscriber.getSubscribeObject() : null;
    }

    @Override
    public Method getSubscribe() {
      return subscriber != null ? subscriber.getSubscribeMethod() : null;
    }

    @Override
    public Object getEvent() {
      return this.event;
    }
  }

  /**
   * 每个消息推送都开辟一个线程执行的
   */
  private static class PreThreadExecutorService implements Executor{
    private final static PreThreadExecutorService INSTANCE = new PreThreadExecutorService();

    @Override
    public void execute(Runnable command) {
      new Thread(command).start();
    }
  }

  /**
   * 顺序执行的ExecutorService
   */
  private static class SeqExecutorService implements Executor {
    private final static SeqExecutorService INSTANCE = new SeqExecutorService();

    @Override
    public void execute(Runnable command) {
      command.run();
    }
  }

}
