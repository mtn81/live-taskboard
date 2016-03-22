import 'components/jqueryui';
import {inject} from 'aurelia-framework';
import {EventAggregator} from 'aurelia-event-aggregator';
import {EventAggregatorWrapper} from '../../lib/event-aggregator-wrapper';
import {MemberService, MemberRemoved} from '../../services/member/member-service';
import {GroupService, GroupLoaded} from '../../services/group/group-service';

@inject(EventAggregator, MemberService, GroupService)
export class Member {

  group = {};
  members = [];

  constructor(eventAggregator, memberService, groupService){
    this.events = new EventAggregatorWrapper(this, eventAggregator);
    this.memberService = memberService;
    this.groupService = groupService;
  }

  changeToAdmin(member) {
    this.memberService.changeToAdmin(this.groupId, member);
  }
  changeToNormal(member) {
    this.memberService.changeToNormal(this.groupId, member);
  }
  removeMember(member) {
    this.memberService.remove(this.groupId, member);
  }
  loadMembers() {
    this.members = this.memberService.loadByGroup(this.groupId);
  }

  activate(params) {
    this.groupId = params.groupId;
    this.group = this.groupService.group(this.groupId);

    this.events.subscribe2([GroupLoaded, MemberRemoved], () => {
      this.loadMembers();
    });
  }
  

}
