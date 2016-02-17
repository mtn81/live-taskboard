import {inject} from 'aurelia-framework';
import {EventAggregator} from 'aurelia-event-aggregator';
import {EventAggregatorWrapper} from './lib/event-aggregator-wrapper';
import {GlobalError} from 'global-error';
import {UserService} from './user/user-service';

@inject(EventAggregator, UserService)
export class SocialUser {
  specifyName = false;
  userName = '';
  originalUserName = '';
  notifyEmail = true;
  specifyEmail = false;
  email = '';
  originalEmail = '';

  constructor(eventAggregator, userService) {
    this.userService = userService;
    this.events = new EventAggregatorWrapper(this, eventAggregator);
  }

  activate() {
    this.userService.loadSocialUser(user => {
      this.specifyName = !!user.name;
      this.userName = this.specifyName ? user.name : user.originalName;
      this.originalUserName = user.originalName;
      this.specifyEmail = !!user.email;
      this.email = this.specifyEmail ? user.email : user.originalEmail;
      this.originalEmail = user.originalEmail;
      this.notifyEmail = user.notifyEmail;
    });
  }

}
