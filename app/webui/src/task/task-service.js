import {inject} from 'aurelia-framework';
import {HttpClient} from 'aurelia-http-client';
import {EventAggregator} from 'aurelia-event-aggregator';
import {GlobalError} from '../global-error';

var _tasks = {
  todo: [],
  doing: [],
  done: []
};

var _loading = false;

var _allTasks = function(){
  var tasks = [];
  $.each(_tasks, (status, tasksInStatus) => {
    $.merge(tasks, tasksInStatus);
  });
  return tasks;
};

var _taskOf = function(taskId){
  var result;
  _allTasks().forEach(t => {
    if(t.taskId === taskId) result = t;
  })
  return result;
};

@inject(HttpClient, EventAggregator)
export class TaskService {

  constructor(http, eventAggregator) {
    this.http = http;
    this.eventAggregator = eventAggregator;
  }

  load(groupId, callback) {

    if(_loading) return _tasks;

    _loading = true;

    this.http
      .get('/api/task-manage/groups/' + groupId + '/tasks/')
      .then(response => {
        let foundTasks = response.content.data;
        $.each(_tasks, (status, tasksInStatus) => {
          tasksInStatus.length = 0;
          $.merge(tasksInStatus, foundTasks[status]);
        });
        _loading = false;
        if(callback) callback(_tasks);
      })
      .catch(response => {
        _loading = false;
        this.eventAggregator.publish(new GlobalError(response.content.errors));
      });

    return _tasks;
  }

  register(groupId, task) {
    this.http
      .post('/api/task-manage/groups/' + groupId + '/tasks/', task)
      .then(response => {
        this.eventAggregator.publish(new TaskRegistered());
      })
      .catch(response => {
        this.eventAggregator.publish(new GlobalError(response.content.errors));
      });
  }

  modify(groupId, task) {
    this.http
      .put('/api/task-manage/groups/' + groupId + '/tasks/' + task.taskId, task)
      .then(response => {
        this.eventAggregator.publish(new TaskModified());
      })
      .catch(response => {
        this.eventAggregator.publish(new GlobalError(response.content.errors));
      });
  }

  remove(groupId, taskId) {
    this.http
      .delete('/api/task-manage/groups/' + groupId + '/tasks/' + taskId)
      .then(response => {
        this.eventAggregator.publish(new TaskRemoved());
      })
      .catch(response => {
        this.eventAggregator.publish(new GlobalError(response.content.errors));
      });
  }

  changeStatus(groupId, taskId, status){
    var task = _taskOf(taskId);
    if(!task || task.status === status) return;

    task.status = status;
    this.modify(groupId, task);
  }
}

export class TaskRegistered {}
export class TaskModified {}
export class TaskRemoved {}
