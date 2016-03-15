package jp.mts.widgetstore;

import jp.mts.widgetstore.infrastructure.cooperation.TaskManageApi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class CooperationConfig {
	@Autowired
	private Settings settings;
	
	@Bean
	public TaskManageApi taskManageApi() {
		return new TaskManageApi(settings.taskManageUrl);
	}

	@Component
	@ConfigurationProperties("cooperation")
	public static class Settings {
		private String taskManageUrl;

		public String getTaskManageUrl() {
			return taskManageUrl;
		}
		public void setTaskManageUrl(String taskManageUrl) {
			this.taskManageUrl = taskManageUrl;
		}
	}
}
