package jp.mts.taskmanage.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.mts.taskmanage.domain.model.Group;
import jp.mts.taskmanage.domain.model.GroupRepository;

@Service
public class GroupAppService {
	
	@Autowired
	private GroupRepository groupRepository;

	public Group register(String name, String description) {
		Group group = new Group(groupRepository.newGroupId(), name, description);
		groupRepository.save(group);
		return group;
	}

}
