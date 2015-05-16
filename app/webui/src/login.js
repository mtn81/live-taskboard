import {inject} from 'aurelia-framework';
import {Router} from 'aurelia-router';
import {EventAggregator} from 'aurelia-event-aggregator';
import {AuthService, AuthSuccessed} from './auth/auth-service';
import {AuthContext} from './auth/auth-context';

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
      this.router.navigate('taskboard');
    });
  }
}
