package jp.mts.taskmanage.domain.model;

import java.util.List;

import jp.mts.base.domain.model.DomainRepository;

public interface MemberRepository extends DomainRepository<MemberId, Member>{
	
	public List<Member> findByGroupId(GroupId groupId);

}
