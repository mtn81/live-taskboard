package jp.mts.taskmanage.infrastructure.repository.jdbc;

import java.util.List;
import java.util.stream.Collectors;

import jp.mts.taskmanage.domain.model.GroupBelonging;
import jp.mts.taskmanage.domain.model.GroupBelongingRepository;
import jp.mts.taskmanage.domain.model.GroupId;
import jp.mts.taskmanage.domain.model.MemberId;
import jp.mts.taskmanage.infrastructure.repository.jdbc.model.GroupMemberModel;

import org.springframework.stereotype.Repository;

@Repository
public class JdbcGroupBelongingRepository implements GroupBelongingRepository {

	@Override
	public List<GroupBelonging> findByMember(MemberId memberId) {
		return GroupMemberModel
				.where("member_id = ?", memberId.value()).orderBy("group_id")
				.stream()
					.map(model -> toDeomain((GroupMemberModel)model))
					.collect(Collectors.toList());
	}
	
	
	private GroupBelonging toDeomain(GroupMemberModel model) {
		return new GroupBelonging(
				new GroupId(model.getString("group_id")), 
				new MemberId(model.getString("member_id")));

	}
	
}
