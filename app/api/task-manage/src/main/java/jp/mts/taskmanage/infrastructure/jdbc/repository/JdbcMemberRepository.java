package jp.mts.taskmanage.infrastructure.jdbc.repository;

import java.util.List;

import jp.mts.base.infrastructure.jdbc.SqlInClause;
import jp.mts.base.infrastructure.jdbc.repository.AbstractSimpleJdbcDomainRepository;
import jp.mts.taskmanage.domain.model.GroupId;
import jp.mts.taskmanage.domain.model.Member;
import jp.mts.taskmanage.domain.model.MemberId;
import jp.mts.taskmanage.domain.model.MemberRepository;
import jp.mts.taskmanage.infrastructure.jdbc.model.GroupMemberModel;
import jp.mts.taskmanage.infrastructure.jdbc.model.MemberModel;

import org.javalite.activejdbc.Model;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcMemberRepository
	extends AbstractSimpleJdbcDomainRepository<MemberId, Member, MemberModel> 
	implements MemberRepository  {

	@Override
	public List<Member> findByGroupId(GroupId groupId) {
		List<Model> groupMemberModels = GroupMemberModel.find("group_id=?", groupId.value());
		SqlInClause<Model> sqlInClause 
			= new SqlInClause<>("member_id", groupMemberModels, model -> model.getString("member_id"));

		return findList(sqlInClause.condition(), sqlInClause.params());
	}


	@Override
	protected String idColumnName() {
		return "member_id";
	}

	@Override
	protected Member toDomain(MemberModel model) {
		return new Member(
				new MemberId(model.getString("member_id")),
				model.getString("name"));
	}

	@Override
	protected MemberModel toModel(MemberModel model, Member entity) {
		model.set(
			"member_id", entity.memberId().value(),
			"name", entity.name());
		return model;
	}
}
