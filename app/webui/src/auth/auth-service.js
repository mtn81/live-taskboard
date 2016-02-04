import {inject} from 'aurelia-framework';
import {HttpClient} from 'aurelia-http-client';
import {EventAggregator} from 'aurelia-event-aggregator';
import {GlobalError} from '../global-error';
import {AuthContext} from './auth-context';
import {HttpClientWrapper, CachedHttpLoader} from '../lib/http-client-wrapper';

@inject(HttpClient, EventAggregator, AuthContext)
export class AuthService {
  constructor(http, eventAggregator, authContext) {
    this.http = new HttpClientWrapper(http, eventAggregator);
    this.httpLoader = new CachedHttpLoader(http, eventAggregator).withAuth(authContext);
    this.eventAggregator = eventAggregator;
    this.authContext = authContext
  }

  authenticate(loginId, password){
    this.http.call(http => {
      return http
        .post("/api/auth-access/auth/",{
          id: loginId,
          password: password
        })
        .then(response => {
          this.authContext.store(response.content.data);
          this.eventAggregator.publish(new AuthSuccessed());
        });
    }, true);
  }

  startSocialLogin(socialSite, acceptUrl, rejectUrl) {
    this.http.call(http => {
      return http
        .post(`/api/auth-access/social_auth/${socialSite}?start`, {
          acceptClientUrl: acceptUrl,
          rejectClientUrl: rejectUrl
        })
        .then(response => {
          this.eventAggregator.publish(new SocialAuthStarted(response.content.data));
        });
    }, true);
  }

  confirmSocialLogin() {
    this.http.call(http => {
      return http
        .get('/api/auth-access/social_auth?confirm')
        .then(response => {
          this.authContext.store(response.content.data);
          this.eventAggregator.publish(new AuthSuccessed());
        });
    }, true);
  }

  loadSocialUser(callback) {
    return this.httpLoader.object(
        `/api/auth-access/social_user`,
        response => {
          const user = response.content.data;
          if(callback) callback(user);
          return user;
        }, false);
  }

  logout(){
    if(!this.authContext.isAuthenticated()) {
      return;
    }

    this.http.call(http => {
      return http
        .delete(`/api/auth-access/auth/${this.authContext.getAuthId()}`)
        .then(response => {
          this.eventAggregator.publish(new LogoutSuccessed());
        });
    }, true);

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
