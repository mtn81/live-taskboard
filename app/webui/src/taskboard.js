import {inject} from 'aurelia-framework';
import {Router} from 'aurelia-router';
import {AuthContext} from './auth/auth-context';
import {EventAggregator} from 'aurelia-event-aggregator';
import {GroupService} from './group/group-service';
import {WidgetManager} from './widget/widget-manager';
import {TaskService} from './task/task-service';
import {TaskStatusAttached} from './task-status';
import {TaskRegisterAttached} from './task-register';
import 'components/jqueryui';

@inject(Router, AuthContext, EventAggregator, GroupService, WidgetManager, TaskService)
export class Taskboard {

  groups = [];
  registeringGroups = [];

  constructor(router, authContext, eventAggregator, groupService, widgetManager, taskService){
    this.router = router;
    this.authContext = authContext;
    this.eventAggregator = eventAggregator;
    this.groupService = groupService;
    this.widgetManager = widgetManager;
    this.taskService = taskService;
    this.attachStatus = new AttachStatus(this);
  }

  showGroupRegister(){
    $(this.groupRegisterModal).modal('show');
  }

  showTaskRegister(){
    $(this.taskRegisterModal).modal('show');
  }

  removeGroup(group){
    this.groupService.remove(group);
  }

  selectGroup(group){
    if(group){
      this.group = group;
    } else {
      if(this.groups.length === 0) return;
      this.group = this.groups[0];
    }

    this.widgetManager.load(this.group.groupId, () => {
      this.eventAggregator.publish('widget.reloaded');
    });
    this._loadTasks();

    this.eventAggregator.publish('group.selected', this.group);
  }

  fire(eventId, hideTarget){
    this.eventAggregator.subscribe(eventId + '.success', payload => {
      $(hideTarget).modal('hide');
    });
    this.eventAggregator.publish(eventId);
  }

  //life cycle methods
  canActivate() {
  }

  activate() {
    this.eventAggregator.subscribe(TaskStatusAttached, e => {
      this.attachStatus.attachTaskStatus(e.status);
    });
    this.eventAggregator.subscribe(TaskRegisterAttached, e => {
      this.attachStatus.attachTaskRegister();
    });
    this.eventAggregator.subscribe('task-register.register.success', message => {
      this._loadTasks();
    });
    this.eventAggregator.subscribe('task-status.remove.success', message => {
      this._loadTasks();
    });
    this.eventAggregator.subscribe('task-status.modify.success', message => {
      this._loadTasks();
    });

    let promiseHolder = {};
    this.groups = this.groupService.groups(promiseHolder);
    this.registeringGroups = this.groupService.registeringGroups();

    return promiseHolder.promise;
  }

  _loadTasks() {
    this.taskService.load(this.group.groupId, (tasks) => {
      this.eventAggregator.publish('tasks.reloaded', tasks);
    });
  }

  toggleMenu(menu){
    var icon = $(menu).find("span.glyphicon");
    icon.toggleClass('glyphicon-menu-left');
    icon.toggleClass('glyphicon-menu-right');
  }

}

class AttachStatus {

  taskboard = null;
  todoAttached = false;
  doingAttached = false;
  doneAttached = false;
  registerAttached = false;

  constructor(taskboard) {
    this.taskboard = taskboard;
  }

  attachTaskStatus(status) {
    if(status == 'todo') this.todoAttached = true;
    if(status == 'doing') this.doingAttached = true;
    if(status == 'done') this.doneAttached = true;
    this._initTaskboard();
  }
  attachTaskRegister() {
    this.registerAttached = true;
    this._initTaskboard();
  }

  _initTaskboard() {
    if(this.todoAttached && this.doingAttached && this.doneAttached && this.registerAttached){
      this.taskboard.selectGroup();
    }
  }

}
