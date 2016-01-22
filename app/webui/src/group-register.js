import {EventAggregator} from 'aurelia-event-aggregator';
import {EventAggregatorWrapper} from './lib/event-aggregator-wrapper';
import {inject} from 'aurelia-framework';
import {GroupService, GroupRegistered, GroupModified, GroupValidationSuccess, GroupValidationError} from './group/group-service';
import {GlobalInfo} from './global-info';

@inject(EventAggregator, GroupService)
export class GroupRegister {

  groupId = null;
  name = '';
  description = '';

  constructor(eventAggregator, groupService){
    this.groupService = groupService;
    this.events = new EventAggregatorWrapper(this, eventAggregator);
  }

  register(){
    this.groupService.register({
      name: this.name,
      description: this.description
    });
  }

  modify(){
    this.groupService.modify({
      groupId: this.groupId,
      name: this.name,
      description: this.description
    });
  }

  validate() {
    this.groupService.validate({
      name: this.name,
      description: this.description
    });
  }

  attached(){
    this.events.subscribe('group-register.init', groupId => {
      if(!!groupId){
        this.groupService.group(groupId, group => {
          this.groupId = group.groupId;
          this.name = group.name;
          this.description = group.description;
          this.validate();
        });
      } else {
          this.groupId = null;
          this.name = '';
          this.description = '';
          this.validate();
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
    this.events.subscribe(GroupModified, message => {
      this.events.publish('group-register.change.success');
    });
    this.events.subscribe('validate.group.register', () => {
      this.validate();
    });
    this.events.subscribe(GroupValidationError, e => {
      this.events.publish('validate.group.register.error', e.error);
      this.events.publish('group-register.disable');
    });
    this.events.subscribe(GroupValidationSuccess, e => {
      this.events.publish('group-register.enable');
    });
  }

}
