package jp.mts.taskmanage.application;

import java.util.Date;
import java.util.List;

import jp.mts.taskmanage.domain.model.Group;
import jp.mts.taskmanage.domain.model.GroupId;
import jp.mts.taskmanage.domain.model.GroupRepository;
import jp.mts.taskmanage.domain.model.Member;
import jp.mts.taskmanage.domain.model.MemberId;
import jp.mts.taskmanage.domain.model.MemberRepository;
import jp.mts.taskmanage.domain.model.Task;
import jp.mts.taskmanage.domain.model.TaskId;
import jp.mts.taskmanage.domain.model.TaskRepository;
import jp.mts.taskmanage.domain.model.TaskStatus;

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
			String aGroupId, String taskName, String assigned, Date deadline) {
		
		GroupId groupId = new GroupId(aGroupId);
		Group group = groupRepository.findById(groupId);
		Member assignedMember = memberRepository.findById(new MemberId(assigned));
		Task task = group.createTask(
				taskRepository.newTaskId(groupId),
				taskName,
				assignedMember,
				deadline);
		taskRepository.save(task);

		return task;
	}

	public Task removeTask(String groupId, String taskId) {
		
		Task task = taskRepository.findById(
				new GroupId(groupId), new TaskId(taskId));
		
		taskRepository.remove(task);
		
		return task;
	}

	public Task modifyTask(
			String aGroupId, 
			String aTaskId, 
			String taskName,
			String assigned, 
			Date deadline,
			TaskStatus status) {
		
		GroupId groupId = new GroupId(aGroupId);
		TaskId taskId = new TaskId(aTaskId);

		Task task = taskRepository.findById(groupId, taskId);
		task.changeSummary(
				taskName, 
				memberRepository.findById(new MemberId(assigned)), 
				deadline,
				status);
		
		taskRepository.save(task);

		return task;
	}

}
