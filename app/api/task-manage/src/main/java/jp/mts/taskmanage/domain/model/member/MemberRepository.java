package jp.mts.taskmanage.domain.model.member;

import java.util.List;

import jp.mts.base.domain.model.DomainRepository;
import jp.mts.taskmanage.domain.model.group.GroupId;

public interface MemberRepository extends DomainRepository<MemberId, Member>{
	
	public List<Member> findByGroupId(GroupId groupId);

}
