package jp.mts.taskmanage.domain.model.group.join;

import java.sql.Date;

public class GroupJoinApplicationBuilder {

	private GroupJoinApplication target;

	public GroupJoinApplicationBuilder(GroupJoinApplication target) {
		this.target = target;
	}
	
	public GroupJoinApplicationBuilder setStatus(GroupJoinApplicationStatus status) {
		target.setStatus(status);
		return this;
	}
	public GroupJoinApplicationBuilder setApplied(Date applied) {
		target.setApplied(applied);
		return this;
	}
	
	public GroupJoinApplication get() {
		return target;
	}
	
}
