import {inject} from 'aurelia-framework';
import {HttpClient} from 'aurelia-http-client';
import {EventAggregator} from 'aurelia-event-aggregator';
import {GlobalError} from '../global-error';

var _tasks = {
  todo: [],
  doing: [],
  done: []
};

@inject(HttpClient, EventAggregator)
export class TaskService {

  constructor(http, eventAggregator) {
    this.http = http;
    this.eventAggregator = eventAggregator;
  }

  load(groupId) {

    this.http
      .get("/api/task-manage/groups/" + groupId + '/tasks/')
      .then(response => {
        let foundTasks = response.content.data;
        $.each(_tasks, (status, tasksInStatus) => {
          tasksInStatus.length = 0;
          $.merge(tasksInStatus, foundTasks[status]);
        });
      })
      .catch(response => {
        this.eventAggregator.publish(new GlobalError(response.content.errors));
      });

    return _tasks;
  }
}
