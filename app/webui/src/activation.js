import {inject} from 'aurelia-framework';
import {Router} from 'aurelia-router';
import {EventAggregator} from 'aurelia-event-aggregator';
import {EventAggregatorWrapper} from './lib/event-aggregator-wrapper';
import {UserService, UserActivated} from './user/user-service';

@inject(Router, EventAggregator, UserService)
export class Activation {
  constructor(router, eventAggregator, userService) {
    this.router = router;
    this.events = new EventAggregatorWrapper(this, eventAggregator);
    this.userService = userService;
  }

  activateUser() {
    this.events.subscribe(UserActivated, e => {
      this.router.navigate('login');
    });
    this.userService.activate(this.activationId);
  }

  //aurelia life cycle callback
  activate(params) {
    this.activationId = params.id;
  }
}
