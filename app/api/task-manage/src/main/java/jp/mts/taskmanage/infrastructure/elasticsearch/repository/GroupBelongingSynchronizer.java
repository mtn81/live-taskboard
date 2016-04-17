package jp.mts.taskmanage.infrastructure.elasticsearch.repository;

import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import jp.mts.base.infrastructure.elasticsearch.AbstractElasticSearchAccessor;
import jp.mts.base.util.MapUtils;
import jp.mts.taskmanage.domain.model.member.Member;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GroupBelongingSynchronizer extends AbstractElasticSearchAccessor {
	
	@Autowired
	public GroupBelongingSynchronizer(TransportClient transportClient) {
		super("task-manage", "v_group_belonging", transportClient);
	}
	
	public void syncOnMemberChanged(Member member) {
		if (member.groupBelongings().isEmpty()) return; 

		BulkRequestBuilder bulkRequestBuilder = transportClient.prepareBulk();
		
		prepareSearch()
			.setQuery(termQuery("member_id", member.id().value()))
			.get()
			.getHits().forEach(hit -> {
				bulkRequestBuilder.add(
						deleteRequest(hit.getId())
							.parent((String)hit.getSource().get("group_id")));
			});
		
		member.groupBelongings().forEach(gb -> {
			bulkRequestBuilder.add(indexRequest()
				.source(MapUtils.pairs(
					"member_id", member.id().value(),
					"group_id", gb.groupId().value(),
					"admin", gb.isAdmin()))
				.parent(gb.groupId().value()));
		});

		bulkRequestBuilder.get();
	}

}
