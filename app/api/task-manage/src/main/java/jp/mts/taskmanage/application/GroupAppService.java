package jp.mts.taskmanage.application;

import static jp.mts.taskmanage.application.ErrorType.GROUP_REMOVE_DISABLED;
import jp.mts.base.application.ApplicationException;
import jp.mts.taskmanage.domain.model.Group;
import jp.mts.taskmanage.domain.model.GroupId;
import jp.mts.taskmanage.domain.model.GroupRepository;
import jp.mts.taskmanage.domain.model.Member;
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
		if (member.belongsTo(groupId)) {
			return groupRepository.findById(new GroupId(groupId)).get();
		}
	
		throw new ApplicationException(ErrorType.GROUP_NOT_AVAILABLE);
	}
	
	public void entryGroup(String groupId, String memberId, boolean admin) {
		Member member = memberRepository.findById(new MemberId(memberId)).get();
		Group group = groupRepository.findById(new GroupId(groupId)).get();
		
		member.entryTo(group, admin);
		
		memberRepository.save(member);
	}
	
}
