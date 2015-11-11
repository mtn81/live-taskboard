import {inject} from 'aurelia-framework';
import {HttpClient} from 'aurelia-http-client';
import {EventAggregator} from 'aurelia-event-aggregator';
import {GlobalError} from '../global-error';
import {AuthContext} from './auth-context';

@inject(HttpClient, EventAggregator, AuthContext)
export class AuthService {
  constructor(http, eventAggregator, authContext) {
    this.http = http;
    this.eventAggregator = eventAggregator;
    this.authContext = authContext
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
        this.authContext.store(auth);
        this.eventAggregator.publish(new AuthSuccessed());
      })
      .catch(response => {
        this.eventAggregator.publish(new GlobalError(response.content.errors));
      });
    return auth;
  }

  logout(){
    if(!this.authContext.isAuthenticated()) {
      return;
    }

    this.http
      .delete(`/api/auth-access/auth/${this.authContext.getAuth().id}`)
      .then(response => {
        this.eventAggregator.publish(new LogoutSuccessed());
      })
      .catch(response => {
        this.eventAggregator.publish(new GlobalError(response.content.errors));
      });

    this.authContext.remove();
  }
}

export class AuthSuccessed {}
export class LogoutSuccessed {}
