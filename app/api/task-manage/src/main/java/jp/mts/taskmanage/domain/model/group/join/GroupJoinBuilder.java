package jp.mts.taskmanage.domain.model.group.join;

import java.util.Date;


public class GroupJoinBuilder {

	private GroupJoin target;

	public GroupJoinBuilder(GroupJoin target) {
		this.target = target;
	}
	
	public GroupJoinBuilder setStatus(GroupJoinStatus status) {
		target.setStatus(status);
		return this;
	}
	public GroupJoinBuilder setApplied(Date applied) {
		target.setApplied(applied);
		return this;
	}
	
	public GroupJoin get() {
		return target;
	}
	
}
