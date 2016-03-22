import 'components/jqueryui';
import {inject} from 'aurelia-framework';
import {Router} from 'aurelia-router';
import {EventAggregator} from 'aurelia-event-aggregator';
import {AuthContext} from '../../services/auth/auth-context';
import {EventAggregatorWrapper} from '../../lib/event-aggregator-wrapper';
import {TaskService} from '../../services/task/task-service';
import {TaskStatusAttached} from './task-status';
import {TaskRegisterAttached} from '../task-register/task-register';

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
    this.events.publish('init.task.register', this.group);
    $(this.taskRegisterModal).modal('show');
  }

  showMemoEdit(task) {
    this.events.publish('task-memo-edit.init', [ this.group.groupId, task.taskId ]);
    this.events.subscribe('task-memo-edit.enable', payload => {
      $(this.taskMemoEditModel).find('.btn-primary').removeAttr('disabled');
    });
    this.events.subscribe('task-memo-edit.disable', payload => {
      $(this.taskMemoEditModel).find('.btn-primary').attr('disabled', true);
    });
    $(this.taskMemoEditModel).modal('show');
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
