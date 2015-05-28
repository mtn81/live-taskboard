import {inject} from 'aurelia-framework';
import {Router} from 'aurelia-router';
import {AuthContext} from './auth/auth-context';

@inject(Router, AuthContext)
export class Taskboard {

  constructor(router, authContext){
    this.router = router;
    this.authContext = authContext;
  }

  get auth(){
    return this.authContext.getAuth();
  }

  logout(){
    this.authContext.remove();
    this.router.navigate('login');
  }
}
