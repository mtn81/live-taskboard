package jp.mts.taskmanage.infrastructure.jdbc.query;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jp.mts.taskmanage.application.query.GroupSearchQuery;

import org.javalite.activejdbc.Base;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcGroupSearchQuery implements GroupSearchQuery {
	
	@Override
	public List<Result> notJoinAppliedByName(String memberId, String groupName) {
		
		return toResult(
			Base.findAll(appliedGroupSql(), "%" + groupName + "%", memberId));
	}

	
	private List<Result> toResult(List<Map> models) {
		return models.stream().map(m -> { 
				return new Result(
						(String)m.get("group_id"), 
						(String)m.get("group_name"), 
						(String)m.get("owner_name")); 
			}).collect(Collectors.toList());
	}

	private String appliedGroupSql() {
		return ""
			+ "select "
			+   "g.group_id as group_id, "
			+   "g.name as group_name, "
			+   "m.name as owner_name "
			+   "from "
			+ "groups g "
			+ "inner join members m "
			+ 	"on g.owner_member_id = m.member_id "
			+ "where "
			+   "g.owner_member_id <> ? "
			+   "g.name like ? "
			+   "and not exists ( "
			+     "select "
			+       "1 "
			+     "from "
			+       "group_joins gj "
			+     "where "
			+       "gj.applicant_id = ? "
			+       "and gj.group_id = g.group_id  "
			+       "and gj.status <> 'CANCELLED'  "
			+   ") ";
	}
}
