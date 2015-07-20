package jp.mts.taskmanage;

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
		"jp.mts.taskmanage"
	})
public class RestApiServer {

	public static void main(String[] args) {
		SpringApplication.run(RestApiServer.class, args);
	}
}
