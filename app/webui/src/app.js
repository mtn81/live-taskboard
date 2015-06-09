import {inject} from 'aurelia-framework';
import {Router} from 'aurelia-router';
import {HttpClient} from 'aurelia-http-client';
import 'bootstrap';
import 'bootstrap/css/bootstrap.css!';
import {AuthContext} from 'auth/auth-context';

@inject(Router, HttpClient)
export class App {
  constructor(router, http) {
    this.router = router;
    this.router.configure(config => {
      config.title = 'live-taskboard';
      config.addPipelineStep('authorize', AuthorizeStep);
      config.map([
        { route: ['','login'], moduleId: './login', nav: false, title: 'ログイン' },
        { route: ['taskboard'], moduleId: './taskboard', nav: false, title: 'タスクボード', auth: true}
      ]);
    });
    
    http.configure(builder => {
      builder.withHeader("Content-Type", "application/json");
    });
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
