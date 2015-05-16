import {inject} from 'aurelia-framework';
import {AuthContext} from './auth/auth-context';

@inject(AuthContext)
export class Taskboard {
  auth = null;
  
  constructor(authContext){
    this.auth = authContext.auth;
  }
}
