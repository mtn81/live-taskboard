package jp.mts.taskmanage.infrastructure.elasticsearch.query;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import jp.mts.taskmanage.application.query.GroupJoinSearchQuery.ByApplicantResult;
import jp.mts.taskmanage.domain.model.group.GroupFixture;
import jp.mts.taskmanage.domain.model.group.join.GroupJoinApplicationFixture;
import jp.mts.taskmanage.domain.model.group.join.GroupJoinApplicationStatus;
import jp.mts.taskmanage.domain.model.member.MemberFixture;
import jp.mts.taskmanage.infrastructure.elasticsearch.TaskManageESTestBase;
import jp.mts.taskmanage.infrastructure.elasticsearch.repository.ElasticSearchGroupJoinApplicationRepository;
import jp.mts.taskmanage.infrastructure.elasticsearch.repository.ElasticSearchGroupRepository;
import jp.mts.taskmanage.infrastructure.elasticsearch.repository.ElasticSearchMemberRepository;

import org.junit.Before;
import org.junit.Test;

public class ElasticSearchGroupJoinSearchQueryTest extends TaskManageESTestBase {
	
	ElasticSearchGroupJoinSearchQuery target;
	ElasticSearchGroupRepository groupRepository;
	ElasticSearchMemberRepository memberRepository;
	ElasticSearchGroupJoinApplicationRepository groupJoinApplicationRepository;
	
	@Before
	public void setup() {
		target = groupJoinSearchQuery();
		groupRepository = groupRepository();
		memberRepository = memberRepository();
		groupJoinApplicationRepository = groupJoinApplicationRepository();
	}

	@Test
	public void test() {
		
		memberRepository.save(new MemberFixture("m01", "owner01").get());
		memberRepository.save(new MemberFixture("m02", "owner02").get());
		memberRepository.save(new MemberFixture("m03", "applicant01").get());
		memberRepository.save(new MemberFixture("m04", "applicant02").get());

		groupRepository.save(new GroupFixture("g01", "m01").setName("group01").get());
		groupRepository.save(new GroupFixture("g02", "m02").setName("group02").get());
		groupRepository.save(new GroupFixture("g03", "m02").setName("group03").get());
		
		groupJoinApplicationRepository.save(new GroupJoinApplicationFixture("a99", "g03", "m03")
			.setStatus(GroupJoinApplicationStatus.REJECTED)
			.get());

		groupJoinApplicationRepository.save(new GroupJoinApplicationFixture("a01", "g01", "m03")
			.setStatus(GroupJoinApplicationStatus.ACCEPTED)
			.get());

		groupJoinApplicationRepository.save(new GroupJoinApplicationFixture("a02", "g02", "m03")
			.setStatus(GroupJoinApplicationStatus.CANCELLED)
			.get());

		groupJoinApplicationRepository.save(new GroupJoinApplicationFixture("a03", "g03", "m04")
			.setStatus(GroupJoinApplicationStatus.ACCEPTED)
			.get());

		await(1);

		List<ByApplicantResult> found = target.byApplicant("m03");
		
		assertThat(found.size(), is(2));

		assertThat(found.get(0).groupId, is("g03"));
		assertThat(found.get(0).groupName, is("group03"));
		assertThat(found.get(0).joinApplicationId, is("a99"));
		assertThat(found.get(0).joinApplicationStatus, is(GroupJoinApplicationStatus.REJECTED));
		assertThat(found.get(0).ownerName, is("owner02"));
		assertThat(found.get(0).ownerType, is("PROPER"));

		assertThat(found.get(1).groupId, is("g01"));
		assertThat(found.get(1).groupName, is("group01"));
		assertThat(found.get(1).joinApplicationId, is("a01"));
		assertThat(found.get(1).joinApplicationStatus, is(GroupJoinApplicationStatus.ACCEPTED));
		assertThat(found.get(1).ownerName, is("owner01"));
		assertThat(found.get(1).ownerType, is("PROPER"));

	}


}
