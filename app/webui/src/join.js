import {inject} from 'aurelia-framework';
import {Router} from 'aurelia-router';
import {AuthContext} from './auth/auth-context';
import {EventAggregator} from 'aurelia-event-aggregator';
import {WidgetManager} from './widget/widget-manager';
import {TaskService} from './task/task-service';
import {TaskStatusAttached} from './task-status';
import {TaskRegisterAttached} from './task-register';
import 'components/jqueryui';

@inject(Router, AuthContext, EventAggregator, WidgetManager, TaskService)
export class Join {


  constructor(router, authContext, eventAggregator, widgetManager, taskService){
    this.router = router;
    this.authContext = authContext;
    this.eventAggregator = eventAggregator;
    this.widgetManager = widgetManager;
  }


}
