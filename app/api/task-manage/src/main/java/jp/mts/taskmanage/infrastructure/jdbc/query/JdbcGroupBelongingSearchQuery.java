package jp.mts.taskmanage.infrastructure.jdbc.query;

import java.util.List;

import jp.mts.base.util.ListUtils;
import jp.mts.taskmanage.application.query.GroupBelongingSearchQuery;

import org.javalite.activejdbc.Base;
import org.springframework.stereotype.Service;

@Service
public class JdbcGroupBelongingSearchQuery implements GroupBelongingSearchQuery {

	@Override
	public List<ByMemberResult> byMember(String memberId) {
		return ListUtils.convert(
			Base.findAll(
				  "select "
				+   "g.group_id as group_id, "
				+   "g.name as group_name, "
				+   "g.description as description, "
				+   "gm.admin as admin "
				+ "from "
				+   "groups g "
				+   "left outer join groups_members gm "
				+ 	  "on g.group_id = gm.group_id "
				+ "where "
				+   "gm.member_id = ? " ,
				memberId)
			,
			record -> new ByMemberResult(
						(String)record.get("group_id"), 
						(String)record.get("group_name"), 
						(String)record.get("description"), 
						(boolean)record.get("admin"))
		);
	}

	@Override
	public ByMemberResult byMember(String memberId, String groupId) { 
		List<ByMemberResult> results = ListUtils.convert(
			Base.findAll(
				  "select "
				+   "g.group_id as group_id, "
				+   "g.name as group_name, "
				+   "g.description as description, "
				+   "gm.admin as admin "
				+ "from "
				+   "groups g "
				+   "left outer join groups_members gm "
				+ 	  "on g.group_id = gm.group_id "
				+ "where "
				+   "gm.member_id = ? "
				+   "and gm.group_id = ? " ,
				memberId, groupId)
			,
			record -> new ByMemberResult(
						(String)record.get("group_id"), 
						(String)record.get("group_name"), 
						(String)record.get("description"), 
						(boolean)record.get("admin"))
		);

		return results.isEmpty() ? null : results.get(0);
	}

}
