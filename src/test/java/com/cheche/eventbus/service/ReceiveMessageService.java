package com.cheche.eventbus.service;

import com.cheche.eventbus.annotation.RegistrySubscriber;
import com.cheche.eventbus.annotation.Subscribe;
import org.springframework.stereotype.Service;

/**
 * @author cheche
 * @date 2023/1/12
 */
@Service
@RegistrySubscriber
public class ReceiveMessageService {

  @Subscribe(topic = "/event/bus/*/*")
  public void receiveMessage(String message) {
    System.out.println(message);
  }

}
