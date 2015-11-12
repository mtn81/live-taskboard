import {inject} from 'aurelia-framework';
import {EventAggregator} from 'aurelia-event-aggregator';
import {EventAggregatorWrapper} from './lib/event-aggregator-wrapper';
import {MemberAcceptService} from './member/member-accept-service';
import 'components/jqueryui';

@inject(EventAggregator, MemberAcceptService)
export class Accept {

  acceptableMembers = [];

  constructor(eventAggregator, memberAcceptService){
    this.events = new EventAggregatorWrapper(this, eventAggregator);
    this.memberAcceptService = memberAcceptService;
  }

  searchAcceptableMembers() {
    this.acceptableMembers = this.memberAcceptService.searchAcceptableMembers();
  }

  formatTime(time) {
    let date = new Date(time);
    return `${date.getFullYear()}/${date.getMonth() + 1}/${date.getDate()} ${date.getHours()}:${date.getMinutes()}`;
  }

  activate() {
    this.searchAcceptableMembers();
  }

}
