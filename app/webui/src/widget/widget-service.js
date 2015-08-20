import {inject} from 'aurelia-framework';
import {HttpClient} from 'aurelia-http-client';
import {EventAggregator} from 'aurelia-event-aggregator';
import {GlobalError} from '../global-error';
import {AuthContext} from 'auth/auth-context';
import {Stomp} from 'stomp-websocket';
import {GroupWidgetStore} from './group-widget-store';

var _widgets = [];

@inject(HttpClient, EventAggregator)
export class WidgetService {

  constructor(http, eventAggregator) {
    this.http = http;
    this.eventAggregator = eventAggregator;
  }

  getStore(groupId) {
    return new GroupWidgetStore(groupId, this, this.eventAggregator);
  }

  loadAll(groupId) {
    this.http
      .get('/api/widget-store/categories/' + groupId + '/widgets/')
      .then(response => {
        let foundWidgets = response.content.data.widgets;
        _widgets.length = 0;
        $.merge(_widgets, foundWidgets);
        this.eventAggregator.publish(new WidgetLoadSuccess());
      })
      ;

    return _widgets;
  }

  save(widget) {
    this.http
      .put('/api/widget-store/categories/' + widget.categoryId + '/widgets/' + widget.widgetId, widget)
      .then(response => {
        console.log(response);
      })
      .catch(response => {
        this.eventAggregator.publish(new GlobalError(response.content.errors));
      });
  }
}

export class WidgetLoadSuccess {}
