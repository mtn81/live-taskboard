import {inject} from 'aurelia-framework';
import {Router} from 'aurelia-router';
import {EventAggregator} from 'aurelia-event-aggregator';
import {EventAggregatorWrapper} from './lib/event-aggregator-wrapper';
import {AuthService, AuthSuccessed, SocialAuthStarted} from 'auth/auth-service';
import {GlobalError} from 'global-error';

@inject(Router, EventAggregator, AuthService)
export class Login {
  constructor(router, eventAggregator, authService) {
    this.router = router;
    this.authService = authService;
    this.events = new EventAggregatorWrapper(this, eventAggregator);
  }

  login() {
    this.events.subscribe(AuthSuccessed, message => {
      this.events.publish('init.menu');
      this.router.navigate('taskboard');
    });
    this.authService.authenticate(this.loginId, this.password);
  }

  loginAsGoogle() {
    this.events.subscribe(SocialAuthStarted, payload => {
      let authProcess = payload.data;
      document.cookie = `auth_pid=${authProcess.processId};path=/;max-age=600`;
      window.location = authProcess.authLocation;
    });

    this.authService.startSocialLogin(
        `${window.location.origin}/#/social_login`,
        `${window.location.origin}`);
  }

  showUserRegister() {
    this.events.subscribe('user-register.enable', payload => {
      $(this.userRegisterModal).find('.btn-primary').removeAttr('disabled');
    });
    this.events.subscribe('user-register.disable', payload => {
      $(this.userRegisterModal).find('.btn-primary').attr('disabled', true);
    });
    $(this.userRegisterModal).on('shown.bs.modal', () => {
      this.events.publish('init.user.register');
    });
    $(this.userRegisterModal).modal('show');
  }


  fire(eventId, hideTarget){
    this.events.subscribe(eventId + '.success', payload => {
      $(hideTarget).modal('hide');
    });
    this.events.publish(eventId);
  }
  
}
