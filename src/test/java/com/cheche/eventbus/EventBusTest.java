package com.cheche.eventbus;

import com.alibaba.fastjson.JSON;
import com.cheche.eventbus.common.EventBus;
import com.cheche.eventbus.dto.TestDataDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author cheche
 * @date 2022/12/29
 */
@SpringBootTest(classes = EventBusApplication.class)
public class EventBusTest {

  @Autowired
  private EventBus eventBus;

  @Test
  public void eventBusTestStr() {
    eventBus.publish("hello bus","/rule-engine/device/message/product_01/device_01/rule_id");
  }

  @Test
  public void eventBusTestObject() {
    TestDataDTO testDataDTO = new TestDataDTO("1", "cheche", "nanjing");
//    eventBus.publish(testDataDTO,"/rule-engine/device/message/product_01/device_01/rule_id");
    eventBus.publish(JSON.toJSONString(testDataDTO),"/event/bus/test/test");
  }

}
