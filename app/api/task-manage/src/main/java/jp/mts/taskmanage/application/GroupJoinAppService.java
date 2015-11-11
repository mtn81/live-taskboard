package jp.mts.taskmanage.application;

import static jp.mts.base.application.AppAssertions.assertTrue;
import jp.mts.taskmanage.domain.model.Group;
import jp.mts.taskmanage.domain.model.GroupId;
import jp.mts.taskmanage.domain.model.GroupJoinApplication;
import jp.mts.taskmanage.domain.model.GroupJoinApplicationRepository;
import jp.mts.taskmanage.domain.model.GroupRepository;
import jp.mts.taskmanage.domain.model.Member;
import jp.mts.taskmanage.domain.model.MemberId;
import jp.mts.taskmanage.domain.model.MemberRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GroupJoinAppService {
	
	@Autowired
	private GroupRepository groupRepository;
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private GroupJoinApplicationRepository groupJoinRepository;


	public GroupJoinApplication applyJoin(String groupId, String applicantMemberId) {
		
		Member member = memberRepository.findById(new MemberId(applicantMemberId));
		assertTrue(member != null, ErrorType.MEMBER_NOT_EXIST);

		Group group = groupRepository.findById(new GroupId(groupId));
		assertTrue(group != null, ErrorType.GROUP_NOT_EXIST);
		
		GroupJoinApplication application 
			= member.applyJoinTo(groupJoinRepository.newId(), group);

		groupJoinRepository.save(application);
		
		return application;
	}



}
