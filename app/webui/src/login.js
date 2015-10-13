import {inject} from 'aurelia-framework';
import {Router} from 'aurelia-router';
import {EventAggregator} from 'aurelia-event-aggregator';
import {AuthService, AuthSuccessed} from 'auth/auth-service';
import {AuthContext} from 'auth/auth-context';
import {GlobalError} from 'global-error';

@inject(Router, EventAggregator, AuthService, AuthContext)
export class Login {
  constructor(router, eventAggregator, authService, authContext) {
    this.router = router;
    this.authService = authService;
    this.authContext = authContext;
    this.eventAggregator = eventAggregator;
  }

  login() {
    var auth = this.authService.authenticate(this.loginId, this.password);
    this.eventAggregator.subscribe(AuthSuccessed, message => {
      this.authContext.store(auth);
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
    this.eventAggregator.publish('init.user.register');
    $(this.userRegisterModal).modal('show');
  }

  fire(eventId, hideTarget){
    this.eventAggregator.subscribe(eventId + '.success', payload => {
      $(hideTarget).modal('hide');
    });
    this.eventAggregator.publish(eventId);
  }
}
