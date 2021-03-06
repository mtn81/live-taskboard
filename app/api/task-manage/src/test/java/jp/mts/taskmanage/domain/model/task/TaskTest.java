package jp.mts.taskmanage.domain.model.task;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import jp.mts.base.domain.model.DomainEventPublisher;
import jp.mts.base.domain.model.DomainObject;
import jp.mts.taskmanage.domain.model.member.Member;
import jp.mts.taskmanage.domain.model.member.MemberFixture;
import jp.mts.taskmanage.domain.model.task.Task;
import mockit.Mocked;

import org.junit.Test;

public class TaskTest {

	@Mocked DomainEventPublisher domainEventPublisher;

	@Test
	public void test_memo() {
		DomainObject.setDomainEventPublisher(domainEventPublisher);
		
		Task task = new TaskFixture().get();
		Member member = new MemberFixture().get();

		task.changeDetail("memo1", member);

		assertThat(task.memo(), is("memo1"));
	}

}
