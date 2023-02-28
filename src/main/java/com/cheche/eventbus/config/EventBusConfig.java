package com.cheche.eventbus.config;

import com.cheche.eventbus.annotation.RegistrySubscriber;
import com.cheche.eventbus.common.EventBus;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * 规则引擎 eventbus 配置类
 *
 * @author cheche
 * @date 2022/12/29
 */
@Configuration
public class EventBusConfig {
  private static final String RULE_ENGINE_EVENT_BUS_NAME = "eventbus";
  @Bean
  public EventBus eventBus(ApplicationContext applicationContext) {
    EventBus eventBus = new EventBus(RULE_ENGINE_EVENT_BUS_NAME);
    Map<String, Object> registrySubscriberMap = applicationContext.getBeansWithAnnotation(RegistrySubscriber.class);
    eventBus.autoRegister(registrySubscriberMap);
    return eventBus;
  }

}
