package jp.mts.taskmanage.infrastructure.jdbc.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import jp.mts.base.infrastructure.jdbc.SqlInClause;
import jp.mts.base.infrastructure.jdbc.repository.AbstractSimpleJdbcDomainRepository;
import jp.mts.base.util.ListUtils;
import jp.mts.taskmanage.domain.model.group.GroupId;
import jp.mts.taskmanage.domain.model.member.GroupBelonging;
import jp.mts.taskmanage.domain.model.member.Member;
import jp.mts.taskmanage.domain.model.member.MemberBuilder;
import jp.mts.taskmanage.domain.model.member.MemberId;
import jp.mts.taskmanage.domain.model.member.MemberRegisterType;
import jp.mts.taskmanage.domain.model.member.MemberRepository;
import jp.mts.taskmanage.infrastructure.jdbc.model.GroupMemberModel;
import jp.mts.taskmanage.infrastructure.jdbc.model.MemberModel;

import org.springframework.stereotype.Repository;


@Repository
public class JdbcMemberRepository
	extends AbstractSimpleJdbcDomainRepository<MemberId, Member, MemberModel> 
	implements MemberRepository  {

	
	@Override
	public Optional<Member> findById(MemberId id) {
		Optional<Member> member = super.findById(id);

		if(member.isPresent()){
			MemberBuilder memberBuilder = new MemberBuilder(member.get());
			setGroupBelongings(memberBuilder, 
					GroupMemberModel.find("member_id=?", id.value()));
		}

		return member;
	}
	
	@Override
	public List<Member> findByGroupId(GroupId groupId) {

		List<Member> members = findListBySql(
				"select * from members m " + 
				"where exists ( " + 
					"select '1' from groups_members gm " + 
					"where m.member_id = gm.member_id and gm.group_id = ? " + 
				")", 
				groupId.value());
		
		SqlInClause<Member> sqlInClause 
			= new SqlInClause<>("member_id", members, member -> member.id().value());

		List<GroupMemberModel> groupMemberModels = GroupMemberModel.find(
				sqlInClause.condition(), sqlInClause.params());
		
		Map<String, List<GroupMemberModel>> modelsByMemberId 
			= ListUtils.group(groupMemberModels, model -> model.getString("member_id"));
		
		members.forEach(member -> {
			MemberBuilder memberBuilder = new MemberBuilder(member);
			if(modelsByMemberId.containsKey(member.id().value())){
				setGroupBelongings(memberBuilder, 
						modelsByMemberId.get(member.id().value()));
			}
		});

		return members;
	}


	@Override
	public void save(Member member) {
		super.save(member);
		
		GroupMemberModel.delete(
				"member_id=?", member.id().value());

		member.groupBelongings().forEach(groupBelonging -> {
			GroupMemberModel.createIt(
				"group_id", groupBelonging.groupId().value(), 
				"member_id", member.id().value(),
				"admin", groupBelonging.isAdmin());
		});
	}
	
	@Override
	public void remove(Member member) {
		super.remove(member);

		GroupMemberModel.delete(
				"member_id=?", member.id().value());
	}

	@Override
	protected String idColumnName() {
		return "member_id";
	}

	@Override
	protected Member toDomain(MemberModel model) {
		return new MemberBuilder(
				new Member(
					new MemberId(model.getString("member_id")),
					model.getString("name"),
					MemberRegisterType.valueOf(model.getString("type"))))
			.setEmail(model.getString("email"))
			.get();
	}

	@Override
	protected void toModel(MemberModel model, Member entity) {
		model.set(
			"member_id", entity.memberId().value(),
			"name", entity.name(),
			"type", entity.registerType().name(),
			"email", entity.email());
	}

	private void setGroupBelongings(MemberBuilder memberBuilder, List<GroupMemberModel> models) {
		models.forEach(groupMemberModel -> {
			memberBuilder.addGroupBelonging(
				new GroupBelonging(
					new GroupId(groupMemberModel.getString("group_id")), 
					groupMemberModel.getBoolean("admin")));
		});
	}
	

}
