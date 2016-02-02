import {EventAggregator} from 'aurelia-event-aggregator';
import {EventAggregatorWrapper} from './lib/event-aggregator-wrapper';
import {inject} from 'aurelia-framework';
import {TaskService, TaskModified, TaskValidationSuccess, TaskValidationError} from './task/task-service';

@inject(EventAggregator, TaskService)
export class TaskMemoEdit {
  task = null
  memo = '';

  constructor(eventAggregator, taskService){
    this.taskService = taskService;
    this.events = new EventAggregatorWrapper(this, eventAggregator);
  }

  change() {
    this.task.memo = this.memo;
    this.taskService.modifyDetail(this.groupId, this.task);
    this.events.subscribe(TaskModified, () => {
      this.events.publish('task-memo-edit.change.success');
    });
  }
  validate() {
    this.task.memo = this.memo;
    this.taskService.validateDetail(this.groupId, this.task);
    this.events.subscribe(TaskValidationSuccess, () => {
      this.events.publish('task-memo-edit.enable');
    });
    this.events.subscribe(TaskValidationError, e => {
      this.events.publish('validate.task.edit.error', e.error);
      this.events.publish('task-memo-edit.disable');
    });
  }

  attached() {
    this.events.subscribe('task-memo-edit.init', args => {
      this.groupId = args[0];
      this.taskId = args[1];
      this.taskService.loadDetail(this.groupId, this.taskId, task => {
        this.task = task;
        this.memo = task.memo;
        this.validate();
      });
    });
    this.events.subscribe('task-memo-edit.change', () => {
      this.change();
    });
    this.events.subscribe('validate.task.edit', () => {
      this.validate();
    });
  }

}
