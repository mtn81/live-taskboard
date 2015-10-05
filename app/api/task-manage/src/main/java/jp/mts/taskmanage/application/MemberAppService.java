package jp.mts.taskmanage.application;

import java.util.List;

import jp.mts.taskmanage.domain.model.GroupId;
import jp.mts.taskmanage.domain.model.Member;
import jp.mts.taskmanage.domain.model.MemberRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberAppService {
	
	@Autowired
	private MemberRepository memberRepository;

	public List<Member> findMembersInGroup(String groupId) {
		return memberRepository.findByGroupId(new GroupId(groupId));
	}

}
