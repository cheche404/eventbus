package com.cheche.eventbus.model;

import com.cheche.eventbus.annotation.RegistrySubscriber;
import com.cheche.eventbus.annotation.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author cheche
 * @date 2022/12/28
 */
@Slf4j
@Component
@RegistrySubscriber
public class SubsciberOne {

  @Subscribe
  public void method1(String event){
    log.info("One-1--通过默认topic传递过来的消息为:[{}]", event);
  }

  @Subscribe
  public void method3(String event){
    log.info("One-2--通过默认topic传递过来的消息为:[{}]", event);
  }

  @Subscribe(topic = "method2")
  public void method2(String event){
    log.info("One-通过 method2 这个topic传递过来的消息为:[{}]", event);
  }

}
