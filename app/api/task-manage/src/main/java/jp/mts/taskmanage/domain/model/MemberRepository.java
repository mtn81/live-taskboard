package jp.mts.taskmanage.domain.model;

import java.util.List;

public interface MemberRepository {
	
	void save(Member member);
	public Member findById(MemberId memberId);
	public List<Member> findByGroupId(GroupId groupId);

}
