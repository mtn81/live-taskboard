import {inject} from 'aurelia-framework';
import {Router} from 'aurelia-router';
import {EventAggregator} from 'aurelia-event-aggregator';
import {EventAggregatorWrapper} from './lib/event-aggregator-wrapper';
import {AuthService, AuthSuccessed, SocialAuthStarted} from 'auth/auth-service';
import {GlobalError} from 'global-error';

@inject(Router, EventAggregator, AuthService)
export class SocialLogin {

  constructor(router, eventAggregator, authService) {
    this.router = router;
    this.authService = authService;
    this.events = new EventAggregatorWrapper(this, eventAggregator);
  }

  confirmAuth() {
    this.events.subscribe(AuthSuccessed, message => {
      this.events.publish('init.menu');
      this.router.navigate('taskboard');
    });
    this.authService.confirmSocialLogin();
  }

  canActivate(params, routeConfig, navigationInstruction) {
    if (params['first_use'] === 'true') {
      return true;
    }
    this.confirmAuth();
    return false;
  }

}
