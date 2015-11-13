package jp.mts.taskmanage.domain.model;

import java.util.List;

import jp.mts.base.domain.model.DomainRepository;


public interface GroupRepository 
	extends DomainRepository<GroupId, Group>{

	GroupId newGroupId();
	List<Group> findByIds(List<GroupId> groupIds);
	void remove(Group group);
	
}
