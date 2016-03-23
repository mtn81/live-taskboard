package jp.mts.widgetstore.app;

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
		"jp.mts.widgetstore.config.base",
		"jp.mts.widgetstore.config.event",
		"jp.mts.widgetstore.infrastructure",
		"jp.mts.widgetstore.application",
		"jp.mts.widgetstore.mq.listener",
	})
public class BackendTask {

	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(BackendTask.class);
		springApplication.setWebEnvironment(false);
		springApplication.run(args);
	}
}
