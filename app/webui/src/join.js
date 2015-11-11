import {inject} from 'aurelia-framework';
import {EventAggregator} from 'aurelia-event-aggregator';
import {EventAggregatorWrapper} from './lib/event-aggregator-wrapper';
import {GroupService, GroupJoinApplied} from './group/group-service';
import 'components/jqueryui';

@inject(EventAggregator, GroupService)
export class Join {

  constructor(eventAggregator, groupService){
    this.events = new EventAggregatorWrapper(this, eventAggregator);
    this.groupService = groupService;
  }

  searchNotAppliedGroup() {
    this.notAppliedGroups = this.groupService.searchNotAppliedByName(this.groupName);
  }

  searchAppliedGroup() {
    this.appliedGroups = this.groupService.searchApplied();
  }

  applyJoin(group) {
    this.groupService.applyJoin(group);
  }

  formatTime(time) {
    let date = new Date(time);
    return `${date.getFullYear()}/${date.getMonth() + 1}/${date.getDate()} ${date.getHours()}:${date.getMinutes()}`;
  }
  
  activate() {
    this.events.subscribe(GroupJoinApplied, payload => {
      this.searchNotAppliedGroup();
      this.searchAppliedGroup();
    });

    this.searchAppliedGroup();
  }

}
