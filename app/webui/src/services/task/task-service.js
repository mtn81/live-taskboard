import {inject} from 'aurelia-framework';
import {HttpClient} from 'aurelia-http-client';
import {EventAggregator} from 'aurelia-event-aggregator';
import {GlobalError} from '../../lib/global-error';
import {HttpClientWrapper, CachedHttpLoader, StompClient} from '../../lib/http-client-wrapper';
import {tmApi, tmWebSocket} from '../../lib/env-resolver';
import {AuthContext} from '../../services/auth/auth-context';

@inject(HttpClient, EventAggregator, AuthContext)
export class TaskService {
  _tasks = {};

  constructor(http, eventAggregator, authContext) {
    this.http = new HttpClientWrapper(http, eventAggregator).withAuth(authContext);
    this.httpLoader = new CachedHttpLoader(http, eventAggregator).withAuth(authContext);
    this.stomp = new StompClient(tmWebSocket('/notify'), authContext);
    this.eventAggregator = eventAggregator;
    this.authContext = authContext;
  }

  search(groupId, condition) {
    this._tasks = this.httpLoader.object(
      tmApi(`/groups/${groupId}/tasks/search?keyword=${condition.keyword}&members=${condition.members}`),
      response => {
        this.eventAggregator.publish(new TasksLoaded());
        return response.content.data;
      });
    return this._tasks;
  }

  loadDetail(groupId, taskId, callback) {
    return this.httpLoader.object(
        tmApi(`/groups/${groupId}/tasks/${taskId}`),
        response => {
          let task = response.content.data;
          if(callback) callback(task);
          return task;
        });
  }

  register(groupId, task) {
    this.http.call(http => {
      return http
        .post(tmApi(`/groups/${groupId}/tasks/`), task)
        .then(response => {
          this.eventAggregator.publish(new TaskRegistered());
        });
    }, true);
  }

  modify(groupId, task) {
    this.http.call(http => {
      return http
        .put(tmApi(`/groups/${groupId}/tasks/${task.taskId}`), task)
        .then(response => {
          this.eventAggregator.publish(new TaskModified());
        });
    }, true);
  }

  validateDetail(groupId, task) {
    this.http.call(http => {
      return http
        .put(tmApi(`/groups/${groupId}/tasks/${task.taskId}?detail&validate`), task)
        .then(response => {
          this.eventAggregator.publish(new TaskValidationSuccess());
        })
        .catch(response => {
          this.eventAggregator.publish(new TaskValidationError(new GlobalError(response.content.errors)));
        });
    }, true);
  }

  modifyDetail(groupId, task) {
    this.http.call(http => {
      return http
        .put(tmApi(`/groups/${groupId}/tasks/${task.taskId}?detail`), task)
        .then(response => {
          this.eventAggregator.publish(new TaskModified());
        });
    }, true);
  }

  remove(groupId, taskId) {
    this.http.call(http => {
      return http
        .delete(tmApi(`/groups/${groupId}/tasks/${taskId}`))
        .then(response => {
          this.eventAggregator.publish(new TaskRemoved());
        });
    });
  }

  changeStatus(groupId, taskId, status){
    var task = this._taskOf(taskId);
    if(!task || task.status === status) return;

    this._changeStatus(task, status);
    this.modify(groupId, task);
  }

  watchTaskChange(groupId, callback) {
    this.stomp.subscribe(`/topic/${groupId}/task_changed`, taskChange => {
      callback(taskChange, this.authContext.hasClientId(taskChange.clientId));
    });
  }

  _changeStatus(task, newStatus){
    const oldStatus = task.status;
    task.status = newStatus;

    this._tasks[newStatus].push(task);
    this._tasks[oldStatus].some((t, i) => {
      if(t === task) this._tasks[oldStatus].splice(i, 1);
    });
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
export class TasksLoaded {}
export class TaskValidationSuccess {}
export class TaskValidationError {
  constructor(error){
    this.error = error;
  }
}
