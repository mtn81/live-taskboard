package jp.mts.taskmanage.domain.model;

import java.util.List;

public interface GroupBelongingRepository {
	
	List<GroupBelonging> findByMember(MemberId memberId);	
}
