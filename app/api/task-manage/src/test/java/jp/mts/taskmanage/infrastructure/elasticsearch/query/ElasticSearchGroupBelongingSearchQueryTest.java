package jp.mts.taskmanage.infrastructure.elasticsearch.query;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import jp.mts.taskmanage.application.query.GroupBelongingSearchQuery.GroupSummary;
import jp.mts.taskmanage.domain.model.group.GroupFixture;
import jp.mts.taskmanage.domain.model.member.MemberFixture;
import jp.mts.taskmanage.infrastructure.elasticsearch.TaskManageESTestBase;
import jp.mts.taskmanage.infrastructure.elasticsearch.repository.ElasticSearchGroupRepository;
import jp.mts.taskmanage.infrastructure.elasticsearch.repository.ElasticSearchMemberRepository;

import org.junit.Before;
import org.junit.Test;

public class ElasticSearchGroupBelongingSearchQueryTest extends TaskManageESTestBase {

	ElasticSearchMemberRepository memberRepository;
	ElasticSearchGroupRepository groupRepository;
	ElasticSearchGroupBelongingSearchQuery target;
	
	@Before
	public void setup() {
		memberRepository = memberRepository();
		groupRepository = groupRepository();
		target = groupBelongingSearchQuery();
	}
	
	@Test
	public void test() {

		groupRepository.save(new GroupFixture("g01")
			.setName("group1")
			.get());
		groupRepository.save(new GroupFixture("g02")
			.setName("group2")
			.get());
		groupRepository.save(new GroupFixture("g03")
			.setName("group3")
			.get());
		
		memberRepository.save(new MemberFixture("m01")
			.addGroupBelonging("g01", true)
			.addGroupBelonging("g02", false)
			.get());
		
		await(1);
		
		List<GroupSummary> groupSummaries = target.byMember("m01");
		
		assertThat(groupSummaries.size(), is(2));
		assertThat(groupSummaries.get(0).groupId, is("g01"));
		assertThat(groupSummaries.get(0).groupName, is("group1"));
		assertThat(groupSummaries.get(0).isAdmin, is(true));
		assertThat(groupSummaries.get(1).groupId, is("g02"));
		assertThat(groupSummaries.get(1).groupName, is("group2"));
		assertThat(groupSummaries.get(1).isAdmin, is(false));
	}

}
