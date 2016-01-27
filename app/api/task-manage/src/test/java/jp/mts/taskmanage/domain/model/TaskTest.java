package jp.mts.taskmanage.domain.model;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Test;

public class TaskTest {

	@Test
	public void test_memo() {
		Task task = new TaskFixture().get();
		task.changeMemo("memo1");
		assertThat(task.memo(), is("memo1"));
	}

}
