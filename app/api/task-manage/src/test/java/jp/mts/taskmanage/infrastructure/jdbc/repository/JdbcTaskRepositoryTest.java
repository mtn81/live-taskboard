package jp.mts.taskmanage.infrastructure.jdbc.repository;

import static jp.mts.base.domain.model.CompositeId.of;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.Optional;

import jp.mts.base.domain.model.CompositeId;
import jp.mts.base.unittest.JdbcTestBase;
import jp.mts.libs.unittest.Dates;
import jp.mts.taskmanage.domain.model.group.GroupId;
import jp.mts.taskmanage.domain.model.member.MemberId;
import jp.mts.taskmanage.domain.model.task.Task;
import jp.mts.taskmanage.domain.model.task.TaskFixture;
import jp.mts.taskmanage.domain.model.task.TaskId;
import mockit.Tested;

import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class JdbcTaskRepositoryTest extends JdbcTestBase {

	@Tested JdbcTaskRepository target = new JdbcTaskRepository();
	
	@Test
	public void test_persistence() {
		target.save(new TaskFixture("g01", "t01")
			.setName("task01")
			.setDeadline(Dates.date("2015/01/02"))
			.setAssigned("m01")
			.setMemo("memo1")
			.get());
		
		Optional<Task> found = target.findById(
				CompositeId.of(new GroupId("g01"), new TaskId("t01")));
		
		assertThat(found.isPresent(), is(true));
		assertThat(found.get().taskId().value(), is("t01"));
		assertThat(found.get().groupId().value(), is("g01"));
		assertThat(found.get().name(), is("task01"));
		assertThat(found.get().deadline(), is(Dates.date("2015/01/02")));
		assertThat(found.get().assignedMemberId().value(), is("m01"));
		assertThat(found.get().memo(), is("memo1"));
	}
	
	@Test
	public void test_findByGroupId() {
		GroupId groupId = new GroupId("g01");
		
		target.save(new TaskFixture(groupId.value(), "t01")
			.setName("task01")
			.setDeadline(Dates.date("2015/01/02"))
			.setAssigned("m01")
			.get());
		target.save(new TaskFixture(groupId.value(), "t02")
			.setName("task02")
			.setDeadline(Dates.date("2015/01/03"))
			.setAssigned("m02")
			.get());
		
		List<Task> foundTasks = target.findByGroupId(groupId);
		
		assertThat(foundTasks.size(), is(2));
		
		Task task1 = foundTasks.get(0); 
		assertThat(task1.taskId(), is(new TaskId("t01")));
		assertThat(task1.groupId(), is(new GroupId("g01")));
		assertThat(task1.name(), is("task01"));
		assertThat(task1.deadline(), is(Dates.date("2015/01/02")));
		assertThat(task1.assignedMemberId(), is(new MemberId("m01")));
		
		Task task2 = foundTasks.get(1); 
		assertThat(task2.taskId(), is(new TaskId("t02")));
		assertThat(task2.groupId(), is(new GroupId("g01")));
		assertThat(task2.name(), is("task02"));
		assertThat(task2.deadline(), is(Dates.date("2015/01/03")));
		assertThat(task2.assignedMemberId(), is(new MemberId("m02")));
	}

	@Test
	public void test_newTaskId() {
		GroupId groupId = new GroupId("g01");

		TaskId newTaskId = target.newTaskId(groupId);
		assertThat(newTaskId.value(), is("TASK-1"));

		newTaskId = target.newTaskId(groupId);
		assertThat(newTaskId.value(), is("TASK-2"));
	}
	
	@Test
	public void test_removeTask() {
		
		GroupId groupId = new GroupId("g01");
		TaskId taskId = new TaskId("t01");
		target.save(new TaskFixture("g01", "t01").get());
		
		Optional<Task> task = target.findById(of(groupId, taskId));
		assertThat(task.isPresent(), is(true));

		target.remove(task.get());

		task = target.findById(of(groupId, taskId));
		assertThat(task.isPresent(), is(false));
	}
}
