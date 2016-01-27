import {EventAggregator} from 'aurelia-event-aggregator';
import {EventAggregatorWrapper} from './lib/event-aggregator-wrapper';
import {inject} from 'aurelia-framework';
import {TaskService} from './task/task-service';

@inject(EventAggregator, TaskService)
export class TaskMemoEdit {
  memo = '';

  constructor(eventAggregator, taskService){
    this.taskService = taskService;
    this.events = new EventAggregatorWrapper(this, eventAggregator);
  }

  change() {

  }

  attached() {
    this.events.subscribe('task-memo-edit.init', args => {
      this.groupId = args[0];
      this.taskId = args[1];

      this.taskService.loadDetail(this.groupId, this.taskId, task => {
        this.memo = task.memo;
      });
    });
  }

}
