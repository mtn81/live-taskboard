package jp.mts.taskmanage.infrastructure.jdbc.repository;

import static java.util.stream.Collectors.toList;

import java.util.List;

import jp.mts.base.infrastructure.jdbc.SqlInClause;
import jp.mts.taskmanage.domain.model.GroupId;
import jp.mts.taskmanage.domain.model.Member;
import jp.mts.taskmanage.domain.model.MemberId;
import jp.mts.taskmanage.domain.model.MemberRepository;
import jp.mts.taskmanage.infrastructure.jdbc.model.GroupMemberModel;
import jp.mts.taskmanage.infrastructure.jdbc.model.MemberModel;

import org.javalite.activejdbc.Model;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcMemberRepository implements MemberRepository {

	@Override
	public Member findById(MemberId memberId) {
		MemberModel model = MemberModel.findFirst("member_id = ?", memberId.value());
		if(model == null){
			return null;
		}
		return toDomain(model);
	}

	@Override
	public void save(Member member) {
		MemberModel model = MemberModel.findFirst("member_id = ?", member.memberId().value());
		if(model == null){
			model = new MemberModel();
		}

		model.set(
				"member_id", member.memberId().value(),
				"name", member.name())
			.saveIt();
	}

	@Override
	public List<Member> findByGroupId(GroupId groupId) {
		List<Model> groupMemberModels = GroupMemberModel.find("group_id=?", groupId.value());
		SqlInClause<Model> sqlInClause 
			= new SqlInClause<>("member_id", groupMemberModels, model -> model.getString("member_id"));
		List<Model> memberModels = MemberModel.find(sqlInClause.condition(), sqlInClause.params());

		return memberModels.stream()
				.map(memberModel -> toDomain(memberModel))
				.collect(toList());
	}

	private Member toDomain(Model model) {
		return new Member(
				new MemberId(model.getString("member_id")),
				model.getString("name"));
	}
}
