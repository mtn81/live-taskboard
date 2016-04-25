package jp.mts.taskmanage.infrastructure.elasticsearch.repository;

import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.HashMap;
import java.util.Map;

import jp.mts.base.infrastructure.elasticsearch.AbstractElasticSearchAccessor;
import jp.mts.base.util.MapUtils;
import jp.mts.taskmanage.domain.model.group.Group;
import jp.mts.taskmanage.domain.model.member.Member;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.get.MultiGetRequestBuilder;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GroupBelongingViewSynchronizer extends AbstractElasticSearchAccessor {
	
	@Autowired
	public GroupBelongingViewSynchronizer(TransportClient transportClient) {
		super("task-manage", "view_group_belonging", transportClient);
	}
	
	public void syncFrom(Member member) {

		Map<String, String> groupNames = new HashMap<>();
		if (!member.groupBelongings().isEmpty()) {
			MultiGetRequestBuilder multiGetRequestBuilder = transportClient.prepareMultiGet();
			member.groupBelongings().forEach(gb -> {
				multiGetRequestBuilder.add(index, "group", gb.groupId().value());
			});
			MultiGetResponse multiGetResponse = multiGetRequestBuilder.get();
			multiGetResponse.forEach(item -> {
				Map<String, Object> group = item.getResponse().getSource();
				if(group != null) groupNames.put(item.getId(), (String)group.get("name"));
			});
		}


		BulkRequestBuilder bulkRequestBuilder = transportClient.prepareBulk();
		prepareSearch()
			.setQuery(constantScoreQuery(
				termQuery("member_id", member.id().value())	
			))
			.get()
			.getHits().forEach(hit -> {
				bulkRequestBuilder.add(deleteRequest(hit.getId()));
			});
		member.groupBelongings().forEach(gb -> {
			bulkRequestBuilder.add(
				indexRequest()
					.id(member.id().value() + "@" + gb.groupId().value())
					.source(MapUtils.pairs(
						"member_id", member.id().value(),
						"group_id", gb.groupId().value(),
						"group_name", groupNames.get(gb.groupId().value()),
						"admin", gb.isAdmin())
					)
			);
		});
		bulkRequestBuilder.get();
		
		//TODO verify group names unchanged
	}

	public void syncFrom(Group group) {

		BulkRequestBuilder bulkRequestBuilder = transportClient.prepareBulk();
		prepareSearch()
			.setQuery(constantScoreQuery(
				termQuery("group_id", group.id().value())	
			))
			.setVersion(true)
			.get()
			.getHits().forEach(hit -> {
				bulkRequestBuilder.add(
					updateRequest(hit.getId())
						.doc(MapUtils.pairs("group_name", group.name()))
						.version(hit.getVersion()));
			});
		
		if(bulkRequestBuilder.numberOfActions() <= 0) return;
		bulkRequestBuilder.get();
	}

}
