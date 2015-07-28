package jp.mts.taskmanage.application;

import static jp.mts.taskmanage.application.ErrorType.GROUP_NOT_AVAILABLE;
import static jp.mts.taskmanage.application.ErrorType.GROUP_NOT_EXIST;
import static jp.mts.taskmanage.application.ErrorType.GROUP_REMOVE_DISABLED;
import static jp.mts.taskmanage.application.ErrorType.MEMBER_NOT_EXIST;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jp.mts.base.application.ApplicationException;
import jp.mts.taskmanage.domain.model.Group;
import jp.mts.taskmanage.domain.model.Group.State;
import jp.mts.taskmanage.domain.model.GroupBelonging;
import jp.mts.taskmanage.domain.model.GroupBelongingRepository;
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

	public void remove(String memberId, String groupId) {
		GroupBelonging groupBelonging 
			= groupBelongingRepository.findById(new MemberId(memberId), new GroupId(groupId));
		if (!groupBelonging.isAdmin()) {
			throw new ApplicationException(GROUP_REMOVE_DISABLED);
		}
		
		Group targetGroup = groupRepository.findById(new GroupId(groupId));
		groupRepository.remove(targetGroup);
		
	}
	
	public List<GroupBelongingPair> listBelonging(String memberId) {
		List<GroupBelonging> groupBelongings 
			= groupBelongingRepository.findByMember(new MemberId(memberId));
		List<Group> groups = groupRepository.findByIds(toGroupIds(groupBelongings));
		return GroupBelongingPair.pairs(groups, groupBelongings);
	}
	
	private List<GroupId> toGroupIds(List<GroupBelonging> groupBelongings){
		return groupBelongings.stream()
				.map(gb -> gb.groupId())
				.collect(Collectors.toList());
	}

	public void entryAdministrator(String groupId, String memberId) {
		Group group = groupRepository.findById(new GroupId(groupId));
		if (group == null) return;

		Member member = memberRepository.findById(new MemberId(memberId));
		if (member == null) return;
		
		GroupBelonging entry = member.entryAsAdministratorTo(group);
		groupBelongingRepository.save(entry);
	}

	public Group detectRegisteredGroupAvailable(String groupId) {
		
		int tryCount = 0;
		while(tryCount < 10){
			Group group = groupRepository.findById(new GroupId(groupId));
			if(group == null) throw new ApplicationException(GROUP_NOT_EXIST);
			if(group.state() == State.AVAILABLE){
				return group;
			}
			try { Thread.sleep(30000); } catch (InterruptedException e) { }
			tryCount++;
		}
		
		throw new ApplicationException(GROUP_NOT_AVAILABLE);
		
	}

	public void changeGroupAvailable(String groupId) {
		Group group = groupRepository.findById(new GroupId(groupId));
		if (group == null) return;

		group.changeToAvailable();
		groupRepository.save(group);
	}
	
	public static class GroupBelongingPair {
		private Group group;
		private GroupBelonging groupBelonging;

		public GroupBelongingPair(Group group, GroupBelonging groupBelonging) {
			this.group = group;
			this.groupBelonging = groupBelonging;
		}

		public static List<GroupBelongingPair> pairs(
				List<Group> groups,
				List<GroupBelonging> groupBelongings) {
			List<GroupBelongingPair> pairs = new ArrayList<>();
			groups.forEach(g -> {
				groupBelongings.forEach(gb -> {
					if(g.groupId().equals(gb.groupId())) 
						pairs.add(new GroupBelongingPair(g, gb));
				});
			});
			return pairs;
		}

		public Group getGroup() {
			return group;
		}
		public GroupBelonging getGroupBelonging() {
			return groupBelonging;
		}
	}

}
