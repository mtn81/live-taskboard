package jp.mts.taskmanage.domain.model;

import jp.mts.base.domain.model.DomainRepository;

public interface GroupJoinApplicationRepository 
	extends DomainRepository<GroupJoinApplicationId, GroupJoinApplication> {

	GroupJoinApplicationId newId();

}
