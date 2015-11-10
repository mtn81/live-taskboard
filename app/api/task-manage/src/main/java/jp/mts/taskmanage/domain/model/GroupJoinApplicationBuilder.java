package jp.mts.taskmanage.domain.model;

public class GroupJoinApplicationBuilder {

	private GroupJoinApplication target;

	public GroupJoinApplicationBuilder(GroupJoinApplication target) {
		this.target = target;
	}
	
	public GroupJoinApplicationBuilder setStatus(GroupJoinApplicationStatus status) {
		target.setStatus(status);
		return this;
	}
	
	public GroupJoinApplication get() {
		return target;
	}
	
}
