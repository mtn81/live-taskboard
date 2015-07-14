package jp.mts.taskmanage.infrastructure.jdbc.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import jp.mts.base.unittest.JdbcTestBase;
import jp.mts.taskmanage.domain.model.Member;
import jp.mts.taskmanage.domain.model.MemberFixture;
import jp.mts.taskmanage.infrastructure.jdbc.repository.JdbcMemberRepository;

import org.junit.Test;

public class JdbcMemberRepositoryTest extends JdbcTestBase {

	JdbcMemberRepository memberRepository = new JdbcMemberRepository();

	@Test
	public void test_persistence() {
		Member member = new MemberFixture().get();
		memberRepository.save(member);
		
		Member found = memberRepository.findById(member.memberId());
		assertThat(member, is(found));
	}

}
