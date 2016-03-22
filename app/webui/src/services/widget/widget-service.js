import {inject} from 'aurelia-framework';
import {HttpClient} from 'aurelia-http-client';
import {EventAggregator} from 'aurelia-event-aggregator';
import {GlobalError} from '../../lib/global-error';
import {HttpClientWrapper, CachedHttpLoader, StompClient} from '../../lib/http-client-wrapper';
import {wsApi, wsWebSocket} from '../../lib/env-resolver';
import {AuthContext} from '../../services/auth/auth-context';

@inject(HttpClient, EventAggregator, AuthContext)
export class WidgetService {

  constructor(http, eventAggregator, authContext) {
    this.http = new HttpClientWrapper(http, eventAggregator).withAuth(authContext);
    this.httpLoader = new CachedHttpLoader(http, eventAggregator).withAuth(authContext);
    this.stomp = new StompClient(wsWebSocket('/notify'), authContext);
    this.eventAggregator = eventAggregator;
    this.authContext = authContext;
  }

  loadAll(groupId, callback) {
    return this.httpLoader.list(
      wsApi(`/categories/${groupId}/widgets/`),
      response => {
        let widgets = response.content.data.widgets;
        callback(widgets);
        return widgets;
      });
  }

  save(widget) {
    this.http.call(http => {
      return http
        .put(wsApi(`/categories/${widget.categoryId}/widgets/${widget.widgetId}`), widget)
        .then(response => {
        });
    }, true);
  }

  watchWidgetChange(groupId, callback) {
    this.stomp.subscribe(`/topic/${groupId}/widget_changed`, widgetChange => {
      callback(widgetChange, this.authContext.hasClientId(widgetChange.clientId));
    });
  }
}

