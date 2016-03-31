package jp.mts.base.lib.mail;

import java.util.Map;

import jp.mts.libs.unittest.Maps;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.velocity.VelocityEngineUtils;

public class MailTemplate {

	private VelocityEngine velocityEngine;
	private String templateResourceName;
	private String subject;
	
	public MailTemplate(
			String templateResourceName, 
			String subject, 
			VelocityEngine velocityEngine) {
		this.templateResourceName = templateResourceName;
		this.subject = subject;
		this.velocityEngine = velocityEngine;
	}
	
	public String build(MailView view) {
		return VelocityEngineUtils.mergeTemplateIntoString(
				velocityEngine, 
				templateResourceName, 
				"UTF-8", 
				Maps.map("view", view));
	}
	
	public String getSubject() { 
		return "[LiveTaskboard]" + subject; 
	}

}
