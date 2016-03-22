package jp.mts.authaccess.app;

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
		"jp.mts.authaccess.config.base",
		"jp.mts.authaccess.config.event",
		"jp.mts.authaccess.infrastructure",
		"jp.mts.authaccess.application",
		"jp.mts.authaccess.mq.listener",
	})
public class BackendTask {

	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(BackendTask.class);
		springApplication.setWebEnvironment(false);
		springApplication.run(args);
	}
}
