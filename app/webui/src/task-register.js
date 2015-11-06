import {EventAggregator} from 'aurelia-event-aggregator';
import {EventAggregatorWrapper} from './lib/event-aggregator-wrapper';
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

  constructor(eventAggregator, taskService, memberServcie){
    this.taskService = taskService;
    this.events = new EventAggregatorWrapper(this, eventAggregator);
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
    this.events.subscribe('task-register.register', payload => {
      this.register();
    });
    this.events.subscribe(TaskRegistered, message => {
      this.events.publish('task-register.register.success');
    });
    this.events.subscribe('init.task.register', group => {
      this.group = group;
      this.members = this.memberServcie.loadByGroup(group.groupId);
    })
  }

}
