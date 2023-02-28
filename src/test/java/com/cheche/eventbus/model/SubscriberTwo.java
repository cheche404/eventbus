package com.cheche.eventbus.model;

import com.alibaba.fastjson.JSON;
import com.cheche.eventbus.annotation.RegistrySubscriber;
import com.cheche.eventbus.annotation.Subscribe;
import com.cheche.eventbus.dto.TestDataDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author cheche
 * @date 2022/12/28
 */
@Slf4j
@Component
@RegistrySubscriber
public class SubscriberTwo {

  @Subscribe
  public void method1(String event){
    System.out.println("Two-通过默认topic传递过来的消息为：" + event);
  }

  @Subscribe(topic = "test/abc/*/*")
  public void method2(String event){
    log.info("Two-通过 method2 这个topic传递过来的消息为:[{}]", event);
  }

  @Subscribe(topic = "/rule-engine/device/message/**")
  public void method3(String event){
    log.info("Two-通过 method3 这个topic传递过来的消息为:[{}]", event);
  }

  @Subscribe(topic = {"/rule-engine/device/message/**", "/event/bus/test"})
  public void method4(TestDataDTO event){
    log.info("Two-通过 method4 这个topic传递过来的消息为:[{}]", JSON.toJSONString(event));
  }

}
