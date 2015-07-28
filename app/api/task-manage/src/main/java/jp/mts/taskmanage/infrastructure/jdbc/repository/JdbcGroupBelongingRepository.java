package jp.mts.taskmanage.infrastructure.jdbc.repository;

import static org.hamcrest.CoreMatchers.nullValue;

import java.util.List;
import java.util.stream.Collectors;

import jp.mts.taskmanage.domain.model.GroupBelonging;
import jp.mts.taskmanage.domain.model.GroupBelongingRepository;
import jp.mts.taskmanage.domain.model.GroupId;
import jp.mts.taskmanage.domain.model.MemberId;
import jp.mts.taskmanage.infrastructure.jdbc.model.GroupMemberModel;

import org.springframework.stereotype.Repository;

import com.sun.xml.internal.bind.v2.TODO;

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
				new MemberId(model.getString("member_id")),
				model.getBoolean("admin"));

	}

	@Override
	public void save(GroupBelonging groupBelonging) {
		
		GroupMemberModel model = GroupMemberModel.findFirst("group_id=? and member_id=?", 
				groupBelonging.groupId().value(), groupBelonging.memberId().value());
		if (model == null) {
			model = new GroupMemberModel()
				.set("group_id", groupBelonging.groupId().value())
				.set("member_id", groupBelonging.memberId().value())
				.set("admin", groupBelonging.isAdmin());
		}
		
		model.saveIt();
	}


	@Override
	public GroupBelonging findById(MemberId memberId, GroupId groupId) {
		GroupMemberModel model = GroupMemberModel.findFirst("group_id=? and member_id=?", 
				groupId.value(), memberId.value());

		if(model == null) return null;
		return toDeomain(model);
	}
	
}
