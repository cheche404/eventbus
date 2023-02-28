### 自定义 eventbus 
```text
使用 spring-boot 框架封装的简单易用的 eventbus 消息发布订阅
```
使用 @Configuration 加载所有添加 @RegistrySubscriber 的实例
```java
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
```

使用说明:

- 发布消息
```java
@SpringBootTest(classes = EventBusApplication.class)
public class EventBusTest {
  
  @Test
  public void eventBusTestObject() {
    TestDataDTO testDataDTO = new TestDataDTO("1", "cheche", "nanjing");
    eventBus.publish(JSON.toJSONString(testDataDTO),"/event/bus/test/test");
  }
}
```
- 订阅消息
```java

@Service
@RegistrySubscriber // 此注解用于将订阅者注册
public class ReceiveMessageService {

  @Subscribe(topic = "/event/bus/*/*")
  public void receiveMessage(String message) {
    System.out.println(message);
  }

}

```