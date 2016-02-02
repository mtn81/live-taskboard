import {EventAggregator} from 'aurelia-event-aggregator';
import {EventAggregatorWrapper} from './lib/event-aggregator-wrapper';
import {inject} from 'aurelia-framework';
import {TaskService, TaskModified} from './task/task-service';

@inject(EventAggregator, TaskService)
export class TaskMemoEdit {
  task = null

  constructor(eventAggregator, taskService){
    this.taskService = taskService;
    this.events = new EventAggregatorWrapper(this, eventAggregator);
  }

  change() {
    this.taskService.modifyDetail(this.groupId, this.task);
    this.events.subscribe(TaskModified, () => {
      this.events.publish('task-memo-edit.change.success');
    });
  }

  attached() {
    this.events.subscribe('task-memo-edit.init', args => {
      this.groupId = args[0];
      this.taskId = args[1];
      this.taskService.loadDetail(this.groupId, this.taskId, task => {
        this.task = task;
      });
    });
    this.events.subscribe('task-memo-edit.change', () => {
      this.change();
    });
  }

}
