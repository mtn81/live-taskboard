import {inject} from 'aurelia-framework';
import {HttpClient} from 'aurelia-http-client';
import {EventAggregator} from 'aurelia-event-aggregator';
import {GlobalError} from '../global-error';
import {AuthContext} from 'auth/auth-context';
import {Stomp} from 'stomp-websocket';
import {HttpClientWrapper, CachedHttpLoader} from '../lib/http-client-wrapper';


@inject(HttpClient, EventAggregator, AuthContext)
export class WidgetService {

  _watchingWidgetChanges = {};

  constructor(http, eventAggregator, authContext) {
    this.http = new HttpClientWrapper(http, eventAggregator).withAuth(authContext);
    this.httpLoader = new CachedHttpLoader(http, eventAggregator).withAuth(authContext);
    this.eventAggregator = eventAggregator;
    this.authContext = authContext;
  }

  loadAll(groupId, callback) {
    return this.httpLoader.list(
      `/api/widget-store/categories/${groupId}/widgets/`,
      response => {
        let widgets = response.content.data.widgets;
        callback(widgets);
        return widgets;
      });
  }

  save(widget) {
    this.http.call(http => {
      return http
        .put(`/api/widget-store/categories/${widget.categoryId}/widgets/${widget.widgetId}`, widget)
        .then(response => {
        });
    }, true);
  }

  watchWidgetChange(groupId, callback) {
    if(!this.authContext.isAuthenticated()) return;
    if(this._watchingWidgetChanges[groupId]) return;

    let websocket = new WebSocket('ws://localhost:38080/widget-store/websocket/notify');
    let stompClient = window.Stomp.over(websocket);
    stompClient.connect({}, frame => {
      stompClient.subscribe(`/topic/${groupId}/widget_changed`, response => {
        if (callback) callback(JSON.parse(response.body));
      });
    });

    this._watchingWidgetChanges[groupId] = true;
  }
}

