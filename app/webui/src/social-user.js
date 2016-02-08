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
      this.userName = user.name;
      this.originalUserName = user.originalName;
      this.specifyName = (this.userName !== this.originalUserName);
      this.email = user.email;
      this.originalEmail = user.originalEmail;
      this.specifyEmail = (this.email !== this.originalEmail);
      this.notifyEmail = user.notifyEmail;
    });
  }

}
