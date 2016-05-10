package jp.mts.taskmanage.rest.presentation.model;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import jp.mts.libs.unittest.Dates;
import jp.mts.taskmanage.application.query.TaskSearchQuery;
import jp.mts.taskmanage.domain.model.task.TaskStatus;
import mockit.Expectations;
import mockit.Mocked;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class TaskSearchTest {

	TaskSearch target = new TaskSearch();
	@Mocked TaskSearchQuery taskSearchQuery;
	
	@Before
	public void setup() {
		TaskSearch.setTaskSearchQuery(taskSearchQuery);
	}

	@Test public void 
	test() {
		new Expectations() {{
			taskSearchQuery.search("g01", "keywordA", newArrayList("m01", "m02"));
				result = Lists.newArrayList(
						new TaskSearchQuery.SearchResult("t01", "task1", Dates.date("2016/01/01"), "m01", TaskStatus.TODO, "aaa"),
						new TaskSearchQuery.SearchResult("t02", "task2", Dates.date("2016/01/02"), "m02", TaskStatus.DOING, "bbb"),
						new TaskSearchQuery.SearchResult("t03", "task3", Dates.date("2016/01/03"), "m03", TaskStatus.DOING, "ccc")
				);
		}};
		
		target.searchTasks("g01", "keywordA", "m01,m02");
		
		List<TaskSearch.TaskView> todoTasks = target.getTodo();
		assertThat(todoTasks.size(), is(1));
		assertThat(todoTasks.get(0).getTaskId(), is("t01"));
		assertThat(todoTasks.get(0).getTaskName(), is("task1"));
		assertThat(todoTasks.get(0).getDeadline(), is(Dates.date("2016/01/01")));
		assertThat(todoTasks.get(0).getAssigned(), is("m01"));
		assertThat(todoTasks.get(0).getStatus(), is("todo"));
		assertThat(todoTasks.get(0).getHilightMemo(), is("aaa"));
		
		List<TaskSearch.TaskView> doingTasks = target.getDoing();
		assertThat(doingTasks.size(), is(2));
		assertThat(doingTasks.get(0).getTaskId(), is("t02"));
		assertThat(doingTasks.get(0).getTaskName(), is("task2"));
		assertThat(doingTasks.get(0).getDeadline(), is(Dates.date("2016/01/02")));
		assertThat(doingTasks.get(0).getAssigned(), is("m02"));
		assertThat(doingTasks.get(0).getStatus(), is("doing"));
		assertThat(doingTasks.get(0).getHilightMemo(), is("bbb"));
		assertThat(doingTasks.get(1).getTaskId(), is("t03"));
		assertThat(doingTasks.get(1).getTaskName(), is("task3"));
		assertThat(doingTasks.get(1).getDeadline(), is(Dates.date("2016/01/03")));
		assertThat(doingTasks.get(1).getAssigned(), is("m03"));
		assertThat(doingTasks.get(1).getStatus(), is("doing"));
		assertThat(doingTasks.get(1).getHilightMemo(), is("ccc"));

		List<TaskSearch.TaskView> doneTasks = target.getDone();
		assertThat(doneTasks.size(), is(0));
	}

	@Test public void 
	test__search_with_empty_params() {
		new Expectations() {{
			taskSearchQuery.search("g01", null, Lists.newArrayList());
		}};
		
		target.searchTasks("g01", null, null);
	}

	@Test(expected=IllegalArgumentException.class) public void 
	test__error_with_empty_groupId() {
		target.searchTasks(null, null, null);
	}

}
