import {EventAggregator} from 'aurelia-event-aggregator';
import {inject} from 'aurelia-framework';
import {GroupService, GroupRegistered} from './group/group-service';

@inject(EventAggregator, GroupService)
export class GroupRegister {

  groupName = '';
  groupDescription = '';

  constructor(eventAggregator, groupService){
    this.groupService = groupService;

    eventAggregator.subscribe('group-register.register', payload => {
      this.register();
    });
    eventAggregator.subscribe(GroupRegistered, message => {
      eventAggregator.publish('group-register.register.success');
    });
  }

  register(){
    this.groupService.register({
      name: this.groupName,
      description: this.groupDescription
    });
  }

}
