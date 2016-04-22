package jp.mts.taskmanage.infrastructure.elasticsearch.query;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import jp.mts.taskmanage.application.query.GroupJoinSearchQuery.AppliableGroupResult;
import jp.mts.taskmanage.application.query.GroupJoinSearchQuery.ByAdminResult;
import jp.mts.taskmanage.application.query.GroupJoinSearchQuery.ByApplicantResult;
import jp.mts.taskmanage.domain.model.group.GroupFixture;
import jp.mts.taskmanage.domain.model.group.join.GroupJoinFixture;
import jp.mts.taskmanage.domain.model.group.join.GroupJoinStatus;
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

	@Test public void 
	test_byApplicant() {
		
		memberRepository.save(new MemberFixture("m01", "owner01").get());
		memberRepository.save(new MemberFixture("m02", "owner02").get());
		memberRepository.save(new MemberFixture("m03", "applicant01").get());
		memberRepository.save(new MemberFixture("m04", "applicant02").get());

		groupRepository.save(new GroupFixture("g01", "m01").setName("group01").get());
		groupRepository.save(new GroupFixture("g02", "m02").setName("group02").get());
		groupRepository.save(new GroupFixture("g03", "m02").setName("group03").get());
		
		groupJoinApplicationRepository.save(new GroupJoinFixture("a99", "g03", "m03")
			.setStatus(GroupJoinStatus.REJECTED)
			.get());

		groupJoinApplicationRepository.save(new GroupJoinFixture("a01", "g01", "m03")
			.setStatus(GroupJoinStatus.ACCEPTED)
			.get());

		groupJoinApplicationRepository.save(new GroupJoinFixture("a02", "g02", "m03")
			.setStatus(GroupJoinStatus.CANCELLED)
			.get());

		groupJoinApplicationRepository.save(new GroupJoinFixture("a03", "g03", "m04")
			.setStatus(GroupJoinStatus.ACCEPTED)
			.get());

		await(1);

		List<ByApplicantResult> found = target.byApplicant("m03");
		
		assertThat(found.size(), is(2));

		assertThat(found.get(0).groupId, is("g03"));
		assertThat(found.get(0).groupName, is("group03"));
		assertThat(found.get(0).joinApplicationId, is("a99"));
		assertThat(found.get(0).joinApplicationStatus, is(GroupJoinStatus.REJECTED));
		assertThat(found.get(0).ownerName, is("owner02"));
		assertThat(found.get(0).ownerType, is("PROPER"));

		assertThat(found.get(1).groupId, is("g01"));
		assertThat(found.get(1).groupName, is("group01"));
		assertThat(found.get(1).joinApplicationId, is("a01"));
		assertThat(found.get(1).joinApplicationStatus, is(GroupJoinStatus.ACCEPTED));
		assertThat(found.get(1).ownerName, is("owner01"));
		assertThat(found.get(1).ownerType, is("PROPER"));

	}

	@Test public void 
	test_acceptableByAdmin() {

		groupRepository.save(new GroupFixture("g01").setName("applied1").get());
		groupRepository.save(new GroupFixture("g02").setName("applied2").get());
		groupRepository.save(new GroupFixture("g03").setName("applied3").get());
		groupRepository.save(new GroupFixture("g04").setName("applied4").get());
		groupRepository.save(new GroupFixture("g05").setName("not applied").get());
		
		await(1);
		
		memberRepository.save(new MemberFixture("m01")
			.addGroupBelonging("g01", true)
			.addGroupBelonging("g02", true)
			.addGroupBelonging("g03", false)
			.get());
		memberRepository.save(new MemberFixture("m02", "applicant02").get());
		memberRepository.save(new MemberFixture("m03", "applicant03").get());
		
		await(1);
		
		groupJoinApplicationRepository.save(new GroupJoinFixture("a01", "g01", "m02").get());
		groupJoinApplicationRepository.save(new GroupJoinFixture("a02", "g02", "m02").get());
		groupJoinApplicationRepository.save(new GroupJoinFixture("a03", "g03", "m02").get());
		groupJoinApplicationRepository.save(new GroupJoinFixture("a04", "g04", "m02").get());
		groupJoinApplicationRepository.save(new GroupJoinFixture("a05", "g05", "m02").get());
		groupJoinApplicationRepository.save(new GroupJoinFixture("a06", "g01", "m03").get());
		
		await(1);
		
		List<ByAdminResult> found = target.acceptableByAdmin("m01");
		
		assertThat(found.size(), is(3));

		assertThat(found.get(0).joinApplicationId, is("a01"));
		assertThat(found.get(0).applicantId, is("m02"));
		assertThat(found.get(0).applicantName, is("applicant02"));
		assertThat(found.get(0).applicantType, is("PROPER"));
		assertThat(found.get(0).groupId, is("g01"));
		assertThat(found.get(0).groupName, is("applied1"));

		assertThat(found.get(1).joinApplicationId, is("a02"));
		assertThat(found.get(1).applicantId, is("m02"));
		assertThat(found.get(1).applicantName, is("applicant02"));
		assertThat(found.get(1).applicantType, is("PROPER"));
		assertThat(found.get(1).groupId, is("g02"));
		assertThat(found.get(1).groupName, is("applied2"));

		assertThat(found.get(2).joinApplicationId, is("a06"));
		assertThat(found.get(2).applicantId, is("m03"));
		assertThat(found.get(2).applicantName, is("applicant03"));
		assertThat(found.get(2).applicantType, is("PROPER"));
		assertThat(found.get(2).groupId, is("g01"));
		assertThat(found.get(2).groupName, is("applied1"));
	}

	@Test public void 
	test_appliableGroups() {
		
		memberRepository.save(new MemberFixture("m01").setName("owner01").get());
		memberRepository.save(new MemberFixture("m02").setName("owner02").get());

		groupRepository.save(new GroupFixture("g01", "m01").setName("テスト グループ").setDescription("group01").get());
		groupRepository.save(new GroupFixture("g02", "m02").setName("テスト").setDescription("group02").get());
		groupRepository.save(new GroupFixture("g03", "m01").setName("テスト グループ3").get());
		groupRepository.save(new GroupFixture("g04", "m01").setName("テスト グループ4").get());
		groupRepository.save(new GroupFixture("g05", "m02").setName("ほげ").get());

		await(1);
		
		memberRepository.save(new MemberFixture("m99").setName("searcher").addGroupBelonging("g03", true).get());
		groupJoinApplicationRepository.save(new GroupJoinFixture("a01", "g04", "m99").get());

		await(1);
		
		List<AppliableGroupResult> found = target.appliableGroups("m99", "テスト");
		
		assertThat(found.size(), is(2));
		assertThat(found.get(0).getGroupId(), is("g02"));
		assertThat(found.get(0).getGroupName(), is("テスト"));
		assertThat(found.get(0).getDescription(), is("group02"));
		assertThat(found.get(0).getOwnerName(), is("owner02"));
		assertThat(found.get(0).getOwnerType(), is("PROPER"));
		assertThat(found.get(1).getGroupId(), is("g01"));
		assertThat(found.get(1).getGroupName(), is("テスト グループ"));
		assertThat(found.get(1).getDescription(), is("group01"));
		assertThat(found.get(1).getOwnerName(), is("owner01"));
		assertThat(found.get(1).getOwnerType(), is("PROPER"));
		

	}
}
