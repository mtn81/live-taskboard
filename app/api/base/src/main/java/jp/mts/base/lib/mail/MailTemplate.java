package jp.mts.base.lib.mail;

import java.util.List;

import jp.mts.base.util.MapUtils;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.google.common.collect.Lists;

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
				MapUtils.pairs("view", view));
	}
	
	public String getSubject() { 
		return "[LiveTaskboard]" + subject; 
	}
	
	public SimpleMailMessage createMessage(String to, MailView payload) {
		return createMessage(Lists.newArrayList(to), payload);
	}
	public SimpleMailMessage createMessage(List<String> to, MailView payload) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(to.toArray(new String[0]));
		mailMessage.setSubject(getSubject());
		mailMessage.setText(build(payload));
		return mailMessage;
	}

}
