package jp.mts.taskmanage.infrastructure.jdbc.query;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import jp.mts.base.util.ListUtils;
import jp.mts.taskmanage.application.query.GroupJoinSearchQuery;
import jp.mts.taskmanage.domain.model.group.join.GroupJoinApplicationStatus;

import org.javalite.activejdbc.Base;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcGroupJoinSearchQuery implements GroupJoinSearchQuery {
	
	@Override
	public List<ByApplicantResult> byApplicant(String memberId) {
		return ListUtils.convert(
				Base.findAll(
					  "select "
					+   "g.group_id as group_id, "
					+   "g.name as group_name, "
					+   "m.name as owner_name, " 
					+   "m.name as owner_type, " 
					+   "gj.applied_time as applied_time, "
					+   "gj.status as applied_status, "
					+   "gj.application_id as application_id " 
					+ "from "
					+   "groups g "
					+   "inner join members m "
					+   	"on g.owner_member_id = m.member_id "  
					+ "inner join group_joins gj "
					+ 	"on ( "
					+ 		"gj.applicant_id = ? " 
					+ 		"and gj.group_id = g.group_id " 
					+ 		"and gj.status <> 'CANCELLED' " 
					+ 	") ",
					memberId)
				,
				record -> new ByApplicantResult(
						(String)record.get("application_id"), 
						(String)record.get("group_id"), 
						(String)record.get("group_name"), 
						(String)record.get("owner_name"),
						(String)record.get("owner_type"),
						record.get("applied_time") == null ? null : new Date(((Timestamp)record.get("applied_time")).getTime()),
						record.get("applied_status") == null ? null : GroupJoinApplicationStatus.valueOf((String)record.get("applied_status"))) 
		);
	}
	
	@Override
	public List<NotJoinAppliedWithNameResult> notJoinAppliedWithName(String memberId, String groupName) {
		return ListUtils.convert(
			Base.findAll(
				  "select "
				+   "g.group_id as group_id, "
				+   "g.name as group_name, "
				+   "m.name as owner_name, "
				+   "m.type as owner_type, "
				+   "g.description as description "
				+ "from "
				+   "groups g "
				+   "inner join members m "
				+ 	  "on g.owner_member_id = m.member_id "
				+ "where "
				+   "g.owner_member_id <> ? "
				+   "and g.name like ? "
				+   "and not exists ( "
				+     "select "
				+       "1 "
				+     "from "
				+       "group_joins gj "
				+     "where "
				+       "gj.applicant_id = ? "
				+       "and gj.group_id = g.group_id  "
				+       "and gj.status not in ('CANCELLED', 'ACCEPTED')  "
				+   ") "
				+   "and not exists ( "
				+     "select "
				+       "1 "
				+     "from "
				+       "groups_members gm "
				+     "where "
				+       "gm.group_id = g.group_id "
				+       "and gm.member_id = ?  "
				+   ") "
				,
				memberId, 
				"%" + groupName + "%", 
				memberId,
				memberId)
			,
			record -> new NotJoinAppliedWithNameResult(
						(String)record.get("group_id"), 
						(String)record.get("group_name"), 
						(String)record.get("owner_name"),
						(String)record.get("owner_type"),
						(String)record.get("description"))
		);
	}

	@Override
	public List<ByAdminResult> acceptableByAdmin(String memberId) {
		return searchByAdmin("APPLIED", memberId);
	}
	@Override
	public List<ByAdminResult> rejectedByAdmin(String memberId) {
		return searchByAdmin("REJECTED", memberId);
	}
	
	
	private List<ByAdminResult> searchByAdmin(String status, String memberId) {
		return ListUtils.convert(
			Base.findAll(
				"select "
				+ "g.group_id as group_id, "
				+ "g.name as group_name, "
				+ "gj.applicant_id as applicant_id, "
				+ "m.type as applicant_type, "
				+ "m.name as applicant_name, "
				+ "gj.applied_time as applied_time, "
				+ "gj.status as applied_status, "
				+ "gj.application_id as application_id " + 
				"from "
				+ "group_joins gj "
				+ "inner join groups g "
				+ 	"on gj.group_id = g.group_id "
				+ "inner join members m "
				+ 	"on gj.applicant_id = m.member_id " +
				"where "
				+ "gj.status = '"+ status + "' " 
				+ "and exists ( "
				+   "select "
				+     "1 "
				+   "from "
				+     "groups_members gm "
				+   "where "
				+ 	  "gm.group_id = g.group_id "  
				+ 	  "and gm.member_id = ? "  
				+ 	  "and gm.admin = true "  
				+ ") ",
				memberId)
			,
			record -> new ByAdminResult(
						(String)record.get("application_id"), 
						(String)record.get("group_id"), 
						(String)record.get("group_name"), 
						(String)record.get("applicant_id"),
						(String)record.get("applicant_type"),
						(String)record.get("applicant_name"),
						record.get("applied_time") == null ? null : new Date(((Timestamp)record.get("applied_time")).getTime()))
		);
	}
	
	

}
