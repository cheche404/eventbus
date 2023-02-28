package com.cheche.eventbus;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.net.UnknownHostException;

/**
 * @author cheche
 * @date 2023/1/12
 */
@Slf4j
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class}, scanBasePackages = {"com.cheche.eventbus"})
public class EventBusApplication {

  public static void main(String[] args) throws UnknownHostException {
    SpringApplication.run(EventBusApplication.class, args);
  }

}
