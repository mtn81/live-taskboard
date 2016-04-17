package jp.mts.taskmanage.infrastructure.elasticsearch;

import jp.mts.base.unittest.ElasticSearchTestBase;
import jp.mts.taskmanage.infrastructure.elasticsearch.query.ElasticSearchGroupBelongingSearchQuery;
import jp.mts.taskmanage.infrastructure.elasticsearch.query.ElasticSearchGroupJoinSearchQuery;
import jp.mts.taskmanage.infrastructure.elasticsearch.repository.ElasticSearchGroupJoinApplicationRepository;
import jp.mts.taskmanage.infrastructure.elasticsearch.repository.ElasticSearchGroupRepository;
import jp.mts.taskmanage.infrastructure.elasticsearch.repository.ElasticSearchMemberRepository;
import jp.mts.taskmanage.infrastructure.elasticsearch.repository.GroupBelongingSynchronizer;
import jp.mts.taskmanage.infrastructure.elasticsearch.repository.GroupOwnerSynchronizer;

import org.junit.Rule;

public abstract class TaskManageESTestBase extends ElasticSearchTestBase {

	@Rule 
	public ESClean esClean = new ESClean("task-manage", this);
	
	protected ElasticSearchMemberRepository memberRepository() {
		return new ElasticSearchMemberRepository(
				new GroupBelongingSynchronizer(transportClient()),
				new GroupOwnerSynchronizer(transportClient()),
				transportClient());
	}
	protected ElasticSearchGroupRepository groupRepository() {
		return new ElasticSearchGroupRepository(
				new GroupOwnerSynchronizer(transportClient()),
				transportClient());
	}
	protected ElasticSearchGroupBelongingSearchQuery groupBelongingSearchQuery() {
		return new ElasticSearchGroupBelongingSearchQuery(transportClient());
	}
	protected ElasticSearchGroupJoinApplicationRepository groupJoinApplicationRepository() {
		return new ElasticSearchGroupJoinApplicationRepository(transportClient());
	}
	protected ElasticSearchGroupJoinSearchQuery groupJoinSearchQuery() {
		return new ElasticSearchGroupJoinSearchQuery(transportClient());
	}
}
