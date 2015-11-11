package jp.mts.taskmanage.infrastructure.jdbc.query;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jp.mts.taskmanage.application.query.GroupSearchQuery;
import jp.mts.taskmanage.domain.model.GroupJoinApplicationStatus;

import org.javalite.activejdbc.Base;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcGroupSearchQuery implements GroupSearchQuery {
	
	@Override
	public List<Result> notJoinAppliedByName(String memberId, String groupName) {
		
		return toResult(
			Base.findAll(
					appliedGroupSql("g.name like ? and gj.status is null"),
					memberId, "%" + groupName + "%"));
	}

	@Override
	public List<Result> joinApplied(String memberId) {

		return toResult(
			Base.findAll( 
					appliedGroupSql("gj.status is not null"),
					memberId));
	}
	
	private List<Result> toResult(List<Map> models) {
		return models.stream().map(m -> { 
				return new Result(
						(String)m.get("group_id"), 
						(String)m.get("group_name"), 
						(String)m.get("owner_name"),
						m.get("applied_time") == null ? null : new Date(((Timestamp)m.get("applied_time")).getTime()),
						m.get("applied_status") == null ? null : GroupJoinApplicationStatus.valueOf((String)m.get("applied_status"))); 
			}).collect(Collectors.toList());
	}

	private String appliedGroupSql(String whereClause) {
		return 
			"select "
			+ "g.group_id as group_id, "
			+ "g.name as group_name, "
			+ "m.name as owner_name, " 
			+ "gj.applied_time as applied_time, "
			+ "gj.status as applied_status " + 
			"from "
			+ "groups g "
			+ "inner join members m "
			+ 	"on g.owner_member_id = m.member_id "  
			+ "left outer join group_joins gj "
			+ 	"on ( "
			+ 		"gj.applicant_id = ? " 
			+ 		"and gj.group_id = g.group_id " 
			+ 	") " +
			"where "
			+  whereClause;
	}
}
