import {inject} from 'aurelia-framework';
import {Router} from 'aurelia-router';
import {HttpClient} from 'aurelia-http-client';
import 'bootstrap';
import 'bootstrap/css/bootstrap.css!';

@inject(Router, HttpClient)
export class App {
  constructor(router, http) {
    this.router = router;
    this.router.configure(config => {
      config.title = 'live-taskboard';
      config.map([
        { route: ['','login'], moduleId: './login', nav: false, title: 'ログイン' },
        { route: ['taskboard'], moduleId: './taskboard', nav: false, title: 'タスクボード'}
      ]);
    });
    
    http.configure(builder => {
      builder.withHeader("Content-Type", "application/json");
    });
  }
}

