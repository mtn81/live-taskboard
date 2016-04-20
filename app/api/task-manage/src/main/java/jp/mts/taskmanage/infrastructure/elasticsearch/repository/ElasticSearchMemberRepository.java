package jp.mts.taskmanage.infrastructure.elasticsearch.repository;

import static org.elasticsearch.index.query.QueryBuilders.termQuery;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import jp.mts.base.infrastructure.elasticsearch.AbstractElasticSearchRepository;
import jp.mts.base.util.ListUtils;
import jp.mts.base.util.MapUtils;
import jp.mts.taskmanage.domain.model.group.GroupId;
import jp.mts.taskmanage.domain.model.member.GroupBelonging;
import jp.mts.taskmanage.domain.model.member.Member;
import jp.mts.taskmanage.domain.model.member.MemberBuilder;
import jp.mts.taskmanage.domain.model.member.MemberId;
import jp.mts.taskmanage.domain.model.member.MemberRegisterType;
import jp.mts.taskmanage.domain.model.member.MemberRepository;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ElasticSearchMemberRepository 
	extends AbstractElasticSearchRepository
	implements MemberRepository {
	
	private GroupBelongingViewSynchronizer groupBelongingViewSynchronizer;
	private GroupJoinByApplicantViewSynchronizer groupJoinByApplicantViewSynchronizer;
	private GroupJoinToAdminViewSynchronizer groupJoinToAdminViewSynchronizer;
	private GroupSearchViewSynchronizer groupSearchViewSynchronizer;

	@Autowired
	public ElasticSearchMemberRepository(
			GroupBelongingViewSynchronizer groupBelongingSynchronizer,
			GroupJoinByApplicantViewSynchronizer groupJoinByApplicantViewSynchronizer,
			GroupJoinToAdminViewSynchronizer groupJoinToAdminViewSynchronizer,
			GroupSearchViewSynchronizer groupSearchViewSynchronizer,
			TransportClient transportClient) {
		super("task-manage", "member", transportClient);
		this.groupBelongingViewSynchronizer = groupBelongingSynchronizer;
		this.groupJoinByApplicantViewSynchronizer = groupJoinByApplicantViewSynchronizer;
		this.groupJoinToAdminViewSynchronizer = groupJoinToAdminViewSynchronizer;
		this.groupSearchViewSynchronizer = groupSearchViewSynchronizer;
	}

	@Override
	public Optional<Member> findById(MemberId id) {
		return getDomain(id.value(), this::toDomain);
	}

	@Override
	public void save(Member member) {
		save(member.id().value(), member, m ->  MapUtils.pairs(
			"member_id", m.memberId().value(),
			"name", m.name(),
			"type", m.registerType().name(),
			"email", m.email(),
			"belongings", ListUtils.convert(m.groupBelongings(), 
							gb -> MapUtils.pairs(
									"group_id", gb.groupId().value(),
									"admin", gb.isAdmin()))
		));

		groupBelongingViewSynchronizer.syncFrom(member);
		groupJoinByApplicantViewSynchronizer.syncFrom(member);
		groupJoinToAdminViewSynchronizer.syncFrom(member);
		groupSearchViewSynchronizer.syncFrom(member);
	}

	@Override
	public void remove(Member member) {
		delete(member.id().value());
	}

	@Override
	public List<Member> findByGroupId(GroupId groupId) {
		SearchResponse searchResponse = prepareSearch()
			.setQuery(termQuery("belongings.group_id", groupId.value()))
			.addSort("name", SortOrder.ASC)
			.get();
		
		return Arrays.stream(searchResponse.getHits().getHits())
				.map(hit -> toDomain(hit.getSource()))
				.collect(Collectors.toList());
	}
	
	Member toDomain(Map<String, Object> source) {
		 return new MemberBuilder(
				new Member(
					new MemberId((String)source.get("member_id")),
					(String)source.get("name"),
					MemberRegisterType.valueOf((String)source.get("type"))))
			.setEmail((String)source.get("email"))
			.addGroupBelongings(ListUtils.convert(
					(List<Map<String, Object>>)source.get("belongings"), 
					belonging -> new GroupBelonging(
						new GroupId((String)belonging.get("group_id")), 
						(Boolean)belonging.get("admin"))))
		 	.get();
	}

}
