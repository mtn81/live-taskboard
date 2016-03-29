package jp.mts.base.config;

import java.io.IOException;

import javax.annotation.PostConstruct;

import jp.mts.base.lib.mail.MailView;
import jp.mts.libs.unittest.Maps;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.ui.velocity.VelocityEngineFactoryBean;

@Configuration
public class MailConfig {

	@Autowired
	private Settings settings;

	@PostConstruct
	public void initDomain() {
		MailView.setSiteBaseUrl(settings.getSiteBaseUrl());
	}

	@Bean
	@Qualifier("mail")
	public VelocityEngine mailVelocityEngine() throws VelocityException, IOException {
		VelocityEngineFactoryBean factory = new VelocityEngineFactoryBean();
		factory.setResourceLoaderPath("classpath:mail_templates");
		factory.setVelocityPropertiesMap(new Maps()
			.e("input.encoding", "UTF-8")
			.e("output.encoding", "UTF-8")
			.get());
		return factory.createVelocityEngine();
	}

	@Component
	@ConfigurationProperties("mail")
	public static class Settings {
		private String siteBaseUrl;

		public String getSiteBaseUrl() {
			return siteBaseUrl;
		}
		public void setSiteBaseUrl(String siteBaseUrl) {
			this.siteBaseUrl = siteBaseUrl;
		}
	}
}
