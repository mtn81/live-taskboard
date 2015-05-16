import {inject} from 'aurelia-framework';
import {HttpClient} from 'aurelia-http-client';
import {EventAggregator} from 'aurelia-event-aggregator';

@inject(HttpClient, EventAggregator)
export class AuthService {
  constructor(http, eventAggregator) {
    this.http = http;
    this.eventAggregator = eventAggregator;
  }

  authenticate(loginId, password){
    var auth = {};
    this.http
      .post("/api/auth-access/auth",{
        id: loginId,
        password: password
      })
      .then(r => {
        auth.id = "hoge";
        auth.userName = "hogehoge";
        this.eventAggregator.publish(new AuthSuccessed());
      });
    return auth;
  }
}

export class AuthSuccessed {}
