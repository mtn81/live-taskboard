import {inject} from 'aurelia-framework';
import {HttpClient} from 'aurelia-http-client';
import {EventAggregator} from 'aurelia-event-aggregator';
import {GlobalError} from '../global-error';
import {AuthContext} from 'auth/auth-context';
import {Stomp} from 'stomp-websocket';
import {HttpClientWrapper} from '../lib/http-client-wrapper';


var _notAppliedGroups = [];
var _appliedGroups = [];

@inject(HttpClient, EventAggregator, AuthContext)
export class GroupJoinService {

  constructor(http, eventAggregator, authContext) {
    this.http = new HttpClientWrapper(http, eventAggregator).withAuth(authContext);
    this.eventAggregator = eventAggregator;
    this.authContext = authContext;
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
        .get(`/api/task-manage/members/${this.memberId()}/group_joins/`)
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
        .post(`/api/task-manage/members/${this.memberId()}/group_joins/`, { groupId: group.groupId })
        .then(response => {
          this.eventAggregator.publish(new GroupJoinApplied());
        })
    }, true);
  }
  cancelJoin(group) {
    group.canceled = true;

    this.http.call(http => {
      return http
        .delete(`/api/task-manage/members/${this.memberId()}/group_joins/`, { joinApplicationId: group.joinApplicationId })
        .then(response => {
          this.eventAggregator.publish(new GroupJoinCancelled());
        })
    }, true);
  }

  memberId() {
    return this.authContext.getAuth().userId;
  }
}

export class GroupJoinApplied {}
export class GroupJoinCancelled {}
