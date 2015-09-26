import {customElement, inject, bindable} from 'aurelia-framework';
import {EventAggregator} from 'aurelia-event-aggregator';
import bootbox from 'bootbox';
import {WidgetManager} from './widget/widget-manager';
import {TaskService, TaskRemoved, TaskModified} from './task/task-service';
import {MemberService} from './member/member-service';
import 'components/jqueryui';
import 'components/jqueryui/themes/base/jquery-ui.css!';


@customElement('task-status')
@inject(EventAggregator, WidgetManager, TaskService, MemberService)
export class TaskStatus {
  @bindable status = '';
  tasks = [];
  members = [];
  _subscription = [];

  constructor(eventAggregator, widgetManager, taskService, memberService) {
    this.eventAggregator = eventAggregator;
    this.widgetManager = widgetManager;
    this.taskService = taskService;
    this.memberService = memberService;
    this.vm = this;
  }

  removeTask(task) {
    bootbox.confirm('削除します。よろしいですか？', confirmation => {
      if(confirmation) {
        this.taskService.remove(this.group.groupId, task.taskId);
      }
    });
  }

  modifyTask(task) {
    this.taskService.modify(this.group.groupId, task);
  }

  changeStatus(taskId) {
    this.taskService.changeStatus(this.group.groupId, taskId, this.status);
  }

  bind(bindingContext) {
    //this.taskboardContext = bindingContext.taskboardContext;
  }

  attached() {
    var me = this;

    this._subscription.push(
      this.eventAggregator.subscribe('tasks.reloaded', (tasks) => {
        this.tasks.length = 0;
        $.merge(this.tasks, tasks[this.status]);
      })
    );

    this._subscription.push(
      this.eventAggregator.subscribe('group.selected', group => {
        this.group = group;
        this.members = this.memberService.loadByGroup(group.groupId);
      })
    );
    this._subscription.push(
      this.eventAggregator.subscribe(TaskRemoved, () => {
        this.eventAggregator.publish('task-status.remove.success');
      })
    );
    this._subscription.push(
      this.eventAggregator.subscribe(TaskModified, () => {
        this.eventAggregator.publish('task-status.modify.success');
      })
    );

    this.eventAggregator.publish(new TaskStatusAttached(this.status));
  }

  detached(){
    for(var i=0; i < this._subscription.length; i++){
      this._subscription[i]();
    }
  }

}

export class TaskStatusAttached {
  constructor(status){
    this.status = status;
  }
}
