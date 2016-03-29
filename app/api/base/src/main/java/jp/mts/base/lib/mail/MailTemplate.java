package jp.mts.base.lib.mail;

import java.util.Map;

import jp.mts.libs.unittest.Maps;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;

public class MailTemplate {

	private VelocityEngine velocityEngine;
	private String templateResourceName;
	
	public MailTemplate(String templateResourceName, VelocityEngine velocityEngine) {
		this.templateResourceName = templateResourceName;
		this.velocityEngine = velocityEngine;
	}
	
	public String build(MailView view) {
		return VelocityEngineUtils.mergeTemplateIntoString(
				velocityEngine, 
				templateResourceName, 
				"UTF-8", 
				Maps.map("view", view));
	}

}
