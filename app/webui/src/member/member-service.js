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
}
