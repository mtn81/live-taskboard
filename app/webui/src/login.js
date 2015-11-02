import {inject} from 'aurelia-framework';
import {Router} from 'aurelia-router';
import {EventAggregator} from 'aurelia-event-aggregator';
import {AuthService, AuthSuccessed} from 'auth/auth-service';
import {GlobalError} from 'global-error';

@inject(Router, EventAggregator, AuthService)
export class Login {
  constructor(router, eventAggregator, authService) {
    this.router = router;
    this.authService = authService;
    this.eventAggregator = eventAggregator;
  }

  login() {
    var auth = this.authService.authenticate(this.loginId, this.password);
    this.eventAggregator.subscribe(AuthSuccessed, message => {
      this.eventAggregator.publish('init.menu');
      this.router.navigate('taskboard');
    });
  }

  showUserRegister() {
    this.eventAggregator.subscribe('user-register.enable', payload => {
      $(this.userRegisterModal).find('.btn-primary').removeAttr('disabled');
    });
    this.eventAggregator.subscribe('user-register.disable', payload => {
      $(this.userRegisterModal).find('.btn-primary').attr('disabled', true);
    });
    $(this.userRegisterModal).on('shown.bs.modal', () => {
      this.eventAggregator.publish('init.user.register');
    });
    $(this.userRegisterModal).modal('show');
  }

  fire(eventId, hideTarget){
    this.eventAggregator.subscribe(eventId + '.success', payload => {
      $(hideTarget).modal('hide');
    });
    this.eventAggregator.publish(eventId);
  }
}
