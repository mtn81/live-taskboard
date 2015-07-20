package jp.mts.taskmanage.infrastructure.jdbc.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import jp.mts.base.unittest.JdbcTestBase;
import jp.mts.taskmanage.domain.model.GroupBelonging;
import jp.mts.taskmanage.domain.model.GroupBelongingFixture;
import jp.mts.taskmanage.domain.model.MemberId;

import org.junit.Test;

public class JdbcGroupBelongingRepositoryTest extends JdbcTestBase {

	JdbcGroupBelongingRepository target = new JdbcGroupBelongingRepository();
	
	@Test
	public void test() {
		GroupBelonging groupBelonging = new GroupBelongingFixture("g01", "m01").get();

		target.save(groupBelonging);
		List<GroupBelonging> found = target.findByMember(new MemberId("m01"));

		assertThat(found.get(0), is(groupBelonging));
	}

}
