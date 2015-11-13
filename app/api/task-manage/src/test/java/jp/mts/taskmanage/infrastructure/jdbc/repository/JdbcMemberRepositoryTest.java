package jp.mts.taskmanage.infrastructure.jdbc.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.Optional;

import jp.mts.base.unittest.JdbcTestBase;
import jp.mts.taskmanage.domain.model.GroupId;
import jp.mts.taskmanage.domain.model.Member;
import jp.mts.taskmanage.domain.model.MemberFixture;
import jp.mts.taskmanage.domain.model.MemberId;
import jp.mts.taskmanage.infrastructure.jdbc.model.GroupMemberModel;

import org.junit.Test;

public class JdbcMemberRepositoryTest extends JdbcTestBase {

	JdbcMemberRepository memberRepository = new JdbcMemberRepository();
	
	@Test
	public void test_persistence() {
		Member member = new MemberFixture().get();
		memberRepository.save(member);
		
		Optional<Member> found = memberRepository.findById(member.memberId());
		assertThat(member, is(found.get()));
	}
	
	@Test
	public void test_findByGroupId() {
		memberRepository.save(new MemberFixture("m01", "taro").get());
		memberRepository.save(new MemberFixture("m02", "jiro").get());
		
		new GroupMemberModel().set(
				"group_id", "g01",
				"member_id", "m01",
				"admin", true).saveIt();
		new GroupMemberModel().set(
				"group_id", "g01",
				"member_id", "m02",
				"admin", true).saveIt();
		
		List<Member> members = memberRepository.findByGroupId(new GroupId("g01"));
		assertThat(members.size(), is(2));
		assertThat(members.get(0).memberId(), is(new MemberId("m01")));
		assertThat(members.get(0).name(), is("taro"));
		assertThat(members.get(1).memberId(), is(new MemberId("m02")));
		assertThat(members.get(1).name(), is("jiro"));
	}

}
