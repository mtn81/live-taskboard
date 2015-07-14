package jp.mts.taskmanage.application;

import static jp.mts.taskmanage.application.ErrorType.MEMBER_NOT_EXIST;

import java.util.List;
import java.util.stream.Collectors;

import jp.mts.base.application.ApplicationException;
import jp.mts.taskmanage.domain.model.Group;
import jp.mts.taskmanage.domain.model.GroupBelonging;
import jp.mts.taskmanage.domain.model.GroupBelongingRepository;
import jp.mts.taskmanage.domain.model.GroupId;
import jp.mts.taskmanage.domain.model.GroupRepository;
import jp.mts.taskmanage.domain.model.Member;
import jp.mts.taskmanage.domain.model.MemberId;
import jp.mts.taskmanage.domain.model.MemberRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupAppService {
	
	@Autowired
	private GroupRepository groupRepository;
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private GroupBelongingRepository groupBelongingRepository;

	public Group register(String memberId, String name, String description) {
		Member member = memberRepository.findById(new MemberId(memberId));
		if (member == null) {
			throw new ApplicationException(MEMBER_NOT_EXIST);
		}
		
		Group group = member.createGroup(
				groupRepository.newGroupId(), name, description);

		groupRepository.save(group);
		return group;
	}

	public List<Group> listBelonging(String memberId) {
		List<GroupBelonging> groupBelongings 
			= groupBelongingRepository.findByMember(new MemberId(memberId));
		return groupRepository.findByIds(toGroupIds(groupBelongings));
	}
	
	private List<GroupId> toGroupIds(List<GroupBelonging> groupBelongings){
		return groupBelongings.stream()
				.map(gb -> gb.groupId())
				.collect(Collectors.toList());
	}

}
