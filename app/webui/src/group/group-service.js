import {inject} from 'aurelia-framework';
import {HttpClient} from 'aurelia-http-client';
import {EventAggregator} from 'aurelia-event-aggregator';
import {GlobalError} from '../global-error';
import {AuthContext} from 'auth/auth-context';
import {Stomp} from 'stomp-websocket';
import {HttpClientWrapper} from '../lib/http-client-wrapper';


var _memberGroups = [];
var _notAppliedGroups = [];
var _appliedGroups = [];
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

  searchNotAppliedByName(groupName) {
    this.http.call(http => {
      return http
        .get(`/api/task-manage/groups/search?not_join_applied&applicantId=${this.memberId()}&groupName=${groupName}`)
        .then(response => {
          let foundGroups = response.content.data.groups;
          _notAppliedGroups.length = 0;
          $.merge(_notAppliedGroups, foundGroups);
        });
    }, false, 'searchNotAppliedByName');

    return _notAppliedGroups;
  }

  searchApplied() {
    this.http.call(http => {
      return http
        .get(`/api/task-manage/groups/search?join_applied&applicantId=${this.memberId()}`)
        .then(response => {
          let foundGroups = response.content.data.groups;
          _appliedGroups.length = 0;
          $.merge(_appliedGroups, foundGroups);
        });
    }, false, 'searchApplied');

    return _appliedGroups;
  }

  applyJoin(group) {
    group.applied = true;

    this.http.call(http => {
      return http
        .post(`/api/task-manage/groups/${group.groupId}/apply`, { applicantMemberId: this.memberId() })
        .then(response => {
          this.eventAggregator.publish(new GroupJoinApplied());
        })
    }, true);
  }

  memberId() {
    return this.authContext.getAuth().userId;
  }
}

export class GroupRegistered {}
export class GroupJoinApplied {}
