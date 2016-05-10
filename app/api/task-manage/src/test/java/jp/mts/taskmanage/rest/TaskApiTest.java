package jp.mts.taskmanage.rest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Date;

import jp.mts.base.rest.RestResponse;
import jp.mts.libs.unittest.Dates;
import jp.mts.taskmanage.application.TaskAppService;
import jp.mts.taskmanage.domain.model.task.TaskFixture;
import jp.mts.taskmanage.domain.model.task.TaskStatus;
import jp.mts.taskmanage.rest.aspect.MemberContext;
import jp.mts.taskmanage.rest.presentation.model.TaskDetailLoad;
import jp.mts.taskmanage.rest.presentation.model.TaskRemove;
import jp.mts.taskmanage.rest.presentation.model.TaskSave;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Tested;

import org.junit.Test;

public class TaskApiTest {

	@Tested TaskApi target = new TaskApi();
	@Injectable TaskAppService taskAppService;
	
	@Test
	public void test_registerTask() {
		
		String groupId = "g01";
		
		new Expectations() {{
			taskAppService.registerTask(
					groupId, "task-A", "m01", Dates.date("2015/09/01"));
				result = new TaskFixture("t01").get();
		}};
		
		TaskSave taskSave = newTaskSave("task-A", "m01", Dates.date("2015/09/01"));
		RestResponse<TaskSave> response = target.registerTask(groupId, taskSave);
		
		assertThat(response.getData().getTaskId(), is("t01"));
	}

	
	@Test
	public void test_removeTask() {
		
		new Expectations() {{
			taskAppService.removeTask("g01", "t01");
				result = new TaskFixture("t01").get();
		}};

		RestResponse<TaskRemove> response = target.removeTask("g01", "t01");
		assertThat(response, is(notNullValue()));
		assertThat(response.getData().getTaskId(), is("t01"));
	}
	
	@Test
	public void test_modifyTask(@Mocked MemberContext memberContext) {
		
		new Expectations() {{
			MemberContext.memberId();
				result = "m00";
			taskAppService.modifyTask(
					"m00", "g01", "t01", "task-A", "m01", Dates.date("2015/09/01"), TaskStatus.TODO);
				result = new TaskFixture("t01").get();
		}};
		
		TaskSave taskSave = newTaskSave("task-A", "m01", Dates.date("2015/09/01"));
		RestResponse<TaskSave> response = target.modifyTask("g01", "t01", taskSave);
		
		assertThat(response.getData().getTaskId(), is("t01"));
	}
	
	@Test
	public void test_loadDetail() {
		target.initialize();
		
		new Expectations() {{
			taskAppService.loadById("g01", "t01");
				result = new TaskFixture("g01", "t01").setMemo("memo").get();
		}};
		
		RestResponse<TaskDetailLoad> response = target.loadTaskDetail("g01", "t01");
		TaskDetailLoad actual = response.getData();
		
		assertThat(actual.getTaskId(), is("t01"));
		assertThat(actual.getMemo(), is("memo"));
	}

	private TaskSave newTaskSave(
			String taskName, String assigned, Date deadline) {
		TaskSave taskSave = new TaskSave();
		taskSave.setTaskName(taskName);
		taskSave.setAssigned(assigned);
		taskSave.setDeadline(deadline);
		taskSave.setStatus("todo");
		return taskSave;
	}

}
