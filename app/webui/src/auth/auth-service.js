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
    this.http
      .post("/api/auth-access/auth/",{
        id: loginId,
        password: password
      })
      .then(response => {
        this.authContext.store(response.content.data);
        this.eventAggregator.publish(new AuthSuccessed());
      })
      .catch(response => {
        this.eventAggregator.publish(new GlobalError(response.content.errors));
      });
  }

  startSocialLogin(acceptUrl, rejectUrl) {
    this.http
      .post('/api/auth-access/social_auth?start', {
        acceptClientUrl: acceptUrl,
        rejectClientUrl: rejectUrl
      })
      .then(response => {
        this.eventAggregator.publish(new SocialAuthStarted(response.content.data));
      })
      .catch(response => {
        this.eventAggregator.publish(new GlobalError(response.content.errors));
      });
  }

  confirmSocialLogin() {
    this.http
      .get('/api/auth-access/social_auth?confirm')
      .then(response => {
        this.authContext.store(response.content.data);
        this.eventAggregator.publish(new AuthSuccessed());
      })
      .catch(response => {
        this.eventAggregator.publish(new GlobalError(response.content.errors));
      });
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
export class SocialAuthStarted {
  constructor(data) {
    this.data = data;
  }
}
