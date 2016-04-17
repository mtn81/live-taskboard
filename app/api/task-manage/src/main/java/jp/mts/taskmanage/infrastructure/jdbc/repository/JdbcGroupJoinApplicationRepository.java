package jp.mts.taskmanage.infrastructure.jdbc.repository;

import java.sql.Date;
import java.util.UUID;

import jp.mts.base.infrastructure.jdbc.repository.AbstractSimpleJdbcDomainRepository;
import jp.mts.taskmanage.domain.model.group.GroupId;
import jp.mts.taskmanage.domain.model.group.join.GroupJoinApplication;
import jp.mts.taskmanage.domain.model.group.join.GroupJoinApplicationBuilder;
import jp.mts.taskmanage.domain.model.group.join.GroupJoinApplicationId;
import jp.mts.taskmanage.domain.model.group.join.GroupJoinApplicationRepository;
import jp.mts.taskmanage.domain.model.group.join.GroupJoinApplicationStatus;
import jp.mts.taskmanage.domain.model.member.MemberId;
import jp.mts.taskmanage.infrastructure.jdbc.model.GroupJoinModel;

//@Repository
public class JdbcGroupJoinApplicationRepository 
	extends AbstractSimpleJdbcDomainRepository<
		GroupJoinApplicationId, 
		GroupJoinApplication, 
		GroupJoinModel>
	implements GroupJoinApplicationRepository {

	@Override
	public GroupJoinApplicationId newId() {
		return new GroupJoinApplicationId(UUID.randomUUID().toString());
	}

	@Override
	protected String idColumnName() {
		return "application_id";
	}

	@Override
	protected GroupJoinApplication toDomain(GroupJoinModel model) {
		return new GroupJoinApplicationBuilder(
				new GroupJoinApplication(
						new GroupJoinApplicationId(model.getString("application_id")), 
						new GroupId(model.getString("group_id")), 
						new MemberId(model.getString("applicant_id"))))
				.setStatus(GroupJoinApplicationStatus.valueOf(model.getString("status")))
				.setApplied(new Date(model.getTimestamp("applied_time").getTime()))
				.get();
	}

	@Override
	protected void toModel(
			GroupJoinModel model,
			GroupJoinApplication groupJoin) {

		model.set(
				"application_id", groupJoin.id().value(),
				"group_id", groupJoin.groupId().value(),
				"applicant_id", groupJoin.applicationMemberId().value(),
				"status", groupJoin.status().name(),
				"applied_time", groupJoin.applied())
			.setTimestamp(
				"applied_time", groupJoin.applied());
	}

}
