package jp.mts.taskmanage.domain.model.member;

import java.util.List;
import java.util.Optional;

import jp.mts.taskmanage.domain.model.group.GroupId;

public interface MemberRepository {
	
	List<Member> findByGroupId(GroupId groupId);
	Optional<Member> findById(MemberId id);
	void save(Member member);

}
