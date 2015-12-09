import _ from 'underscore';
import {inject} from 'aurelia-framework';
import {HttpClient} from 'aurelia-http-client';
import {EventAggregator} from 'aurelia-event-aggregator';
import {GlobalError} from '../global-error';
import {AuthContext} from 'auth/auth-context';
import {Stomp} from 'stomp-websocket';
import {HttpClientWrapper, CachedHttpLoader} from '../lib/http-client-wrapper';

@inject(HttpClient, EventAggregator, AuthContext)
export class GroupService {
  _watchingAvailableGroup = false;

  constructor(http, eventAggregator, authContext) {
    this.http = new HttpClientWrapper(http, eventAggregator).withAuth(authContext);
    this.httpLoader = new CachedHttpLoader(http, eventAggregator).withAuth(authContext);
    this.eventAggregator = eventAggregator;
    this.authContext = authContext;
  }

  register(group){

    this.http.call(http => {
      return http
        .post(`/api/task-manage/members/${this.memberId()}/groups/`, group)
        .then(response => {
          this.groups();
          this.eventAggregator.publish(new GroupRegistered(response.content.data));
        })
    }, true);
  }

  watchGroupAvailable(callback){
    if(!this.authContext.isAuthenticated()) return;
    if(this._watchingAvailableGroup) return;

    let websocket = new WebSocket('ws://localhost:28080/task-manage/websocket/notify');
    let stompClient = window.Stomp.over(websocket);
    stompClient.connect({}, frame => {
      stompClient.subscribe('/topic/' + this.memberId() + '/group_available', response => {
        if (callback) callback(JSON.parse(response.body));
        this.groups();
      });
    });
    this._watchingAvailableGroup = true;
  }

  groups() {
    return this.httpLoader.list(
        `/api/task-manage/members/${this.memberId()}/groups/`,
        response => {
          return response.content.data.groups;
        });
  }

  group(groupId) {
    return this.httpLoader.object(
        `/api/task-manage/members/${this.memberId()}/groups/${groupId}`,
        response => {
          this.eventAggregator.publish(new GroupLoaded());
          return response.content.data;
        });
  }

  remove(group){
    this.http.call(http => {
      group.removing = true;
      return http
        .delete(`/api/task-manage/members/${this.memberId()}/groups/${group.groupId}`)
        .then(response => {
          this.groups();
        });
    }, true);
  }

  memberId() {
    return this.authContext.getAuth().userId;
  }
}

export class GroupLoaded {}
export class GroupRegistered {}
export class GroupJoinApplied {}
export class GroupJoinCancelled {}
