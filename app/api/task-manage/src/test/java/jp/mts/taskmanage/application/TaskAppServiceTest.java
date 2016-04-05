package jp.mts.taskmanage.application;

import static jp.mts.base.domain.model.CompositeId.of;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import jp.mts.base.domain.model.CompositeId;
import jp.mts.base.domain.model.DomainEventPublisher;
import jp.mts.base.domain.model.DomainObject;
import jp.mts.libs.unittest.Dates;
import jp.mts.taskmanage.domain.model.group.Group;
import jp.mts.taskmanage.domain.model.group.GroupFixture;
import jp.mts.taskmanage.domain.model.group.GroupId;
import jp.mts.taskmanage.domain.model.group.GroupRepository;
import jp.mts.taskmanage.domain.model.member.MemberFixture;
import jp.mts.taskmanage.domain.model.member.MemberId;
import jp.mts.taskmanage.domain.model.member.MemberRepository;
import jp.mts.taskmanage.domain.model.task.Task;
import jp.mts.taskmanage.domain.model.task.TaskFixture;
import jp.mts.taskmanage.domain.model.task.TaskId;
import jp.mts.taskmanage.domain.model.task.TaskRepository;
import jp.mts.taskmanage.domain.model.task.TaskStatus;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.Tested;

import org.junit.Test;

import com.google.common.collect.Lists;

public class TaskAppServiceTest {
	
	@Tested TaskAppService taskAppService;
	@Injectable TaskRepository taskRepository;
	@Injectable GroupRepository groupRepository;
	@Injectable MemberRepository memberRepository;
	@Mocked DomainEventPublisher domainEventPublisher;

	@Test
	public void test_loadById() {
		Task expected = new TaskFixture().get();
		new Expectations() {{
			taskRepository.findById(CompositeId.of(new GroupId("g01"), new TaskId("t01")));
				result = Optional.of(expected);
		}};

		Task actual = taskAppService.loadById("g01", "t01");
		assertThat(actual, is(expected));
	}
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
				result = Optional.of(new GroupFixture(groupId.value()).get()); 
				times=1;
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
		Group group = new GroupFixture().get();

		new Expectations() {{
			groupRepository.findById(groupId);
				result = Optional.of(group);
			taskRepository.findById(of(groupId, taskId));
				result = Optional.of(task);
				
			taskRepository.remove(task);
		}};
		DomainObject.setDomainEventPublisher(domainEventPublisher);
		
		taskAppService.removeTask(groupId.value(), taskId.value());
	}
	
	@Test
	public void test_modifyTask() {
		DomainObject.setDomainEventPublisher(domainEventPublisher);
		
		MemberId modifierMemberId = new MemberId("m00");
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
			memberRepository.findById(modifierMemberId);
				result = Optional.of(new MemberFixture(modifierMemberId.value()).get()); 
				times=1;
		}};
		new Expectations() {{
			taskRepository.findById(of(groupId, taskId));
				result = Optional.of(new TaskFixture(groupId.value(), taskId.value()).get());
			taskRepository.save((Task)any);
		}};
		
		Task modifiedTask = taskAppService.modifyTask(
				modifierMemberId.value(), groupId.value(), taskId.value(), taskName, memberId.value(), deadline, status);
		
		assertThat(modifiedTask, is(notNullValue()));
		assertThat(modifiedTask.taskId(), is(taskId));
		assertThat(modifiedTask.groupId(), is(groupId));
		assertThat(modifiedTask.name(), is(taskName));
		assertThat(modifiedTask.assignedMemberId(), is(memberId));
		assertThat(modifiedTask.deadline(), is(deadline));
		assertThat(modifiedTask.status(), is(status));
		
	}

}
