package com.irengine.wechat.connector;

import java.io.IOException;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class ApplicationBootstrap implements ApplicationListener<ContextRefreshedEvent> {

	  /*
	   * This method is called during Spring's startup.
	   * 
	   * @param event Event raised when an ApplicationContext gets initialized or
	   * refreshed.
	   * 
	   * http://stackoverflow.com/questions/6684451/executing-a-java-class-at-application-startup-using-spring-mvc
	   * http://stackoverflow.com/questions/22389996/how-to-configure-spring-boot-servlet-like-in-web-xml
	   */
	  @Override
	  public void onApplicationEvent(final ContextRefreshedEvent event) {

		System.out.println("===============Bootstrap===============");
		try {
			WeChatConnector.init();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("==================end==================");
		return;
	  }
}
