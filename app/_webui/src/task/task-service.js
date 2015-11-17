import {inject} from 'aurelia-framework';
import {HttpClient} from 'aurelia-http-client';
import {EventAggregator} from 'aurelia-event-aggregator';
import {GlobalError} from '../global-error';
import {HttpClientWrapper} from '../lib/http-client-wrapper';
import {AuthContext} from '../auth/auth-context';

var _tasks = {};

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

@inject(HttpClient, EventAggregator, AuthContext)
export class TaskService {

  constructor(http, eventAggregator, authContext) {
    this.http = new HttpClientWrapper(http, eventAggregator).withAuth(authContext);
    this.eventAggregator = eventAggregator;
  }

  load(groupId, status) {

    _tasks[status] = _tasks[status] || [];

    this.http.call(http => {
      return http
        .get('/api/task-manage/groups/' + groupId + '/tasks/')
        .then(response => {
          let foundTasks = response.content.data;
          $.each(_tasks, (aStatus, tasksInStatus) => {
            tasksInStatus.length = 0;
            $.merge(tasksInStatus, foundTasks[aStatus]);
          });
        });
    });

    return _tasks[status];
  }

  register(groupId, task) {
    this.http.call(http => {
      return http
        .post(`/api/task-manage/groups/${groupId}/tasks/`, task)
        .then(response => {
          this.eventAggregator.publish(new TaskRegistered());
        });
    }, true);
  }

  modify(groupId, task) {
    this.http.call(http => {
      return http
        .put(`/api/task-manage/groups/${groupId}/tasks/${task.taskId}`, task)
        .then(response => {
          this.eventAggregator.publish(new TaskModified());
        });
    }, true);
  }

  remove(groupId, taskId) {
    this.http.call(http => {
      return http
        .delete(`/api/task-manage/groups/${groupId}/tasks/${taskId}`)
        .then(response => {
          this.eventAggregator.publish(new TaskRemoved());
        });
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