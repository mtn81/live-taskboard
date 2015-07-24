package jp.mts.taskmanage.domain.model;

import java.util.List;

public interface GroupBelongingRepository {
	
	List<GroupBelonging> findByMember(MemberId memberId);
	GroupBelonging findById(MemberId memberId, GroupId groupId);

	void save(GroupBelonging groupBelonging);	
}
