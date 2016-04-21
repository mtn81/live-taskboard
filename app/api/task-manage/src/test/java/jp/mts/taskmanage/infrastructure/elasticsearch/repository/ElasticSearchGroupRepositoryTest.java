package jp.mts.taskmanage.infrastructure.elasticsearch.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import jp.mts.taskmanage.domain.model.group.Group;
import jp.mts.taskmanage.domain.model.group.GroupFixture;
import jp.mts.taskmanage.domain.model.group.GroupId;
import jp.mts.taskmanage.domain.model.member.MemberFixture;
import jp.mts.taskmanage.infrastructure.elasticsearch.TaskManageESTestBase;

import org.junit.Before;
import org.junit.Test;

public class ElasticSearchGroupRepositoryTest extends TaskManageESTestBase {

	ElasticSearchMemberRepository memberRepository;
	ElasticSearchGroupRepository target;
	
	@Before
	public void setup() {
		memberRepository = memberRepository();
		target = groupRepository();
	}
	
	@Test public void 
	test_persistence() {
		memberRepository.save(new MemberFixture("m01").get());

		target.save(new GroupFixture("g01", "m01")
			.setName("test group")
			.setDescription("group description")
			.get());
		
		Group group = target.findById(new GroupId("g01")).get();
		
		assertThat(group.id().value(), is("g01"));
		assertThat(group.ownerMemberId().value(), is("m01"));
		assertThat(group.name(), is("test group"));
		assertThat(group.description(), is("group description"));
	}

}
