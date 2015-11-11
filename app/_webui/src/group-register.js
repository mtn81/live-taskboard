import {EventAggregator} from 'aurelia-event-aggregator';
import {EventAggregatorWrapper} from './lib/event-aggregator-wrapper';
import {inject} from 'aurelia-framework';
import {GroupService, GroupRegistered} from './group/group-service';

@inject(EventAggregator, GroupService)
export class GroupRegister {

  groupName = '';
  groupDescription = '';

  constructor(eventAggregator, groupService){
    this.groupService = groupService;
    this.events = new EventAggregatorWrapper(this, eventAggregator);
  }

  register(){
    this.groupService.register({
      name: this.groupName,
      description: this.groupDescription
    });
  }

  attached(){
    this.events.subscribe('group-register.register', payload => {
      this.register();
    });
    this.events.subscribe(GroupRegistered, message => {
      this.events.publish('group-register.register.success');
    });
  }

}
