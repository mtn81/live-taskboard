import {inject} from 'aurelia-framework';
import {HttpClient} from 'aurelia-http-client';
import {EventAggregator} from 'aurelia-event-aggregator';
import {GlobalError} from '../../lib/global-error';
import {HttpClientWrapper, CachedHttpLoader} from '../../lib/http-client-wrapper';
import {tmApi} from '../../lib/env-resolver';
import {AuthContext} from '../../services/auth/auth-context';

@inject(HttpClient, EventAggregator, AuthContext)
export class MemberService {
  _members = [];

  constructor(http, eventAggregator, authContext) {
    this.http = new HttpClientWrapper(http, eventAggregator).withAuth(authContext);
    this.httpLoader = new CachedHttpLoader(http, eventAggregator).withAuth(authContext);
    this.eventAggregator = eventAggregator;
  }

  loadByGroup(groupId, callback) {
    return this.httpLoader.list(
      tmApi(`/groups/${groupId}/members/`),
      response => {
        if (callback) callback();
        return response.content.data.members;
      });
  }

  changeToAdmin(groupId, member) {
    if (member.admin) return;

    this.http.call(http => {
      return http
        .put(tmApi(`/groups/${groupId}/members/${member.memberId}`), { admin: true })
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
        .put(tmApi(`/groups/${groupId}/members/${member.memberId}`), { admin: false })
        .then(response => {
          member.admin = false;
          this.eventAggregator.publish(new MemberChanged());
        });
    }, true);
  }
  remove(groupId, member) {
    member.removed = true;

    this.http.call(http => {
      return http
        .delete(tmApi(`/groups/${groupId}/members/${member.memberId}`))
        .then(response => {
          this.eventAggregator.publish(new MemberRemoved());
        });
    }, true);
  }
}

export class MemberChanged {}
export class MemberRemoved {}
export class MemberRoleChanged {}
