import {inject} from 'aurelia-framework';
import {HttpClient} from 'aurelia-http-client';
import {EventAggregator} from 'aurelia-event-aggregator';
import {GlobalError} from '../global-error';
import {AuthContext} from 'auth/auth-context';
import {Stomp} from 'stomp-websocket';
import {HttpClientWrapper, CachedHttpLoader} from '../lib/http-client-wrapper';


@inject(HttpClient, EventAggregator, AuthContext)
export class MemberAcceptService {

  constructor(http, eventAggregator, authContext) {
    this.http = new HttpClientWrapper(http, eventAggregator).withAuth(authContext);
    this.httpLoader = new CachedHttpLoader(http, eventAggregator).withAuth(authContext);
    this.eventAggregator = eventAggregator;
    this.authContext = authContext;
  }

  searchAcceptableMembers() {
    return this.httpLoader.list(
      `/api/task-manage/members/${this._memberId()}/acceptable_group_joins/search`,
      response => {
        return response.content.data.members;
      });
  }

  searchRejectedMembers() {
    return this.httpLoader.list(
      `/api/task-manage/members/${this._memberId()}/reject_group_joins/search`,
      response => {
        return response.content.data.members;
      });
  }

  acceptMember(member) {
    this.http.call(http => {
      return http
        .put(`/api/task-manage/groups/${member.groupId}/group_joins/${member.joinId}/accept`)
        .then(response => {
          this.eventAggregator.publish(new MemberAccepted());
        })
    }, true);
  }
  rejectMember(member) {
    this.http.call(http => {
      return http
        .put(`/api/task-manage/groups/${member.groupId}/group_joins/${member.joinId}/reject`)
        .then(response => {
          this.eventAggregator.publish(new MemberRejected());
        })
    }, true);
  }

  _memberId() {
    return this.authContext.getUserId();
  }
}

export class MemberRejected {}
export class MemberAccepted {}
