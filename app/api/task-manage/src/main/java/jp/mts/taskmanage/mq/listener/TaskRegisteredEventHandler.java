package jp.mts.taskmanage.mq.listener;

import java.util.Date;

import jp.mts.base.lib.mail.MailTemplate;
import jp.mts.base.util.DateUtils;
import jp.mts.libs.event.eventstore.EventBody;
import jp.mts.libs.event.mq.MqEventHandler;
import jp.mts.libs.event.mq.MqEventHandlerConfig;
import jp.mts.taskmanage.application.MemberAppService;
import jp.mts.taskmanage.application.TaskAppService;
import jp.mts.taskmanage.domain.model.member.Member;
import jp.mts.taskmanage.domain.model.task.Task;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

		javaMailSender.send(mailTemplate.createMessage(
				assigned.email(), new MailView(task)));
		
	}
	
	public static class MailView extends jp.mts.base.lib.mail.MailView {
		private Task task;

		public MailView(Task task) {
			this.task = task;
		}
		public String getTask() {
			return task.name();
		}
		public String getDeadline() {
			return DateUtils.prettyYmd(task.deadline());
		}
		public String getStatus() {
			return task.status().name();
		}
		public String getMemo() {
			return StringUtils.defaultString(task.memo());
		}
	}

}
