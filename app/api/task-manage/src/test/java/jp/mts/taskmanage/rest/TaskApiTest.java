package jp.mts.taskmanage.rest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import jp.mts.taskmanage.application.TaskAppService;
import jp.mts.taskmanage.domain.model.TaskFixture;
import jp.mts.taskmanage.domain.model.TaskStatus;
import jp.mts.taskmanage.rest.presentation.model.TaskList;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.sun.prism.impl.Disposer.Target;

public class TaskApiTest {

	@Tested TaskApi target = new TaskApi();
	@Injectable TaskAppService taskAppService;
	
	@Test
	public void test() {
		
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

}
