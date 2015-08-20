import {inject} from 'aurelia-framework';
import {Router} from 'aurelia-router';
import {AuthContext} from './auth/auth-context';
import {EventAggregator} from 'aurelia-event-aggregator';
import {GroupService} from './group/group-service';
import {WidgetService} from './widget/widget-service';
import {TaskService} from './task/task-service';
import {TaskStatusAttached} from './task-status';
import 'components/jqueryui';

@inject(Router, AuthContext, EventAggregator, GroupService, WidgetService, TaskService)
export class Taskboard {

  groups = [];
  registeringGroups = [];

  constructor(router, authContext, eventAggregator, groupService, widgetService, taskService){
    this.router = router;
    this.authContext = authContext;
    this.eventAggregator = eventAggregator;
    this.groupService = groupService;
    this.widgetService = widgetService;
    this.taskService = taskService;
  }

  showGroupRegister(){
    $(this.groupRegisterModal).modal('show');
  }

  removeGroup(group){
    this.groupService.remove(group);
  }

  selectGroup(group){
    this.group = group;
    this.widgets = this.widgetService.getStore(this.group.groupId);
    this.tasks = this.taskService.load(this.group.groupId);
  }

  fire(eventId){
    this.eventAggregator.subscribe(eventId + '.success', payload => {
      $(this.groupRegisterModal).modal('hide');
    });
    this.eventAggregator.publish(eventId);
  }

  //life cycle methods
  canActivate() {
  }

  activate() {
    this.registeringGroups = this.groupService.registeringGroups();

    // カスタムエレメントの準備ができるまで待機
    this.eventAggregator.subscribe(TaskStatusAttached, payload => {
      if(this.groups.length > 0)
        this.selectGroup(this.groups[0]);
    });

    return this.groupService.groups(groups => {
      this.groups = groups;
    });
  }

  toggleMenu(menu){
    var icon = $(menu).find("span.glyphicon");
    icon.toggleClass('glyphicon-menu-left');
    icon.toggleClass('glyphicon-menu-right');
  }

}

