package jp.mts.taskmanage.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
@ComponentScan(
	basePackages={
		"jp.mts.base.config", 
		"jp.mts.taskmanage.config.base",
		"jp.mts.taskmanage.config.api",
		"jp.mts.taskmanage.infrastructure",
		"jp.mts.taskmanage.application",
		"jp.mts.taskmanage.rest",
		"jp.mts.taskmanage.websocket"
	})
public class ApiServer {

	public static void main(String[] args) {
		SpringApplication.run(ApiServer.class, args);
	}
}
