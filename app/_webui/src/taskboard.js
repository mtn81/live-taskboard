import {inject} from 'aurelia-framework';
import {Router} from 'aurelia-router';
import {AuthContext} from './auth/auth-context';
import {EventAggregator} from 'aurelia-event-aggregator';
import {EventAggregatorWrapper} from './lib/event-aggregator-wrapper';
import {TaskService} from './task/task-service';
import {TaskStatusAttached} from './task-status';
import {TaskRegisterAttached} from './task-register';
import 'components/jqueryui';

@inject(Router, AuthContext, EventAggregator, TaskService)
export class Taskboard {

  group = null;

  constructor(router, authContext, eventAggregator, taskService){
    this.router = router;
    this.authContext = authContext;
    this.events = new EventAggregatorWrapper(this, eventAggregator);
    this.taskService = taskService;
  }

  showTaskRegister(){
    $(this.taskRegisterModal).on('shown.bs.modal', () => {
      this.events.publish('init.task.register', this.group);
    });

    $(this.taskRegisterModal).modal('show');
  }

  fire(eventId, hideTarget){
    this.events.subscribe(eventId + '.success', payload => {
      $(hideTarget).modal('hide');
    });
    this.events.publish(eventId);
  }

  //life cycle methods
  bind() {
  }
  canActivate() {
  }

  activate() {
    this.events.subscribe('group.selected', group => {
      this.group = group;
    });
  }

  get isGroupSelected() {
    return !!this.group;
  }

}
