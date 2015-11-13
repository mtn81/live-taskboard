package jp.mts.taskmanage.infrastructure.jdbc.repository;

import java.sql.Date;
import java.util.UUID;

import jp.mts.base.infrastructure.jdbc.repository.AbstractSimpleJdbcDomainRepository;
import jp.mts.taskmanage.domain.model.GroupId;
import jp.mts.taskmanage.domain.model.GroupJoinApplication;
import jp.mts.taskmanage.domain.model.GroupJoinApplicationBuilder;
import jp.mts.taskmanage.domain.model.GroupJoinApplicationId;
import jp.mts.taskmanage.domain.model.GroupJoinApplicationRepository;
import jp.mts.taskmanage.domain.model.GroupJoinApplicationStatus;
import jp.mts.taskmanage.domain.model.MemberId;
import jp.mts.taskmanage.infrastructure.jdbc.model.GroupJoinModel;

import org.springframework.stereotype.Repository;

@Repository
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
	protected GroupJoinModel toModel(
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
		
		return model;
	}

}
