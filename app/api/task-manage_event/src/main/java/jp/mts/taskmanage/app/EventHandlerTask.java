package jp.mts.taskmanage.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableScheduling
@EnableAsync
@ComponentScan(
	basePackages={
		"jp.mts.base.config", 
		"jp.mts.taskmanage.config.base",
		"jp.mts.taskmanage.config.event",
		"jp.mts.taskmanage.infrastructure",
		"jp.mts.taskmanage.application",
		"jp.mts.taskmanage.mq.listener",
	})
public class EventHandlerTask {

	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(EventHandlerTask.class);
		springApplication.setWebEnvironment(false);
		springApplication.run(args);
	}
}
