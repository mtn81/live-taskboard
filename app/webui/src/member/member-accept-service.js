import {inject} from 'aurelia-framework';
import {HttpClient} from 'aurelia-http-client';
import {EventAggregator} from 'aurelia-event-aggregator';
import {GlobalError} from '../global-error';
import {AuthContext} from 'auth/auth-context';
import {Stomp} from 'stomp-websocket';
import {HttpClientWrapper} from '../lib/http-client-wrapper';


@inject(HttpClient, EventAggregator, AuthContext)
export class MemberAcceptService {

  _acceptableMemberJoins = [];
  _rejectedMemberJoins = [];

  constructor(http, eventAggregator, authContext) {
    this.http = new HttpClientWrapper(http, eventAggregator).withAuth(authContext);
    this.eventAggregator = eventAggregator;
    this.authContext = authContext;
  }

  searchAcceptableMembers() {
    this.http.call(http => {
      return http
        .get(`/api/task-manage/members/${this._memberId()}/acceptable_group_joins/search`)
        .then(response => {
          let found = response.content.data.members;
          this._acceptableMemberJoins.length = 0;
          $.merge(this._acceptableMemberJoins, found);
        });
    }, false, 'searchAcceptableMembers');

    return this._acceptableMemberJoins;
  }

  searchRejectedMembers() {
    this.http.call(http => {
      return http
        .get(`/api/task-manage/members/${this._memberId()}/reject_group_joins/search`)
        .then(response => {
          let found = response.content.data.members;
          this._rejectedMemberJoins.length = 0;
          $.merge(this._rejectedMemberJoins, found);
        });
    }, false, 'searchRejectedMembers');

    return this._rejectedMemberJoins;
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
    return this.authContext.getAuth().userId;
  }
}

export class MemberRejected {}
export class MemberAccepted {}
