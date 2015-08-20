package jp.mts.taskmanage.application;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import jp.mts.taskmanage.application.TaskAppService;
import jp.mts.taskmanage.domain.model.GroupId;
import jp.mts.taskmanage.domain.model.Task;
import jp.mts.taskmanage.domain.model.TaskFixture;
import jp.mts.taskmanage.domain.model.TaskRepository;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;

import org.junit.Test;

import com.google.common.collect.Lists;

public class TaskAppServiceTest {
	
	@Tested TaskAppService taskAppService;
	@Injectable TaskRepository taskRepository;

	@Test
	public void test() {
		
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

}
