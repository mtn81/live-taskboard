import {customElement, inject, bindable} from 'aurelia-framework';
import {EventAggregator} from 'aurelia-event-aggregator';
import {EventAggregatorWrapper} from './lib/event-aggregator-wrapper';
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

  constructor(eventAggregator, widgetManager, taskService, memberService) {
    this.events = new EventAggregatorWrapper(this, eventAggregator);
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

  deadlineCss(deadline) {
    if (!deadline) return '';

    const now = new Date();
    const nowTime = new Date(now.getFullYear(), now.getMonth(), now.getDate());
    const deadlineTime = new Date(`${deadline} 00:00:00`).getTime();
    if (nowTime > deadlineTime) {
      return 'task-deadline-error';
    }
    if (deadlineTime - nowTime <= 3 * 24 * 60 * 60 * 1000) {
      return 'task-deadline-warn';
    }
    return '';
  }

  bind(bindingContext) {
    //this.taskboardContext = bindingContext.taskboardContext;
  }

  attached() {

    this.events.subscribe('group.selected', group => {
      this.group = group;
      this.members = this.memberService.loadByGroup(group.groupId);
      this.tasks = this.taskService.load(this.group.groupId, this.status);
    });
    this.events.subscribe2(
      ['task-register.register.success', TaskRemoved, TaskModified],
      () => {
        this.tasks = this.taskService.load(this.group.groupId, this.status);
      }
    );

  }

}

