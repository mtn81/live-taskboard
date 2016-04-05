package jp.mts.taskmanage.domain.model.group;

import jp.mts.base.domain.model.DomainRepository;


public interface GroupRepository 
	extends DomainRepository<GroupId, Group>{

	GroupId newGroupId();
}
