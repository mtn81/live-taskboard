package jp.mts.taskmanage.domain.model.group.join;

import java.util.Date;

import jp.mts.taskmanage.domain.model.group.GroupId;
import jp.mts.taskmanage.domain.model.group.join.GroupJoin;
import jp.mts.taskmanage.domain.model.group.join.GroupJoinId;
import jp.mts.taskmanage.domain.model.group.join.GroupJoinStatus;
import jp.mts.taskmanage.domain.model.member.MemberId;


public class GroupJoinFixture {

	private GroupJoin target;
	
	public GroupJoinFixture() {
		this("g01", "m01");
	}
	public GroupJoinFixture(String groupId, String memberId) {
		this("a01", groupId, memberId);
	}
	public GroupJoinFixture(String applicationId, String groupId, String memberId) {
		target = new GroupJoin(
				new GroupJoinId(applicationId), 
				new GroupId(groupId), 
				new MemberId(memberId));
	}
	public GroupJoinFixture setStatus(GroupJoinStatus status) {
		target.setStatus(status);
		return this;
	}
	public GroupJoinFixture setApplied(Date applied) {
		target.setApplied(applied);
		return this;
	}
	
	public GroupJoin get() {
		return target;
	}
}
