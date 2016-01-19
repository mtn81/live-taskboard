import {EventAggregator} from 'aurelia-event-aggregator';
import {EventAggregatorWrapper} from './lib/event-aggregator-wrapper';
import {inject} from 'aurelia-framework';
import {GroupService, GroupRegistered} from './group/group-service';
import {GlobalInfo} from './global-info';

@inject(EventAggregator, GroupService)
export class GroupRegister {

  groupId = null;
  groupName = '';
  description = '';

  constructor(eventAggregator, groupService){
    this.groupService = groupService;
    this.events = new EventAggregatorWrapper(this, eventAggregator);
  }

  register(){
    this.groupService.register({
      name: this.groupName,
      description: this.description
    });
  }

  modify(){
    this.groupService.register({
      groupId: this.groupId,
      name: this.groupName,
      description: this.description
    });
  }

  attached(){
    this.events.subscribe('group-register.init', groupId => {
      if(!!groupId){
        this.groupService.group(groupId, group => {
          this.groupId = group.groupId;
          this.groupName = group.name;
          this.description = group.description;
        });
      }
    });
    this.events.subscribe('group-register.register', payload => {
      this.register();
    });
    this.events.subscribe('group-register.change', payload => {
      this.modify();
    });
    this.events.subscribe(GroupRegistered, message => {
      this.events.publish('group-register.register.success');
      this.events.publish(new GlobalInfo([{message: 'グループが利用可能になるまで少々お待ち下さい'}]));
    });
  }

}
