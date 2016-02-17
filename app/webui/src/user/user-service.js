import {inject} from 'aurelia-framework';
import {HttpClient} from 'aurelia-http-client';
import {EventAggregator} from 'aurelia-event-aggregator';
import {GlobalError} from '../global-error';
import {AuthContext} from '../auth/auth-context';
import {HttpClientWrapper, CachedHttpLoader} from '../lib/http-client-wrapper';
import {aaApi} from '../lib/env-resolver';

@inject(HttpClient, EventAggregator, AuthContext)
export class UserService {

  constructor(http, eventAggregator, authContext) {
    this.http = http;
    this.httpLoader = new CachedHttpLoader(http, eventAggregator).withAuth(authContext);
    this.eventAggregator = eventAggregator;
    this.authContext = authContext;
  }

  register(user) {

    this.http
      .post(aaApi('/users/'), user)
      .then(response => {
        this.eventAggregator.publish(new UserRegistered());
      })
      .catch(response => {
        this.eventAggregator.publish('user.register.failed', new GlobalError(response.content.errors));
      });
  }

  validate(user) {

    this.http
      .post(aaApi('/users/validate'), user)
      .then(response => {
        this.eventAggregator.publish(new UserRegisterValidationSuccess());
      })
      .catch(response => {
        this.eventAggregator.publish(new UserRegisterValidationError(new GlobalError(response.content.errors)));
      });
  }

  activate(activationId) {
    this.http
      .post(aaApi('/activate_user/'), { activationId: activationId })
      .then(response => {
        this.eventAggregator.publish(new UserActivated());
      })
      .catch(response => {
        this.eventAggregator.publish(new GlobalError(response.content.errors));
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
  
  saveSocialUser(user) {
    console.log(user);
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
