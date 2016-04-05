package jp.mts.taskmanage.domain.model.group;

import jp.mts.base.domain.model.DomainEvent;
import jp.mts.base.domain.model.DomainEventConfig;

@DomainEventConfig(eventType="mts:taskmanage/GroupRemoved")
public class GroupRemoved extends DomainEvent {

}
