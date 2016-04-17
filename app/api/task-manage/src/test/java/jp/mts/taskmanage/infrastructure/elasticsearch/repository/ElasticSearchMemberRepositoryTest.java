package jp.mts.taskmanage.infrastructure.elasticsearch.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;
import java.util.Optional;

import jp.mts.taskmanage.domain.model.group.GroupId;
import jp.mts.taskmanage.domain.model.member.Member;
import jp.mts.taskmanage.domain.model.member.MemberFixture;
import jp.mts.taskmanage.domain.model.member.MemberId;
import jp.mts.taskmanage.infrastructure.elasticsearch.TaskManageESTestBase;

import org.junit.Before;
import org.junit.Test;

public class ElasticSearchMemberRepositoryTest extends TaskManageESTestBase {
	
	ElasticSearchMemberRepository target;
	
	@Before 
	public void setup() {
		target = memberRepository();
	}
	
	@Test public void 
	test_persistence() {

		Member member = new MemberFixture("m01")
			.setName("taro")
			.setEmail("taro@test.jp")
			.addGroupBelonging("g01", true)
			.get();

		target.save(member);

		Member found = target.findById(new MemberId("m01")).get();
		assertThat(found.id().value(), is("m01"));
		assertThat(found.name(), is("taro"));
		assertThat(found.email(), is("taro@test.jp"));
		assertThat(found.belongsAsAdmin(new GroupId("g01")), is(true));
		assertThat(found.belongsTo(new GroupId("g02")), is(false));
		
		target.remove(found);
		
		Optional<Member> notExists = target.findById(new MemberId("m01"));
		assertThat(notExists.isPresent(), is(false));
	}

	@Test public void 
	test_findByGroupId() {

		Member taro = new MemberFixture("m01")
			.setName("taro")
			.addGroupBelonging("g01", true)
			.addGroupBelonging("g02", false)
			.get();
		Member jiro = new MemberFixture("m02")
			.setName("jiro")
			.addGroupBelonging("g01", false)
			.get();
		Member saburo = new MemberFixture("m03")
			.setName("saburo")
			.get();

		target.save(taro);
		target.save(jiro);
		target.save(saburo);

		try { Thread.sleep(1000); } catch (InterruptedException e) {}; //wait indexing
		
		List<Member> found = target.findByGroupId(new GroupId("g01"));
		assertThat(found.size(), is(2));
		assertThat(found.get(0).name(), is("jiro"));
		assertThat(found.get(1).name(), is("taro"));
	}
	
}
