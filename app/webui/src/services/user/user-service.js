import {inject} from 'aurelia-framework';
import {HttpClient} from 'aurelia-http-client';
import {EventAggregator} from 'aurelia-event-aggregator';
import {GlobalError} from '../../lib/global-error';
import {HttpClientWrapper, CachedHttpLoader} from '../../lib/http-client-wrapper';
import {aaApi} from '../../lib/env-resolver';
import {AuthContext} from '../../services/auth/auth-context';

@inject(HttpClient, EventAggregator, AuthContext)
export class UserService {

  constructor(http, eventAggregator, authContext) {
    this.http = new HttpClientWrapper(http, eventAggregator).withAuth(authContext);
    this.httpLoader = new CachedHttpLoader(http, eventAggregator).withAuth(authContext);
    this.eventAggregator = eventAggregator;
    this.authContext = authContext;
  }

  register(user) {

    this.http.call(http => {
      return this.http
        .post(aaApi('/users/'), user)
        .then(response => {
          this.eventAggregator.publish(new UserRegistered());
        });
    }, true);
  }

  validate(user) {
    this.http.call(http => {
      return this.http
        .post(aaApi('/users/validate'), user)
        .then(response => {
          this.eventAggregator.publish(new UserRegisterValidationSuccess());
        });
    }, true);
  }

  activate(activationId) {
    this.http.call(http => {
      return this.http
        .post(aaApi('/activate_user/'), { activationId: activationId })
        .then(response => {
          this.eventAggregator.publish(new UserActivated());
        });
    }, true);
  }

  loadUser(callback) {
    return this.httpLoader.object(
        aaApi(`/users/${this.authContext.getUserId()}`),
        response => {
          const user = response.content.data;
          if(callback) callback(user);
          return user;
        });
  }
  loadSocialUser(callback) {
    return this.httpLoader.object(
        aaApi(`/social_users/${this.authContext.getUserId()}`),
        response => {
          const user = response.content.data;
          if(callback) callback(user);
          return user;
        });
  }

  saveUser(user) {
    this.http.call(http => {
      return http
        .put(aaApi(`/users/${this.authContext.getUserId()}`), user)
        .then(response => {
          this.eventAggregator.publish(new UserChanged());
          this.authContext.changeUserName(user.userName);
        });
    }, true);
  }
  saveSocialUser(user) {
    this.http.call(http => {
      return http
        .put(aaApi(`/social_users/${this.authContext.getUserId()}`), user)
        .then(response => {
          this.eventAggregator.publish(new UserChanged());
          this.authContext.changeUserName(user.userName);
        });
    }, true);
  }

}
export class UserRegistered {}
export class UserRegisterValidationError {
  constructor(error){
    this.error = error;
  }
}
export class UserRegisterValidationSuccess {}
export class UserActivated {}
export class UserChanged {}
