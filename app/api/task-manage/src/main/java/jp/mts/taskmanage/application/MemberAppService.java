package jp.mts.taskmanage.application;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jp.mts.taskmanage.domain.model.GroupId;
import jp.mts.taskmanage.domain.model.Member;
import jp.mts.taskmanage.domain.model.MemberId;
import jp.mts.taskmanage.domain.model.MemberRegisterType;
import jp.mts.taskmanage.domain.model.MemberRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberAppService {
	
	@Autowired
	private MemberRepository memberRepository;

	public Member findById(String memberId) {
		return memberRepository.findById(new MemberId(memberId)).get();
	}
	public List<Member> findMembersInGroup(String groupId) {
		return memberRepository.findByGroupId(new GroupId(groupId));
	}
	public List<Member> findAdminMembersInGroup(String groupIdStr) {
		GroupId groupId = new GroupId(groupIdStr);
		return memberRepository.findByGroupId(groupId)
				.stream()
				.filter(member -> member.belongsAsAdmin(groupId))
				.collect(Collectors.toList());
	}
	
	public void registerMember(
			String memberId, 
			String name,
			MemberRegisterType memberRegisterType) {

		Member newMember = new Member(new MemberId(memberId), name, memberRegisterType);
		memberRepository.save(newMember);
	}
	
	public void changeMember(
			String memberId,
			String name,
			String email) {
		
		Optional<Member> foundMember 
			= memberRepository.findById(new MemberId(memberId));
		if(!foundMember.isPresent()) return;
		
		Member member = foundMember.get();
		member.changeAttributes(name, email);
		
		memberRepository.save(member);
		
	}

}
