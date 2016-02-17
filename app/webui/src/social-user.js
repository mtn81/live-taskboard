import {inject} from 'aurelia-framework';
import {EventAggregator} from 'aurelia-event-aggregator';
import {EventAggregatorWrapper} from './lib/event-aggregator-wrapper';
import {GlobalError} from 'global-error';
import {UserService} from './user/user-service';

@inject(EventAggregator, UserService)
export class SocialUser {
  _specifyName = false;
  _userName = '';
  originalUserName = '';
  notifyEmail = true;
  specifyEmail = false;
  email = '';
  originalEmail = '';

  constructor(eventAggregator, userService) {
    this.userService = userService;
    this.events = new EventAggregatorWrapper(this, eventAggregator);
  }

  modifyUser() {
    const user = {
      userName: this._userName,
      email: this.specifyEmail ? this.email : null,
      notifyEmail: this.notifyEmail
    };
    this.userService.saveSocialUser(user);
  }


  activate() {
    this.userService.loadSocialUser(user => {
      this._specifyName = !!user.name;
      this._userName = user.name;
      this.originalUserName = user.originalName;
      this.specifyEmail = !!user.email;
      this.email = this.specifyEmail ? user.email : user.originalEmail;
      this.originalEmail = user.originalEmail;
      this.notifyEmail = user.notifyEmail;
    });
  }

  get userName() {
    return this._specifyName ? this._userName : this.originalUserName;
  }
  set userName(userName) {
    this._userName = userName;
  }
  get specifyName() {
    return this._specifyName;
  }
  set specifyName(specifyName) {
    this._specifyName = specifyName;
    this._userName = specifyName ? this.originalUserName : null;
  }

}
