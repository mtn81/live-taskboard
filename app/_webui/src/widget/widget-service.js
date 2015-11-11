import {inject} from 'aurelia-framework';
import {HttpClient} from 'aurelia-http-client';
import {EventAggregator} from 'aurelia-event-aggregator';
import {GlobalError} from '../global-error';
import {AuthContext} from 'auth/auth-context';
import {Stomp} from 'stomp-websocket';
import {HttpClientWrapper} from '../lib/http-client-wrapper';

var _widgets = [];

@inject(HttpClient, EventAggregator, AuthContext)
export class WidgetService {

  constructor(http, eventAggregator, authContext) {
    this.http = new HttpClientWrapper(http, eventAggregator).withAuth(authContext);
    this.eventAggregator = eventAggregator;
  }

  loadAll(groupId, callback) {

    this.http.call(http => {
      return http
        .get(`/api/widget-store/categories/${groupId}/widgets/`)
        .then(response => {
          let foundWidgets = response.content.data.widgets;
          _widgets.length = 0;
          $.merge(_widgets, foundWidgets);
          callback(_widgets);
          this._loading = false;
        });
    });

    return _widgets;
  }

  save(widget) {
    this.http.call(http => {
      return http
        .put(`/api/widget-store/categories/${widget.categoryId}/widgets/${widget.widgetId}`, widget)
        .then(response => {
        });
    }, true);
  }
}

