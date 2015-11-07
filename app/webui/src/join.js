import {inject} from 'aurelia-framework';
import {EventAggregator} from 'aurelia-event-aggregator';
import {EventAggregatorWrapper} from './lib/event-aggregator-wrapper';
import {GroupService} from './group/group-service';
import 'components/jqueryui';

@inject(EventAggregator, GroupService)
export class Join {

  constructor(eventAggregator, groupService){
    this.events = new EventAggregatorWrapper(this, eventAggregator);
    this.groupService = groupService;
  }

  searchGroup() {
    this.groupsSearched = this.groupService.searchByName(this.groupName);
  }
  applyJoin(group) {
    this.groupService.applyJoin(group);
  }
  
  activate() {
  
  }

}
