package jp.mts.taskmanage.infrastructure.elasticsearch.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import jp.mts.libs.unittest.Dates;
import jp.mts.taskmanage.domain.model.group.GroupId;
import jp.mts.taskmanage.domain.model.task.Task;
import jp.mts.taskmanage.domain.model.task.TaskFixture;
import jp.mts.taskmanage.domain.model.task.TaskId;
import jp.mts.taskmanage.domain.model.task.TaskStatus;
import jp.mts.taskmanage.infrastructure.elasticsearch.TaskManageESTestBase;

import org.junit.Before;
import org.junit.Test;

public class ElasticSearchTaskRepositoryTest extends TaskManageESTestBase {
	
	ElasticSearchTaskRepository target;
	
	@Before 
	public void SetupContext() {
		target = elasticSearchTaskRepository();
	}

	@Test public void 
	test_create_task_id() {
		assertThat(target.newTaskId(new GroupId("g01")).value(), is("TASK-1"));
		assertThat(target.newTaskId(new GroupId("g01")).value(), is("TASK-2"));
		assertThat(target.newTaskId(new GroupId("g02")).value(), is("TASK-1"));
	}

	@Test public void 
	test_persistence() {
		
		target.save(new TaskFixture("g01", "t01")
			.setName("task1")
			.setMemo("memo1")
			.setDeadline(Dates.date("2016/01/02"))
			.setAssigned("m01")
			.setStatus(TaskStatus.TODO)
			.get());

		Task found = target.findById(new GroupId("g01"), new TaskId("t01")).get();
		
		assertThat(found.taskId().value(), is("t01"));
		assertThat(found.groupId().value(), is("g01"));
		assertThat(found.name(), is("task1"));
		assertThat(found.memo(), is("memo1"));
		assertThat(found.deadline(), is(Dates.date("2016/01/02")));
		assertThat(found.assignedMemberId().value(), is("m01"));
		assertThat(found.status(), is(TaskStatus.TODO));
		
		target.remove(found);

		assertThat(target.findById(new GroupId("g01"), new TaskId("t01")).isPresent(), is(false));
	}
}
