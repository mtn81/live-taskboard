package jp.mts.taskmanage.rest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Date;

import jp.mts.libs.unittest.Dates;
import jp.mts.taskmanage.application.TaskAppService;
import jp.mts.taskmanage.domain.model.TaskFixture;
import jp.mts.taskmanage.domain.model.TaskStatus;
import jp.mts.taskmanage.rest.presentation.model.TaskList;
import jp.mts.taskmanage.rest.presentation.model.TaskRemove;
import jp.mts.taskmanage.rest.presentation.model.TaskSave;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;

import org.junit.Test;

import com.google.common.collect.Lists;

public class TaskApiTest {

	@Tested TaskApi target = new TaskApi();
	@Injectable TaskAppService taskAppService;
	
	@Test
	public void test_loadTasksByGroup() {
		
		new Expectations() {{
			taskAppService.findTasksByGroup("g01");
				result = Lists.newArrayList(
						new TaskFixture("t01").setStatus(TaskStatus.TODO).get(),
						new TaskFixture("t02").setStatus(TaskStatus.DOING).get(),
						new TaskFixture("t03").setStatus(TaskStatus.DOING).get(),
						new TaskFixture("t04").setStatus(TaskStatus.DONE).get(),
						new TaskFixture("t05").setStatus(TaskStatus.DONE).get(),
						new TaskFixture("t06").setStatus(TaskStatus.DONE).get());
		}};
		
		RestResponse<TaskList> response = target.loadTasksInGroup("g01");

		TaskList tasks = response.getData();

		assertThat(tasks.getTodo().size(), is(1));
		assertThat(tasks.getTodo().get(0).getTaskId(), is("t01"));

		assertThat(tasks.getDoing().size(), is(2));
		assertThat(tasks.getDoing().get(0).getTaskId(), is("t02"));
		assertThat(tasks.getDoing().get(1).getTaskId(), is("t03"));

		assertThat(tasks.getDone().size(), is(3));
		assertThat(tasks.getDone().get(0).getTaskId(), is("t04"));
		assertThat(tasks.getDone().get(1).getTaskId(), is("t05"));
		assertThat(tasks.getDone().get(2).getTaskId(), is("t06"));
		
	}
	
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
	public void test_modifyTask() {
		
		new Expectations() {{
			taskAppService.modifyTask(
					"g01", "t01", "task-A", "m01", Dates.date("2015/09/01"), TaskStatus.TODO);
				result = new TaskFixture("t01").get();
		}};
		
		TaskSave taskSave = newTaskSave("task-A", "m01", Dates.date("2015/09/01"));
		RestResponse<TaskSave> response = target.modifyTask("g01", "t01", taskSave);
		
		assertThat(response.getData().getTaskId(), is("t01"));
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
