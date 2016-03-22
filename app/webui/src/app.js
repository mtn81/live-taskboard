import {inject} from 'aurelia-framework';
import {Router} from 'aurelia-router';
import {HttpClient} from 'aurelia-http-client';
import {AuthContext} from './services/auth/auth-context';
import {AuthService} from './services/auth/auth-service';

@inject(HttpClient, AuthContext, AuthService)
export class App {
  constructor(http, authContext, authService) {
    http.configure(builder => {
      builder.withHeader("Content-Type", "application/json");
    });
    this.authContext = authContext;
    this.authService = authService;
  }

  configureRouter(config, router) {
    this.router = router;

    config.title = 'live-taskboard';
    config.addPipelineStep('authorize', AuthorizeStep);
    config.map([
      { route: ['','login'],
        name: 'login',
        moduleId: 'views/login/login',
        nav: false,
        title: 'ログイン' },
      { route: ['social_login'],
        name: 'social-login',
        moduleId: 'views/social-login/social-login',
        nav: false,
        title: 'ログイン' },
      { route: 'activate/:id',
        name: 'activate',
        moduleId: 'views/activation/activation',
        nav: false,
        title: '利用開始' },
      { route: 'social_user',
        name: 'social-user',
        moduleId: 'views/social-user/social-user',
        nav: false,
        title: 'ユーザ' },
      { route: 'user',
        name: 'user',
        moduleId: 'views/user/user',
        nav: false,
        title: 'ユーザ' },
      { route: 'taskboard',
        name: 'taskboard',
        moduleId: 'views/taskboard/taskboard',
        nav: false,
        title: 'タスクボード',
        auth: true},
      { route: 'join',
        name: 'join',
        moduleId: 'views/join/join',
        nav: false,
        title: 'グループ参加',
        auth: true},
      { route: 'accept',
        name: 'accept',
        moduleId: 'views/accept/accept',
        nav: false,
        title: 'グループ参加承認',
        auth: true},
      { route: 'member/:groupId',
        name: 'member',
        moduleId: 'views/member/member',
        nav: false,
        title: 'メンバー管理',
        auth: true}
    ]);
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

  goToUserPage(){
    if(this.auth.isSocial) {
      this.router.navigate('social_user');
    } else {
      this.router.navigate('user');
    }
  }

}

@inject(AuthContext)
class AuthorizeStep {
  constructor(authContext){
    this.authContext = authContext;
  }
  run(routingContext, next){
    if (routingContext.config.auth) {
      if(!this.authContext.isAuthenticated()){
        return next.cancel(new Redirect('login'));
      }
    }
    return next();
  }
}
