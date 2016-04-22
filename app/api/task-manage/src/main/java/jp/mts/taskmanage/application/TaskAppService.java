package jp.mts.taskmanage.application;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import jp.mts.base.application.ApplicationException;
import jp.mts.taskmanage.domain.model.group.Group;
import jp.mts.taskmanage.domain.model.group.GroupId;
import jp.mts.taskmanage.domain.model.group.GroupRepository;
import jp.mts.taskmanage.domain.model.member.Member;
import jp.mts.taskmanage.domain.model.member.MemberId;
import jp.mts.taskmanage.domain.model.member.MemberRepository;
import jp.mts.taskmanage.domain.model.task.Task;
import jp.mts.taskmanage.domain.model.task.TaskId;
import jp.mts.taskmanage.domain.model.task.TaskRepository;
import jp.mts.taskmanage.domain.model.task.TaskStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TaskAppService {
	
	@Autowired
	private TaskRepository taskRepository;
	@Autowired
	private GroupRepository groupRepository;
	@Autowired
	private MemberRepository memberRepository;

	public List<Task> findTasksByGroup(String groupId) {
		return taskRepository.findByGroupId(new GroupId(groupId));
	}

	public Task registerTask(
			String aGroupId, 
			String taskName, 
			String assigned, 
			Date deadline) {
		
		GroupId groupId = new GroupId(aGroupId);
		Optional<Group> group = groupRepository.findById(groupId);
		Optional<Member> assignedMember = memberRepository.findById(new MemberId(assigned));
		Task task = group.get().createTask(
				taskRepository.newTaskId(groupId),
				taskName,
				assignedMember.get(),
				deadline);
		taskRepository.save(task);

		return task;
	}

	public Task removeTask(String groupId, String taskId) {
		
		Group group = groupRepository.findById(new GroupId(groupId)).get();
		Task task = taskRepository.findById(new GroupId(groupId), new TaskId(taskId)).get();
		
		if(!group.removeTask(task)){
			throw new ApplicationException(ErrorType.NOT_AUTHORIZED);
		};
		
		taskRepository.remove(task);
		
		return task;
	}

	public Task modifyTask(
			String modifierMemberId,
			String aGroupId, 
			String aTaskId, 
			String taskName,
			String assigned, 
			Date deadline,
			TaskStatus status) {
		
		GroupId groupId = new GroupId(aGroupId);
		TaskId taskId = new TaskId(aTaskId);

		Task task = taskRepository.findById(groupId, taskId).get();
		Member member = memberRepository.findById(new MemberId(assigned)).get();
		Member modifier = memberRepository.findById(new MemberId(modifierMemberId)).get();
		
		task.changeSummary(
				taskName, 
				member, 
				deadline,
				status,
				modifier);
		
		taskRepository.save(task);

		return task;
	}

	public Task modifyTaskDetail(
			String modifierMemberId,
			String aGroupId, 
			String aTaskId, 
			String memo) {
		
		GroupId groupId = new GroupId(aGroupId);
		TaskId taskId = new TaskId(aTaskId);

		Task task = taskRepository.findById(groupId, taskId).get();
		Member modifier = memberRepository.findById(new MemberId(modifierMemberId)).get();
		task.changeDetail(memo, modifier );
		
		taskRepository.save(task);

		return task;
	}

	public Task loadById(String groupId, String taskId) {
		return taskRepository.findById(new GroupId(groupId), new TaskId(taskId)).get();
	}

}
