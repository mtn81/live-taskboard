package jp.mts.taskmanage.application;

import static jp.mts.base.application.AppAssertions.assertTrue;
import static jp.mts.taskmanage.application.ErrorType.CANNOT_ACCEPT_JOIN;

import java.util.Optional;

import jp.mts.base.application.ApplicationException;
import jp.mts.taskmanage.domain.model.group.Group;
import jp.mts.taskmanage.domain.model.group.GroupId;
import jp.mts.taskmanage.domain.model.group.GroupRepository;
import jp.mts.taskmanage.domain.model.group.join.GroupJoin;
import jp.mts.taskmanage.domain.model.group.join.GroupJoinId;
import jp.mts.taskmanage.domain.model.group.join.GroupJoinRepository;
import jp.mts.taskmanage.domain.model.member.Member;
import jp.mts.taskmanage.domain.model.member.MemberId;
import jp.mts.taskmanage.domain.model.member.MemberRepository;

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
	private GroupJoinRepository groupJoinRepository;


	public GroupJoin applyJoin(String groupId, String applicantMemberId) {
		
		Optional<Member> member = memberRepository.findById(new MemberId(applicantMemberId));
		assertTrue(member.isPresent(), ErrorType.MEMBER_NOT_EXIST);

		Optional<Group> group = groupRepository.findById(new GroupId(groupId));
		assertTrue(group != null, ErrorType.GROUP_NOT_EXIST);
		
		GroupJoin application 
			= member.get().applyJoinTo(groupJoinRepository.newId(), group.get());

		groupJoinRepository.save(application);
		
		return application;
	}


	public void cancelJoin(String applicantMemberId, String joinApplicationId) {
		
		Member member = memberRepository.findById(new MemberId(applicantMemberId)).get();

		GroupJoin application 
			= groupJoinRepository.findById(new GroupJoinId(joinApplicationId)).get();
		
		if(!member.cancel(application)){
			throw new ApplicationException(CANNOT_ACCEPT_JOIN);
		}
		
		groupJoinRepository.save(application);
	}

	public GroupJoin acceptJoin(
			String groupId, String joinApplicationId) {

		GroupJoin application = groupJoinRepository.findById(new GroupJoinId(joinApplicationId)).get();
		Group group = groupRepository.findById(new GroupId(groupId)).get();

		if(!group.accept(application)) {
			throw new ApplicationException(ErrorType.CANNOT_ACCEPT_JOIN);
		}

		groupJoinRepository.save(application);
		return application;
	}
	public GroupJoin rejectJoin(
			String groupId, String joinApplicationId) {
		
		GroupJoin application = groupJoinRepository.findById(new GroupJoinId(joinApplicationId)).get();
		Group group = groupRepository.findById(new GroupId(groupId)).get();

		if(!group.reject(application)) {
			throw new ApplicationException(ErrorType.CANNOT_REJECT_JOIN);
		}

		groupJoinRepository.save(application);
		return application;
	}
}
