import {inject} from 'aurelia-framework';
import {Router} from 'aurelia-router';
import {EventAggregator} from 'aurelia-event-aggregator';
import {UserService, UserActivated} from './user/user-service';

@inject(Router, EventAggregator, UserService)
export class Activation {
  constructor(router, eventAggregator, userService) {
    this.router = router;
    this.eventAggregator = eventAggregator;
    this.userService = userService;
  }

  activateUser() {
    this.eventAggregator.subscribe(UserActivated, e => {
      this.router.navigate('login');
    });
    this.userService.activate(this.activateId);
  }

  //aurelia life cycle callback
  activate(params) {
    this.activateId = params.id;
  }
}