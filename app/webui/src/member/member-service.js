import {inject} from 'aurelia-framework';
import {HttpClient} from 'aurelia-http-client';
import {EventAggregator} from 'aurelia-event-aggregator';
import {GlobalError} from '../global-error';
import {HttpClientWrapper} from '../lib/http-client-wrapper';
import {AuthContext} from '../auth/auth-context';

@inject(HttpClient, EventAggregator, AuthContext)
export class MemberService {
  _members = [];

  constructor(http, eventAggregator, authContext) {
    this.http = new HttpClientWrapper(http, eventAggregator).withAuth(authContext);
    this.eventAggregator = eventAggregator;
  }

  loadByGroup(groupId, callback) {
    
    this.http.call(http => {
      return http
        .get(`/api/task-manage/groups/${groupId}/members/`)
        .then(response => {
          let foundMembers = response.content.data.members;
          this._members.length = 0;
          $.merge(this._members, foundMembers);
          if(callback) callback(this._members);
        });
    });

    return this._members;
  }

  changeToAdmin(groupId, member) {
    if (member.admin) return;

    this.http.call(http => {
      return http
        .put(`/api/task-manage/members/${member.memberId}?change_admin`, { groupId: groupId })
        .then(response => {
          member.admin = true;
          this.eventAggregator.publish(new MemberRoleChanged());
        });
    }, true);
  }
  changeToNormal(groupId, member) {
    if (!member.admin) return;

    this.http.call(http => {
      return http
        .put(`/api/task-manage/members/${member.memberId}?change_normal`, { groupId: groupId })
        .then(response => {
          member.admin = false;
          this.eventAggregator.publish(new MemberRoleChanged());
        });
    }, true);
  }
}

export class MemberRoleChanged {}
