package jp.mts.taskmanage.mq.listener;

import java.util.Date;

import jp.mts.base.lib.mail.MailTemplate;
import jp.mts.libs.event.eventstore.EventBody;
import jp.mts.libs.event.mq.MqEventHandler;
import jp.mts.libs.event.mq.MqEventHandlerConfig;
import jp.mts.taskmanage.application.MemberAppService;
import jp.mts.taskmanage.application.TaskAppService;
import jp.mts.taskmanage.domain.model.Member;
import jp.mts.taskmanage.domain.model.Task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@MqEventHandlerConfig(targetEventTypes="mts:taskmanage/TaskRegistered")
public class TaskRegisteredEventHandler implements MqEventHandler {

	@Autowired
	private TaskAppService taskAppService;
	@Autowired
	private MemberAppService memberAppService;
	@Autowired
	private JavaMailSender javaMailSender;
	@Autowired @Qualifier("taskRegistered")
	private MailTemplate mailTemplate;

	@Override
	public void handleEvent(
			long eventId, 
			String publisherId, 
			Date occurred,
			EventBody eventBody) {
		
		String groupId = eventBody.asString("groupId");
		String taskId = eventBody.asString("taskId");
		String assignedMemberId = eventBody.asString("assigned");
		
		Task task = taskAppService.loadById(groupId, taskId);
		Member assigned = memberAppService.findById(assignedMemberId);
		
		if (!assigned.emailNotificationEnabled()) return;

		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(assigned .email());
		mailMessage.setSubject(mailTemplate.getSubject());
		mailMessage.setText(mailTemplate.build(new MailView(task)));
		javaMailSender.send(mailMessage);
		
	}
	
	public static class MailView extends jp.mts.base.lib.mail.MailView {
		private Task task;

		public MailView(Task task) {
			this.task = task;
		}
		public String getTask() {
			return task.name();
		}
	}

}
