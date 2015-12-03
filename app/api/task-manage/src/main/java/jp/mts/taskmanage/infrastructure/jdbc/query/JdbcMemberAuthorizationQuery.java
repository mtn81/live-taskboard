package jp.mts.taskmanage.infrastructure.jdbc.query;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import jp.mts.taskmanage.application.query.MemberAuthorizationQuery;

import org.javalite.activejdbc.Base;
import org.springframework.stereotype.Service;

@Service
public class JdbcMemberAuthorizationQuery implements MemberAuthorizationQuery {

	@Override
	public Map<String, Boolean> belongingByMember(String memberId) {
		Map<String, Boolean> resultMap = new HashMap<>();
		Base.findAll(
			  "select "
			+   "gm.group_id as group_id, "
			+   "gm.admin as admin "
			+ "from "
			+   "groups_members gm "
			+ "where "
			+   "gm.member_id = ? " , memberId).
			stream().forEach(record -> {
				resultMap.put(
						(String)record.get("group_id"), 
						(Boolean)record.get("admin"));
			});
		return resultMap;
	}

	@Override
	public Set<String> joinsByMember(String memberId) {
		return Base.findAll(
			  "select "
			+   "gj.application_id as application_id "
			+ "from "
			+   "group_joins gj "
			+ "where "
			+   "gj.applicant_id = ? " , memberId)
			.stream()
			.map(record -> (String)record.get("application_id"))
			.collect(Collectors.toSet());
	}

}
