package jp.mts.taskmanage.application;

import static jp.mts.base.application.AppAssertions.assertTrue;

import java.util.Optional;

import jp.mts.base.application.ApplicationException;
import jp.mts.taskmanage.domain.model.Group;
import jp.mts.taskmanage.domain.model.GroupId;
import jp.mts.taskmanage.domain.model.GroupJoinApplication;
import jp.mts.taskmanage.domain.model.GroupJoinApplicationId;
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
		
		Optional<Member> member = memberRepository.findById(new MemberId(applicantMemberId));
		assertTrue(member.isPresent(), ErrorType.MEMBER_NOT_EXIST);

		Optional<Group> group = groupRepository.findById(new GroupId(groupId));
		assertTrue(group != null, ErrorType.GROUP_NOT_EXIST);
		
		GroupJoinApplication application 
			= member.get().applyJoinTo(groupJoinRepository.newId(), group.get());

		groupJoinRepository.save(application);
		
		return application;
	}


	public void cancelJoin(String applicantMemberId, String joinApplicationId) {
		
		Optional<Member> member = memberRepository.findById(new MemberId(applicantMemberId));
		assertTrue(member.isPresent(), ErrorType.MEMBER_NOT_EXIST);

		Optional<GroupJoinApplication> application 
			= groupJoinRepository.findById(new GroupJoinApplicationId(joinApplicationId));
		
		member.get().cancel(application.get());
		
		groupJoinRepository.save(application.get());
	}


	public GroupJoinApplication rejectJoin(
			String joinApplicationId, String adminMemberId) {
		
		GroupJoinApplication application = groupJoinRepository.findById(new GroupJoinApplicationId(joinApplicationId)).get();
		Member member = memberRepository.findById(new MemberId(adminMemberId)).get();

		if(!member.reject(application)) {
			throw new ApplicationException(ErrorType.CANNOT_ACCEPT_JOIN);
		}

		groupJoinRepository.save(application);
		return application;
	}


	public GroupJoinApplication acceptJoin(
			String joinApplicationId, String adminMemberId) {

		GroupJoinApplication application = groupJoinRepository.findById(new GroupJoinApplicationId(joinApplicationId)).get();
		Member member = memberRepository.findById(new MemberId(adminMemberId)).get();

		if(!member.accept(application)) {
			throw new ApplicationException(ErrorType.CANNOT_ACCEPT_JOIN);
		}

		groupJoinRepository.save(application);
		return application;
	}


}
