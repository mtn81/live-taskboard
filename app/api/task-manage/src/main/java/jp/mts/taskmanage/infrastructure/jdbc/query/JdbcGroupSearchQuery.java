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
	public List<Result> byName(String groupName) {
		
		List<Map> models = Base.findAll(
			"select "
			+ "g.group_id as group_id, "
			+ "g.name as group_name, "
			+ "m.name as owner_name " + 
			"from "
			+ "groups g, "
			+ "members m " + 
			"where "
			+ "g.owner_member_id = m.member_id "
			+ "and g.name like ?", 
			"%" + groupName + "%");
		
		return models.stream().map(m -> { 
				return new Result(
						(String)m.get("group_id"), 
						(String)m.get("group_name"), 
						(String)m.get("owner_name")); 
			}).collect(Collectors.toList());
	}
	
}
