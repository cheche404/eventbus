package com.cheche.eventbus.common;

import com.cheche.eventbus.annotation.Subscribe;
import com.cheche.eventbus.utils.EventBusUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * 注册表类
 *
 * @author cheche
 * @date 2022/12/28
 */
public class Registry {

  /**
   * 储存Subscriber(消息接收者)集合和topic之间的关系集合
   */
  private final ConcurrentHashMap<String, ConcurrentLinkedDeque<Subscriber>> subscriberContainer = new ConcurrentHashMap<>();

  /**
   * 绑定消息接收者
   * @param subscriber 消息接收者
   */
  public void bind(Object subscriber){
    //获取 消息接收者的添加@Subscrib注解方法集合 和消息接收者进行绑定
    List<Method> subscribeMethod = getSubscribeMethod(subscriber);
    subscribeMethod.forEach(m -> tierSubscriber(subscriber, m));
  }

  /**
   * 消息接收者解绑
   *
   * @param subscriber 消息接收者
   */
  public void unbind(Object subscriber){
    // 解绑为了提高速度 只对Subscriber进行失效操作
    subscriberContainer.forEach((key,queue) ->
      queue.forEach(s -> {
        if (s.getSubscribeObject() == subscriber){
          s.setDisable(true);
        }
      }));
  }

  /**
   * 根据topic 主题获取对应消息接收者队列  方法参数添加final 防止变量在方法体中被改变
   *
   * @param topic topic 名称
   * @return 订阅者队列
   */
  public ConcurrentLinkedDeque<Subscriber> scanSubscriber(final String topic){
    if (Objects.isNull(subscriberContainer.get(topic))) {
      for (Map.Entry<String, ConcurrentLinkedDeque<Subscriber>> entry : subscriberContainer.entrySet()) {
        if (EventBusUtils.isMatch(topic, entry.getKey())) {
          return subscriberContainer.get(entry.getKey());
        }
      }
    }
    return subscriberContainer.get(topic);
  }
  /**
   *  将绑定对象封装成Subscriber 注册到subscriberContainer的topic对应集合中
   */
  private void tierSubscriber(Object subscriber, Method method) {
    // 获取@Subscribe注解信息
    final Subscribe subscribe = method.getDeclaredAnnotation(Subscribe.class);
    String[] topics = subscribe.topic();
    for (String topic: topics) {
      // 当topic没有 就新建一个 有就添加到对应的集合中
      subscriberContainer.computeIfAbsent(topic, key-> new ConcurrentLinkedDeque<>());
      // 再将消息接收者添加到对应的 topic的集合中
      subscriberContainer.get(topic).add(new Subscriber(subscriber, method));
    }
  }

  /**
   * 查找绑定对象的方法是否添加 @Subscribe注解
   *
   * @param subscriber 订阅者
   * @return 方法集合
   */
  private List<Method> getSubscribeMethod(Object subscriber) {
    final List<Method> methods = new ArrayList<>();
    Class<?> temp = subscriber.getClass();
    // 不断的获取当前类和父类的所有添加 @Subscribe注解的方法
    while (temp != null) {
      // 获取当前类的所以方法
      Method[] declaredMethods = temp.getDeclaredMethods();
      // 只有public 方法 && 有一个入参 && 被@Subscribe注解 的方法才满足 是回调方法
      Arrays.stream(declaredMethods)
        .filter(m -> m.isAnnotationPresent(Subscribe.class)
          && (m.getParameterCount() == 1 || m.getParameterCount() == 2)
          && m.getModifiers() == Modifier.PUBLIC)
        .forEach(methods::add);
      // 取父类消息
      temp = temp.getSuperclass();
    }
    return methods;
  }

}
