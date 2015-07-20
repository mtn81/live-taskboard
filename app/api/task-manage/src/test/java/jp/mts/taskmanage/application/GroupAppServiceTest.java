package jp.mts.taskmanage.application;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import jp.mts.base.application.ApplicationException;
import jp.mts.taskmanage.domain.model.Group;
import jp.mts.taskmanage.domain.model.GroupBelonging;
import jp.mts.taskmanage.domain.model.GroupBelongingFixture;
import jp.mts.taskmanage.domain.model.GroupBelongingRepository;
import jp.mts.taskmanage.domain.model.GroupFixture;
import jp.mts.taskmanage.domain.model.GroupId;
import jp.mts.taskmanage.domain.model.GroupRepository;
import jp.mts.taskmanage.domain.model.Member;
import jp.mts.taskmanage.domain.model.MemberFixture;
import jp.mts.taskmanage.domain.model.MemberId;
import jp.mts.taskmanage.domain.model.MemberRepository;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;

import org.junit.Test;

public class GroupAppServiceTest {

	@Tested GroupAppService target = new GroupAppService();
	@Injectable GroupRepository groupRepository;
	@Injectable MemberRepository memberRepository;
	@Injectable GroupBelongingRepository groupBelongingRepository;

	@Test
	public void test_group_creation() {
		new Expectations() {{
			memberRepository.findById(new MemberId("m01"));
				result = new MemberFixture().get();
			groupRepository.newGroupId();
				result = new GroupId("g01");
				
			groupRepository.save((Group)any);
		}};

		Group group = target.register("m01", "group01", "this is a test group");
		
		assertThat(group.groupId().value(), is("g01"));
		assertThat(group.ownerMemberId().value(), is("m01"));
		assertThat(group.name(), is("group01"));
		assertThat(group.description(), is("this is a test group"));
	}
	@Test
	public void test_member_absent_error_on_group_creation() {
		new Expectations() {{
			memberRepository.findById(new MemberId("m01"));
				result = null;
		}};

		try{
			target.register("m01", "group01", "this is a test group");
		}catch(ApplicationException e){
			assertThat(e.hasErrorOf(ErrorType.MEMBER_NOT_EXIST), is(true));
		}
	}

	@Test
	public void test_list_belonging_group() {

		new Expectations() {{
			groupBelongingRepository.findByMember(new MemberId("m01"));
				result = newArrayList(
						new GroupBelongingFixture("g01", "m01").get(),
						new GroupBelongingFixture("g02", "m01").get());

			groupRepository.findByIds(newArrayList(new GroupId("g01"), new GroupId("g02")));
				result = newArrayList(
						new GroupFixture("g01").get(), 
						new GroupFixture("g02").get());
		}};
		List<Group> groups = target.listBelonging("m01");
		
		assertThat(groups.size(), is(2));
		assertThat(groups.get(0).groupId().value(), is("g01"));
		assertThat(groups.get(1).groupId().value(), is("g02"));
	}
	
	@Test
	public void test_participate_admin() {
		
		GroupId groupId = new GroupId("g01");
		Group group = new GroupFixture(groupId.value()).get();
		MemberId memberId = new MemberId("m01");
		Member member = new MemberFixture(memberId.value()).get();
		GroupBelonging groupBelonging = new GroupBelongingFixture("g01", "m01").get();
		
		new Expectations() {{
			groupRepository.findById(groupId);
				result = group;
			memberRepository.findById(memberId);
				result = member;
			groupBelongingRepository.save(groupBelonging);
		}};
		
		target.entryMember(groupId, memberId);
	}
}
