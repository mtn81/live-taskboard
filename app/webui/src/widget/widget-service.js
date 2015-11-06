import {inject} from 'aurelia-framework';
import {HttpClient} from 'aurelia-http-client';
import {EventAggregator} from 'aurelia-event-aggregator';
import {GlobalError} from '../global-error';
import {AuthContext} from 'auth/auth-context';
import {Stomp} from 'stomp-websocket';

var _widgets = [];

@inject(HttpClient, EventAggregator)
export class WidgetService {
  
  _loading = false;

  constructor(http, eventAggregator) {
    this.http = http;
    this.eventAggregator = eventAggregator;
  }

  loadAll(groupId, callback) {

    console.log('widget service loading ?', this._loading);

    if(this._loading) return _widgets;
    this._loading = true;

    this.http
      .get('/api/widget-store/categories/' + groupId + '/widgets/')
      .then(response => {
        let foundWidgets = response.content.data.widgets;
        _widgets.length = 0;
        $.merge(_widgets, foundWidgets);
        callback(_widgets);
        this._loading = false;
      });

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

