package jp.mts.taskmanage.application;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import jp.mts.libs.unittest.Dates;
import jp.mts.taskmanage.application.TaskAppService;
import jp.mts.taskmanage.domain.model.GroupFixture;
import jp.mts.taskmanage.domain.model.GroupId;
import jp.mts.taskmanage.domain.model.GroupRepository;
import jp.mts.taskmanage.domain.model.MemberFixture;
import jp.mts.taskmanage.domain.model.MemberId;
import jp.mts.taskmanage.domain.model.MemberRepository;
import jp.mts.taskmanage.domain.model.Task;
import jp.mts.taskmanage.domain.model.TaskFixture;
import jp.mts.taskmanage.domain.model.TaskId;
import jp.mts.taskmanage.domain.model.TaskRepository;
import jp.mts.taskmanage.domain.model.TaskStatus;
import mockit.Expectations;
import mockit.Injectable;
import mockit.NonStrictExpectations;
import mockit.Tested;

import org.javalite.test.jspec.Expectation;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.sun.org.apache.bcel.internal.generic.NEW;

public class TaskAppServiceTest {
	
	@Tested TaskAppService taskAppService;
	@Injectable TaskRepository taskRepository;
	@Injectable GroupRepository groupRepository;
	@Injectable MemberRepository memberRepository;

	@Test
	public void test_findTasksByGroup() {
		
		GroupId groupId = new GroupId("g01");
		Task task = new TaskFixture().get();
		new Expectations() {{
			taskRepository.findByGroupId(groupId);
				result = Lists.newArrayList(task);
		}};

		List<Task> tasks = taskAppService.findTasksByGroup(groupId.value());
		
		assertThat(tasks.size(), is(1));
		assertThat(tasks.get(0), is(task));
	}
	
	@Test
	public void test_registerTask() {
		
		MemberId memberId = new MemberId("m01");
		GroupId groupId = new GroupId("g01");
		TaskId taskId = new TaskId("t01");
		String taskName = "task-a";
		Date deadline = Dates.date("2015/09/01");
		
		new NonStrictExpectations() {{
			memberRepository.findById(memberId);
				result = Optional.of(new MemberFixture(memberId.value()).get()); 
				times=1;
			groupRepository.findById(groupId);
				result = new GroupFixture(groupId.value()).get(); times=1;
		}};
		new Expectations() {{
			taskRepository.newTaskId(groupId);
				result = taskId;
			taskRepository.save((Task)any);
		}};
		
		Task registeredTask = taskAppService.registerTask(
				groupId.value(), taskName, memberId.value(), deadline);
		
		assertThat(registeredTask, is(notNullValue()));
		assertThat(registeredTask.taskId(), is(taskId));
		assertThat(registeredTask.groupId(), is(groupId));
		assertThat(registeredTask.name(), is(taskName));
		assertThat(registeredTask.assignedMemberId(), is(memberId));
		assertThat(registeredTask.deadline(), is(deadline));
		
	}
	
	@Test
	public void test_removeTask() {
		GroupId groupId = new GroupId("g01");
		TaskId taskId = new TaskId("t01");
		Task task = new TaskFixture().get();
		
		new Expectations() {{
			taskRepository.findById(groupId, taskId);
				result = task;
				
			taskRepository.remove(task);
		}};
		
		taskAppService.removeTask(groupId.value(), taskId.value());
	}
	
	@Test
	public void test_modifyTask() {
		
		MemberId memberId = new MemberId("m01");
		GroupId groupId = new GroupId("g01");
		TaskId taskId = new TaskId("t01");
		String taskName = "task-a";
		Date deadline = Dates.date("2015/09/01");
		TaskStatus status = TaskStatus.DOING;
		
		new NonStrictExpectations() {{
			memberRepository.findById(memberId);
				result = Optional.of(new MemberFixture(memberId.value()).get()); 
				times=1;
		}};
		new Expectations() {{
			taskRepository.findById(groupId, taskId);
				result = new TaskFixture(groupId.value(), taskId.value()).get();
			taskRepository.save((Task)any);
		}};
		
		Task modifiedTask = taskAppService.modifyTask(
				groupId.value(), taskId.value(), taskName, memberId.value(), deadline, status);
		
		assertThat(modifiedTask, is(notNullValue()));
		assertThat(modifiedTask.taskId(), is(taskId));
		assertThat(modifiedTask.groupId(), is(groupId));
		assertThat(modifiedTask.name(), is(taskName));
		assertThat(modifiedTask.assignedMemberId(), is(memberId));
		assertThat(modifiedTask.deadline(), is(deadline));
		assertThat(modifiedTask.status(), is(status));
		
	}

}
