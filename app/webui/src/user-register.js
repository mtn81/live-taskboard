import {EventAggregator} from 'aurelia-event-aggregator';
import {inject} from 'aurelia-framework';
import {UserService, UserRegistered} from './user/user-service';

@inject(EventAggregator, UserService)
export class UserRegister {

  _subscription = [];

  constructor(eventAggregator, userService){
    this.eventAggregator = eventAggregator;
    this.userService = userService;
  }

  register(){
    const user = {
      userId: this.userId,
      name: this.userName,
      email: this.email,
      password: this.password,
      confirmPassword: this.confirmPassword
    };

    this.userService.register(user);
  }

  attached(){
    this._subscription.push(
      this.eventAggregator.subscribe('user-register.register', payload => {
        this.register();
      })
    );
    this._subscription.push(
      this.eventAggregator.subscribe(UserRegistered, message => {
        this.eventAggregator.publish('user-register.register.success');
      })
    );
    this._subscription.push(
      this.eventAggregator.subscribe('validate.user.register', () => {
        console.log('validate !');
      })
    );
  }
  detached(){
    for(var i=0; i < this._subscription.length; i++){
      this._subscription[i]();
    }
  }

}

