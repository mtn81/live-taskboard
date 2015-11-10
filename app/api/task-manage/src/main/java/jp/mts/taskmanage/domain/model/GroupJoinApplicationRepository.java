package jp.mts.taskmanage.domain.model;

public interface GroupJoinApplicationRepository {

	GroupJoinApplicationId newId();
	void save(GroupJoinApplication groupJoin);
	GroupJoinApplication findById(GroupJoinApplicationId groupJoinId);

}
