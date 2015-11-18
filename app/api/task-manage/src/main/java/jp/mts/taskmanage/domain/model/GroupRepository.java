package jp.mts.taskmanage.domain.model;

import jp.mts.base.domain.model.DomainRepository;


public interface GroupRepository 
	extends DomainRepository<GroupId, Group>{

	GroupId newGroupId();
}
