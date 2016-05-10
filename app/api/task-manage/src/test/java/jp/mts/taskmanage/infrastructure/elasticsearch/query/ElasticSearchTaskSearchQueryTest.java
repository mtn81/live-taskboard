package jp.mts.taskmanage.infrastructure.elasticsearch.query;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import jp.mts.libs.unittest.Dates;
import jp.mts.taskmanage.application.query.TaskSearchQuery.SearchResult;
import jp.mts.taskmanage.domain.model.task.TaskFixture;
import jp.mts.taskmanage.domain.model.task.TaskRepository;
import jp.mts.taskmanage.domain.model.task.TaskStatus;
import jp.mts.taskmanage.infrastructure.elasticsearch.TaskManageESTestBase;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class ElasticSearchTaskSearchQueryTest extends TaskManageESTestBase {

	ElasticSearchTaskSearchQuery target;
	TaskRepository taskRepository;
	
	@Before
	public void setup(){
		target = taskSearchQuery();
		taskRepository = taskRepository();
	}
	
	@Test public void 
	test() {
		taskRepository.save(new TaskFixture("g01", "t01")
			.setAssigned("m01")
			.setName("task A")
			.setMemo("This is a test task")
			.setDeadline(Dates.date("2016/01/01"))
			.get());
		taskRepository.save(new TaskFixture("g01", "t02")
			.setAssigned("m02")
			.setName("test task")
			.setMemo("This is a task")
			.setDeadline(Dates.date("2016/01/02"))
			.get());
		taskRepository.save(new TaskFixture("g99", "t03") //group unmatch
			.setAssigned("m01")
			.setName("task A")
			.setMemo("This is a test task")
			.setDeadline(Dates.date("2016/01/01"))
			.get());
		taskRepository.save(new TaskFixture("g01", "t04")
			.setAssigned("m99") //assigned unmatch
			.setName("task A")
			.setMemo("This is a test task")
			.setDeadline(Dates.date("2016/01/01"))
			.get());
		taskRepository.save(new TaskFixture("g01", "t05") 
			.setAssigned("m01") 
			.setName("task A") //keyword unmatch
			.setMemo("This is a task") //keyword unmatch
			.setDeadline(Dates.date("2016/01/01"))
			.get());
		
		await(1);
		
		List<SearchResult> found = target.search("g01", "test", Lists.newArrayList("m01", "m02"));
		
		assertThat(found.size(), is(2));

		assertThat(found.get(0).getTaskId(), is("t01"));
		assertThat(found.get(0).getAssigned(), is("m01"));
		assertThat(found.get(0).getDeadline(), is(Dates.date("2016/01/01")));
		assertThat(found.get(0).getHilightMemo(), is("This is a <em>test</em> task"));
		assertThat(found.get(0).getStatus(), is(TaskStatus.TODO));
		assertThat(found.get(0).getTaskName(), is("task A"));

		assertThat(found.get(1).getTaskId(), is("t02"));
		assertThat(found.get(1).getAssigned(), is("m02"));
		assertThat(found.get(1).getDeadline(), is(Dates.date("2016/01/02")));
		assertThat(found.get(1).getHilightMemo(), is(nullValue()));
		assertThat(found.get(1).getStatus(), is(TaskStatus.TODO));
		assertThat(found.get(1).getTaskName(), is("test task"));
	}

}
