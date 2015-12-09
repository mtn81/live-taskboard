import {inject} from 'aurelia-framework';
import {HttpClient} from 'aurelia-http-client';
import {EventAggregator} from 'aurelia-event-aggregator';
import {GlobalError} from '../global-error';
import {HttpClientWrapper, CachedHttpLoader} from '../lib/http-client-wrapper';
import {AuthContext} from '../auth/auth-context';



@inject(HttpClient, EventAggregator, AuthContext)
export class TaskService {
  _tasks = {};

  constructor(http, eventAggregator, authContext) {
    this.http = new HttpClientWrapper(http, eventAggregator).withAuth(authContext);
    this.httpLoader = new CachedHttpLoader(http, eventAggregator).withAuth(authContext);
    this.eventAggregator = eventAggregator;
  }

  load(groupId) {
    this._tasks = this.httpLoader.object(
      `/api/task-manage/groups/${groupId}/tasks/`,
      response => {
        return response.content.data;
      });
    return this._tasks;
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
    var task = this._taskOf(taskId);
    if(!task || task.status === status) return;

    task.status = status;
    this.modify(groupId, task);
  }

  _allTasks(){
    var allTasks = [];
    $.each(this._tasks, (status, tasksInStatus) => {
      $.merge(allTasks, tasksInStatus);
    });
    return allTasks;
  };

  _taskOf(taskId){
    var result;
    this._allTasks().forEach(t => {
      if(t.taskId === taskId) result = t;
    })
    return result;
  };

}

export class TaskRegistered {}
export class TaskModified {}
export class TaskRemoved {}
