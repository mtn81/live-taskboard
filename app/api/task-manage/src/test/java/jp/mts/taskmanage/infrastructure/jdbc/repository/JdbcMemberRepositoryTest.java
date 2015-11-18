package jp.mts.taskmanage.infrastructure.jdbc.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import jp.mts.base.unittest.JdbcTestBase;
import jp.mts.taskmanage.domain.model.GroupBelonging;
import jp.mts.taskmanage.domain.model.GroupId;
import jp.mts.taskmanage.domain.model.Member;
import jp.mts.taskmanage.domain.model.MemberFixture;
import jp.mts.taskmanage.domain.model.MemberId;

import org.junit.Test;

import com.google.common.collect.Sets;

public class JdbcMemberRepositoryTest extends JdbcTestBase {

	JdbcMemberRepository memberRepository = new JdbcMemberRepository();
	
	@Test
	public void test_persistence() {
		Member member = new MemberFixture()
			.addGroupBelonging("g01", true)
			.get();

		memberRepository.save(member);
		
		Member found = memberRepository.findById(member.memberId()).get();
		
		assertThat(found.memberId().value(), is("m01"));
		assertThat(found.name(), is("taro"));
		assertThat(found.groupBelongings(), 
				is(Sets.newHashSet(new GroupBelonging(new GroupId("g01"), true))));
	}
	
	@Test
	public void test_findByGroupId() {
		memberRepository.save(new MemberFixture("m01", "taro")
			.addGroupBelonging("g01", true)
			.get());
		memberRepository.save(new MemberFixture("m02", "jiro")
			.addGroupBelonging("g01", true)
			.get());
		
		List<Member> members = memberRepository.findByGroupId(new GroupId("g01"));

		assertThat(members.size(), is(2));
		assertThat(members.get(0).memberId(), is(new MemberId("m01")));
		assertThat(members.get(0).name(), is("taro"));
		assertThat(members.get(1).memberId(), is(new MemberId("m02")));
		assertThat(members.get(1).name(), is("jiro"));
	}

}
