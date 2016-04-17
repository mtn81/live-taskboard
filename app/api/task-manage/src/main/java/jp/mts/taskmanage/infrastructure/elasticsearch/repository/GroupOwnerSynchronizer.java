package jp.mts.taskmanage.infrastructure.elasticsearch.repository;

import static org.elasticsearch.index.query.QueryBuilders.termQuery;

import java.util.Map;

import jp.mts.base.infrastructure.elasticsearch.AbstractElasticSearchAccessor;
import jp.mts.base.util.MapUtils;
import jp.mts.taskmanage.domain.model.group.Group;
import jp.mts.taskmanage.domain.model.member.Member;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.springframework.test.annotation.Commit;

@Commit
public class GroupOwnerSynchronizer extends AbstractElasticSearchAccessor {

	public GroupOwnerSynchronizer(
			TransportClient transportClient) {
		super("task-manage", "v_group_owner", transportClient);
	}
	
	public void syncOnGroupChanged(Group group) {
		
		Map<String, Object> member = prepareGet("member", group.ownerMemberId().value())
			.setFetchSource(new String[]{"name", "type"}, null)
			.get().getSource();
		
		if (member == null) return;

		prepareIndex(group.id().value())
			.setSource(MapUtils.pairs(
				"member_id", group.ownerMemberId().value(),
				"member_name", member.get("name"),
				"member_type", member.get("type"),
				"group_name", group.name()))
			.get();
	}
	
	public void syncOnMemberChanged(Member member) {
		
		//TODO 非同期
		BulkRequestBuilder bulkRequestBuilder = transportClient.prepareBulk();
		
		prepareSearch()
			.setQuery(termQuery("member_id", member.id().value()))
			.get()
			.getHits()
			.forEach(hit -> {
				bulkRequestBuilder.add(updateRequest(hit.getId())
						.parent(hit.getId())
						.doc(MapUtils.pairs("member_name", member.name())));
			});
		
		if (bulkRequestBuilder.numberOfActions() <= 0) return;
		bulkRequestBuilder.get();
	}

}
