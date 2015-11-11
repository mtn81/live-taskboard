import {inject} from 'aurelia-framework';
import {HttpClient} from 'aurelia-http-client';
import {EventAggregator} from 'aurelia-event-aggregator';
import {GlobalError} from '../global-error';
import {AuthContext} from 'auth/auth-context';
import {Stomp} from 'stomp-websocket';
import {HttpClientWrapper} from '../lib/http-client-wrapper';


@inject(HttpClient, EventAggregator, AuthContext)
export class MemberAcceptService {

  constructor(http, eventAggregator, authContext) {
    this.http = new HttpClientWrapper(http, eventAggregator).withAuth(authContext);
    this.eventAggregator = eventAggregator;
    this.authContext = authContext;
  }

  searchAcceptableMembers() {
    return [
      { memberId:'m01', groupName:'group01', applied: new Date().getTime() },
      { memberId:'m02', groupName:'group02', applied: new Date().getTime() },
    ];
  }

  _memberId() {
    return this.authContext.getAuth().userId;
  }
}

