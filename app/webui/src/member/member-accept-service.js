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

  constructor(http, eventAggregator, authContext) {
    this.http = new HttpClientWrapper(http, eventAggregator).withAuth(authContext);
    this.eventAggregator = eventAggregator;
    this.authContext = authContext;
  }

  searchAcceptableMembers() {
    this.http.call(http => {
      return http
        .get(`/api/task-manage/group_joins/search?acceptable&memberId=${this._memberId()}`)
        .then(response => {
          let found = response.content.data.members;
          this._acceptableMemberJoins.length = 0;
          $.merge(this._acceptableMemberJoins, found);
        });
    });

    return this._acceptableMemberJoins;
  }

  _memberId() {
    return this.authContext.getAuth().userId;
  }
}

