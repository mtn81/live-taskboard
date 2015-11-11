import {EventAggregator} from 'aurelia-event-aggregator';
import {EventAggregatorWrapper} from './lib/event-aggregator-wrapper';
import {inject} from 'aurelia-framework';
import {UserService, UserRegistered, UserRegisterValidationError, UserRegisterValidationSuccess} from './user/user-service';

@inject(EventAggregator, UserService)
export class UserRegister {

  constructor(eventAggregator, userService){
    this.events = new EventAggregatorWrapper(this, eventAggregator);
    this.userService = userService;
  }

  register(){
    const user = {
      userId: this.userId,
      userName: this.userName,
      email: this.email,
      password: this.password,
      confirmPassword: this.confirmPassword
    };

    this.userService.register(user);
  }

  validate(){
    const user = {
      userId: this.userId,
      userName: this.userName,
      email: this.email,
      password: this.password,
      confirmPassword: this.confirmPassword
    };

    this.userService.validate(user);
  }

  init(){
    this.userId = '';
    this.userName = '',
    this.email = '',
    this.password = '',
    this.confirmPassword = ''
    this.validate();
  }

  attached(){
    this.events.subscribe('user-register.register', payload => {
      this.register();
    });
    this.events.subscribe(UserRegistered, message => {
      this.events.publish('user-register.register.success');
    });
    this.events.subscribe('init.user.register', () => {
      this.init();
    });
    this.events.subscribe('validate.user.register', () => {
      this.validate();
    });
    this.events.subscribe(UserRegisterValidationError, e => {
      this.events.publish('validate.user.register.error', e.error);
      this.events.publish('user-register.disable');
    });
    this.events.subscribe(UserRegisterValidationSuccess, e => {
      this.events.publish('user-register.enable');
    });
  }

}

