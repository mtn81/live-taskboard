import {EventAggregator} from 'aurelia-event-aggregator';
import {EventAggregatorWrapper} from './lib/event-aggregator-wrapper';
import {inject} from 'aurelia-framework';
import {GroupService, GroupRegistered} from './group/group-service';
import {GlobalInfo} from './global-info';

@inject(EventAggregator, GroupService)
export class GroupRegister {

  group = null;

  constructor(eventAggregator, groupService){
    this.groupService = groupService;
    this.events = new EventAggregatorWrapper(this, eventAggregator);
  }

  register(){
    this.groupService.register(this.group);
  }

  attached(){
    this.events.subscribe('group-register.init', group => {
      this.group = {};
    });
    this.events.subscribe('group-register.register', payload => {
      this.register();
    });
    this.events.subscribe('group-register.edit', group => {
      this.group = this.groupService.group(group.groupId);
    });
    this.events.subscribe('group-register.change', payload => {
      console.log(this.editGroup);
      this.modify();
    });
    this.events.subscribe(GroupRegistered, message => {
      this.events.publish('group-register.register.success');
      this.events.publish(new GlobalInfo([{message: 'グループが利用可能になるまで少々お待ち下さい'}]));
    });
  }

}
