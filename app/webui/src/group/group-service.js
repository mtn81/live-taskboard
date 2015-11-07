import {inject} from 'aurelia-framework';
import {HttpClient} from 'aurelia-http-client';
import {EventAggregator} from 'aurelia-event-aggregator';
import {GlobalError} from '../global-error';
import {AuthContext} from 'auth/auth-context';
import {Stomp} from 'stomp-websocket';
import {HttpClientWrapper} from '../lib/http-client-wrapper';


var _memberGroups = [];
var _searchedGroups = [];
var _watchingAvailableGroup = false;

@inject(HttpClient, EventAggregator, AuthContext)
export class GroupService {

  constructor(http, eventAggregator, authContext) {
    this.http = new HttpClientWrapper(http, eventAggregator).withAuth(authContext);
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
    if(_watchingAvailableGroup) return;

    let websocket = new WebSocket('ws://localhost:28080/task-manage/websocket/notify');
    let stompClient = window.Stomp.over(websocket);
    stompClient.connect({}, frame => {
      stompClient.subscribe('/topic/' + this.memberId() + '/group_available', response => {
        if (callback) callback(JSON.parse(response.body));
        this.groups();
      });
    });
    _watchingAvailableGroup = true;
  }

  groups() {
    this.http.call(http => {
      return http
        .get(`/api/task-manage/members/${this.memberId()}/groups/?type=belonging`)
        .then(response => {
          let foundGroups = response.content.data.groups;
          _memberGroups.length = 0;
          $.merge(_memberGroups, foundGroups);
        });
    });

    return _memberGroups;
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

  searchByName(groupName) {
    this.http.call(http => {
      return http
        .get(`/api/task-manage/groups/search?groupName=${groupName}`)
        .then(response => {
          let foundGroups = response.content.data.groups;
          _searchedGroups.length = 0;
          $.merge(_searchedGroups, foundGroups);
        });
    });

    return _searchedGroups;
  }

  applyJoin(group) {
    group.applied = true;
  }

  memberId() {
    return this.authContext.getAuth().userId;
  }
}

export class GroupRegistered {}
