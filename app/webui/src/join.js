import {inject} from 'aurelia-framework';
import {EventAggregator} from 'aurelia-event-aggregator';
import {EventAggregatorWrapper} from './lib/event-aggregator-wrapper';
import {GroupJoinService, GroupJoinApplied, GroupJoinCancelled} from './group/group-join-service';
import 'components/jqueryui';

@inject(EventAggregator, GroupJoinService)
export class Join {
  notAppliedGroups = [];
  appliedGroups = [];
  notAppliedGroupsSearched = false;

  constructor(eventAggregator, groupJoinService){
    this.events = new EventAggregatorWrapper(this, eventAggregator);
    this.groupJoinService = groupJoinService;
  }

  searchNotAppliedGroup() {
    this.notAppliedGroups = this.groupJoinService.searchNotAppliedByName(this.groupName);
    this.notAppliedGroupsSearched = true;
  }

  searchAppliedGroup() {
    this.appliedGroups = this.groupJoinService.searchApplied();
  }

  applyJoin(group) {
    this.groupJoinService.applyJoin(group);
  }

  cancelJoin(group) {
    this.groupJoinService.cancelJoin(group);
  }

  formatTime(time) {
    let date = new Date(time);
    return `${date.getFullYear()}/${date.getMonth() + 1}/${date.getDate()} ${date.getHours()}:${date.getMinutes()}`;
  }

  activate() {
    this.events.subscribe2([GroupJoinApplied, GroupJoinCancelled], payload => {
      this.searchNotAppliedGroup();
      this.searchAppliedGroup();
    });

    this.searchAppliedGroup();
    this.notAppliedGroupsSearched = false;
  }

}
