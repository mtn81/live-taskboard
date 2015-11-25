import {inject} from 'aurelia-framework';
import {EventAggregator} from 'aurelia-event-aggregator';
import {EventAggregatorWrapper} from './lib/event-aggregator-wrapper';
import {MemberService} from './member/member-service';
import 'components/jqueryui';

@inject(EventAggregator, MemberService)
export class Member {

  members = [];

  constructor(eventAggregator, memberService){
    this.events = new EventAggregatorWrapper(this, eventAggregator);
    this.memberService = memberService;
  }

  activate(params) {
    this.members = this.memberService.loadByGroup(params.groupId);
  }

}
