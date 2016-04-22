package jp.mts.taskmanage.domain.model.group.join;

import java.util.Optional;


public interface GroupJoinRepository {

	GroupJoinId newId();
	Optional<GroupJoin> findById(GroupJoinId id);
	void save(GroupJoin groupJoinApplication);

}
