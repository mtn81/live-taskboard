import {inject} from 'aurelia-framework';
import {EventAggregator} from 'aurelia-event-aggregator';
import {EventAggregatorWrapper} from './lib/event-aggregator-wrapper';
import {MemberAcceptService, MemberRejected} from './member/member-accept-service';
import 'components/jqueryui';

@inject(EventAggregator, MemberAcceptService)
export class Accept {

  acceptableMembers = [];
  rejectedMembers = [];

  constructor(eventAggregator, memberAcceptService){
    this.events = new EventAggregatorWrapper(this, eventAggregator);
    this.memberAcceptService = memberAcceptService;
  }

  searchAcceptableMembers() {
    this.acceptableMembers = this.memberAcceptService.searchAcceptableMembers();
  }

  searchRejectedMembers() {
    this.rejectedMembers = this.memberAcceptService.searchRejectedMembers();
  }

  reject(member) {
    this.events.subscribe(MemberRejected, payload => {
      this.searchAcceptableMembers();
      this.searchRejectedMembers();
    });
    this.memberAcceptService.rejectMember(member);
  }

  activate() {
    this.searchAcceptableMembers();
    this.searchRejectedMembers();
  }

}
