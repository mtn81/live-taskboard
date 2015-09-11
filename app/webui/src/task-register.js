import {EventAggregator} from 'aurelia-event-aggregator';
import {inject} from 'aurelia-framework';
import {TaskService, TaskRegistered} from './task/task-service';
import {MemberService} from './member/member-service';

@inject(EventAggregator, TaskService, MemberService)
export class TaskRegister {
  group = null;
  taskName = '';
  members = [];
  assignedMember = null;
  deadline = null;

  _subscription = [];

  constructor(eventAggregator, taskService, memberServcie){
    this.taskService = taskService;
    this.eventAggregator = eventAggregator;
    this.memberServcie = memberServcie;
  }

  register(){
    this.taskService.register(this.group.groupId, {
      taskName: this.taskName,
      assigned: this.assignedMember.memberId,
      deadline: this.deadline
    });
  }

  activate(taskboardContext){
    // this.taskboardContext = taskboardContext;
  }

  attached(){
    this._subscription.push(
      this.eventAggregator.subscribe('task-register.register', payload => {
        this.register();
      })
    );
    this._subscription.push(
      this.eventAggregator.subscribe(TaskRegistered, message => {
        this.eventAggregator.publish('task-register.register.success');
      })
    );

    this._subscription.push(
      this.eventAggregator.subscribe('group.selected', group => {
        this.group = group;
        this.members = this.memberServcie.loadByGroup(group.groupId);
      })
    );

    //this.taskboardContext.attachTaskRegister(this);
    this.eventAggregator.publish(new TaskRegisterAttached());
  }
  detached(){
    for(var i=0; i < this._subscription.length; i++){
      this._subscription[i]();
    }
  }

}

export class TaskRegisterAttached {}
