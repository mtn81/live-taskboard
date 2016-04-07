package jp.mts.authaccess.app;

import jp.mts.base.config.CacheConfig;
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
		"jp.mts.authaccess.config.base",
		"jp.mts.authaccess.infrastructure",
		"jp.mts.authaccess.application",
		"jp.mts.authaccess.rest",
	})
public class ApiServer {

	public static void main(String[] args) {
		SpringApplication.run(ApiServer.class, args);
	}
}
