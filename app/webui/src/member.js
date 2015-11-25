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

  changeToAdmin(member) {
    this.memberService.changeToAdmin(this.groupId, member);
  }
  changeToNormal(member) {
    this.memberService.changeToNormal(this.groupId, member);
  }

  activate(params) {
    this.groupId = params.groupId;
    this.members = this.memberService.loadByGroup(this.groupId);
  }

}
