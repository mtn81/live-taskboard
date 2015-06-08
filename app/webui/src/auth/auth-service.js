import {inject} from 'aurelia-framework';
import {HttpClient} from 'aurelia-http-client';
import {EventAggregator} from 'aurelia-event-aggregator';
import {GlobalError} from '../global-error';

@inject(HttpClient, EventAggregator)
export class AuthService {
  constructor(http, eventAggregator) {
    this.http = http;
    this.eventAggregator = eventAggregator;
  }

  authenticate(loginId, password){
    var auth = {};
    this.http
      .post("/api/auth-access/auth/",{
        id: loginId,
        password: password
      })
      .then(response => {
        jQuery.extend(auth, response.content.data);
        this.eventAggregator.publish(new AuthSuccessed());
      })
      .catch(response => {
        this.eventAggregator.publish(new GlobalError(response.content.errors));
      });
    return auth;
  }
}

export class AuthSuccessed {}
