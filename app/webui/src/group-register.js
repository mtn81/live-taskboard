import {EventAggregator} from 'aurelia-event-aggregator';
import {inject} from 'aurelia-framework';
import {GroupService, GroupRegistered} from './group/group-service';

@inject(EventAggregator, GroupService)
export class GroupRegister {

  groupName = '';
  groupDescription = '';
  subscription = [];

  constructor(eventAggregator, groupService){
    this.groupService = groupService;
    this.eventAggregator = eventAggregator;

  }

  register(){
    this.groupService.register({
      name: this.groupName,
      description: this.groupDescription
    });
  }

  attached(){
    this.subscription.push(
      this.eventAggregator.subscribe('group-register.register', payload => {
        this.register();
      })
    );
    this.subscription.push(
      this.eventAggregator.subscribe(GroupRegistered, message => {
        this.eventAggregator.publish('group-register.register.success');
      })
    );
  }
  detached(){
    for(var i=0; i < this.subscription.length; i++){
      this.subscription[i]();
    }
  }

}
