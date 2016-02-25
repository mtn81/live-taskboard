import {inject} from 'aurelia-framework';
import {EventAggregator} from 'aurelia-event-aggregator';
import {EventAggregatorWrapper} from './lib/event-aggregator-wrapper';
import {GlobalError} from 'global-error';
import {UserService, UserChanged} from './user/user-service';

@inject(EventAggregator, UserService)
export class User {

  userName = '';
  email = '';
  notifyEmail = false;

  constructor(eventAggregator, userService) {
    this.userService = userService;
    this.events = new EventAggregatorWrapper(this, eventAggregator);
  }

  modifyUser() {
    this.events.info(UserChanged, 'ユーザ情報を変更しました。');
    this.userService.saveUser({
      userName: this.userName,
      email: this.email,
      notifyEmail: this.notifyEmail
    });
  }

  activate() {
    this.userService.loadUser(user => {
      this.userName = user.name;
      this.email = user.email;
      this.notifyEmail = user.notifyEmail;
    });
  }
}
