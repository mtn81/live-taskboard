package jp.mts.widgetstore.app;

import jp.mts.base.config.RestConfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableAspectJAutoProxy
@ComponentScan(
	basePackages={
		"jp.mts.base.config", 
		"jp.mts.base.aspect", 
		"jp.mts.widgetstore.config.base",
		"jp.mts.widgetstore.config.api",
		"jp.mts.widgetstore.infrastructure",
		"jp.mts.widgetstore.application",
		"jp.mts.widgetstore.rest",
		"jp.mts.widgetstore.websocket"
	})
public class ApiServer {

	public static void main(String[] args) {
		SpringApplication.run(ApiServer.class, args);
	}
}
