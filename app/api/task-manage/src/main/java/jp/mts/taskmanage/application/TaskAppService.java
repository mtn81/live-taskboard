package jp.mts.taskmanage.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.mts.taskmanage.domain.model.GroupId;
import jp.mts.taskmanage.domain.model.Task;
import jp.mts.taskmanage.domain.model.TaskRepository;

@Service
public class TaskAppService {
	
	@Autowired
	private TaskRepository taskRepository;

	public List<Task> findTasksByGroup(String groupId) {
		return taskRepository.findByGroupId(new GroupId(groupId));
	}

}
