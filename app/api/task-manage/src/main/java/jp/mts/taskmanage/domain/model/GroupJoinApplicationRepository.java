package jp.mts.taskmanage.domain.model;

public interface GroupJoinApplicationRepository {

	GroupJoinApplicationId newApplicationId();
	void save(GroupJoinApplication groupJoinApplication);

}
