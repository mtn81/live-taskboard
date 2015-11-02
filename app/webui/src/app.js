/*** Handle jQuery plugin naming conflict between jQuery UI and Bootstrap ***/
import 'components/jqueryui';
$.widget.bridge('uibutton', $.ui.button);
$.widget.bridge('uitooltip', $.ui.tooltip);

import 'bootstrap';
import 'bootstrap/css/bootstrap.css!';

import {inject} from 'aurelia-framework';
import {Router} from 'aurelia-router';
import {HttpClient} from 'aurelia-http-client';
import {AuthContext} from 'auth/auth-context';
import {AuthService} from 'auth/auth-service';

@inject(Router, HttpClient, AuthContext, AuthService)
export class App {
  constructor(router, http, authContext, authService) {
    this.router = router;
    this.router.configure(config => {
      config.title = 'live-taskboard';
      config.addPipelineStep('authorize', AuthorizeStep);
      config.map([
        { route: ['','login'],   moduleId: './login',      nav: false, title: 'ログイン' },
        { route: 'activate/:id', moduleId: './activation', nav: false, title: 'ログイン' },
        { route: 'taskboard',    moduleId: './taskboard',  nav: false, title: 'タスクボード', auth: true}
      ]);
    });

    http.configure(builder => {
      builder.withHeader("Content-Type", "application/json");
    });

    this.authContext = authContext;
    this.authService = authService;
  }

  get auth(){
    return this.authContext.getAuth();
  }

  get isAuth() {
    return this.authContext.isAuthenticated();
  }

  logout(){
    this.authService.logout();
    this.router.navigate('login');
  }

}

@inject(AuthContext)
class AuthorizeStep {
  constructor(authContext){
    this.authContext = authContext;
  }
  run(routingContext, next){
    if (routingContext.nextInstructions.some(i => i.config.auth)) {
      if(!this.authContext.isAuthenticated()){
        return next.cancel(new Redirect('login'));
      }
    }
    return next();
  }
}
