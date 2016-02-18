import {inject} from 'aurelia-framework';
import {EventAggregator} from 'aurelia-event-aggregator';
import {EventAggregatorWrapper} from './lib/event-aggregator-wrapper';
import {GlobalError} from 'global-error';
import {UserService} from './user/user-service';

@inject(EventAggregator, UserService)
export class SocialUser {
  _specifyName = false;
  _userName = '';
  _specifyEmail = false;
  _email = '';
  originalUserName = '';
  notifyEmail = true;
  originalEmail = '';

  constructor(eventAggregator, userService) {
    this.userService = userService;
    this.events = new EventAggregatorWrapper(this, eventAggregator);
  }

  modifyUser() {
    const user = {
      userName: this._userName,
      email: this._email,
      notifyEmail: this.notifyEmail
    };
    this.userService.saveSocialUser(user);
  }


  activate() {
    this.userService.loadSocialUser(user => {
      this._specifyName = !!user.name;
      this._userName = user.name;
      this.originalUserName = user.originalName;
      this._specifyEmail = !!user.email;
      this._email = user.email;
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

  get email() {
    return this._specifyEmail ? this._email : this.originalEmail;
  }
  set email(email) {
    this._email = email;
  }
  get specifyEmail() {
    return this._specifyEmail;
  }
  set specifyEmail(specifyEmail) {
    this._specifyEmail = specifyEmail;
    this._email = specifyEmail ? this.originalEmail : null;
  }

}
