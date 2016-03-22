import {Stomp} from 'stomp-websocket';
import {inject} from 'aurelia-framework';
import {HttpClient} from 'aurelia-http-client';
import {EventAggregator} from 'aurelia-event-aggregator';
import {GlobalError} from '../../lib/global-error';
import {HttpClientWrapper, CachedHttpLoader} from '../../lib/http-client-wrapper';
import {AuthContext} from '../../services/auth/auth-context';


@inject(HttpClient, EventAggregator, AuthContext)
export class GroupJoinService {

  constructor(http, eventAggregator, authContext) {
    this.http = new HttpClientWrapper(http, eventAggregator).withAuth(authContext);
    this.httpLoader = new CachedHttpLoader(http, eventAggregator).withAuth(authContext);
    this.eventAggregator = eventAggregator;
    this.authContext = authContext;
  }

  searchNotAppliedByName(groupName) {
    return this.httpLoader.list(
      `/api/task-manage/members/${this.memberId()}/not_join_applied_groups/search?groupName=${groupName}`,
      response => {
        return response.content.data.groups;
      });
  }

  searchApplied() {
    return this.httpLoader.list(
      `/api/task-manage/members/${this.memberId()}/group_joins/`,
      response => {
        return response.content.data.groups;
      });
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
        .delete(`/api/task-manage/members/${this.memberId()}/group_joins/${group.joinApplicationId}`)
        .then(response => {
          this.eventAggregator.publish(new GroupJoinCancelled());
        })
    }, true);
  }

  memberId() {
    return this.authContext.getUserId();
  }
}

export class GroupJoinApplied {}
export class GroupJoinCancelled {}
