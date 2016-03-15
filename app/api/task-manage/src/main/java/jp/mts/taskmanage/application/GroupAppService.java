package jp.mts.taskmanage.application;

import static jp.mts.taskmanage.application.ErrorType.GROUP_REMOVE_DISABLED;
import jp.mts.base.application.ApplicationException;
import jp.mts.taskmanage.domain.model.Group;
import jp.mts.taskmanage.domain.model.GroupId;
import jp.mts.taskmanage.domain.model.GroupRepository;
import jp.mts.taskmanage.domain.model.Member;
import jp.mts.taskmanage.domain.model.Member.LeaveResult;
import jp.mts.taskmanage.domain.model.MemberId;
import jp.mts.taskmanage.domain.model.MemberRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GroupAppService {
	
	@Autowired
	private GroupRepository groupRepository;
	@Autowired
	private MemberRepository memberRepository;

	public Group registerGroup(String memberId, String name, String description) {
		Member member = memberRepository.findById(new MemberId(memberId)).get();
		
		Group group = member.createGroup(
				groupRepository.newGroupId(), name, description);

		groupRepository.save(group);

		return group;
	}

	public Group modifyGroup(
			String memberId, 
			String groupId, 
			String name,
			String description) {

		Member member = memberRepository.findById(new MemberId(memberId)).get();
		Group group = groupRepository.findById(new GroupId(groupId)).get();
		
		if (!member.editGroup(group, name, description)) {
			throw new ApplicationException(GROUP_REMOVE_DISABLED);
		}
		
		groupRepository.save(group);

		return group;
	}

	public void removeGroup(String memberId, String groupId) {
		Member member = memberRepository.findById(new MemberId(memberId)).get();
		Group targetGroup = groupRepository.findById(new GroupId(groupId)).get();

		if (!member.remove(targetGroup)) {
			throw new ApplicationException(GROUP_REMOVE_DISABLED);
		}
		groupRepository.remove(targetGroup);
	}

	public Group findBelongingGroup(String groupId, String memberId) {
		Member member = memberRepository.findById(new MemberId(memberId)).get();
		if (member.belongsTo(new GroupId(groupId))) {
			return groupRepository.findById(new GroupId(groupId)).get();
		}
		return null; 
	}
	
	public Group findById(String groupId) {
		return groupRepository.findById(new GroupId(groupId)).get();
	}
	
	public void entryGroup(String groupId, String memberId, boolean admin) {
		Member member = memberRepository.findById(new MemberId(memberId)).get();
		Group group = groupRepository.findById(new GroupId(groupId)).get();
		
		member.entryTo(group, admin);
		
		memberRepository.save(member);
	}
	public void changeGroupBelonging(String groupId, String memberId, boolean admin) {
		Member member = memberRepository.findById(new MemberId(memberId)).get();
		Group group = groupRepository.findById(new GroupId(groupId)).get();
		
	 	if (admin) {
			member.changeToAdmin(group);
		} else {
			if(!member.changeToNormal(group)){
				throw new ApplicationException(ErrorType.CANNOT_CHANGE_NORMAL_OWNER);
			}
		}
		
		memberRepository.save(member);
	}
	public void removeGroupBelonging(String groupId, String memberId) {
		Member member = memberRepository.findById(new MemberId(memberId)).get();
		Group group = groupRepository.findById(new GroupId(groupId)).get();

		LeaveResult result = member.leave(group);
		if(result == LeaveResult.OWNER_ERROR){
			throw new ApplicationException(ErrorType.CANNOT_LEAVE_MEMBER_OWNER);
		}
		
		memberRepository.save(member);
	}

	
}
