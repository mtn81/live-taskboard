import _ from 'underscore';
import {inject} from 'aurelia-framework';
import {EventAggregator} from 'aurelia-event-aggregator';
import {WidgetService} from '../services/widget/widget-service';
import {AuthContext} from '../services/auth/auth-context';

@inject(WidgetService, EventAggregator, AuthContext)
export class WidgetManager {

  widgetHandlers = [];
  status = '';

  constructor(widgetService, eventAggregator, authContext) {
    this.widgetService = widgetService;
    this.eventAggregator = eventAggregator;

    this.eventAggregator.subscribe('task.loaded', groupId => {
      this.groupId = groupId;
      this.loadWidgets();

      this.widgetService.watchWidgetChange(this.groupId, (widgetChange, self) => {
        if(!self) this.loadWidgets();
      });
    });
  }

  loadWidgets() {
    this.status = 'loading';

    this.widgetService.loadAll(this.groupId, (widgets) => {
      this.widgets = widgets;
      this.status = 'loaded';

      this.widgetHandlers.forEach(handler => {
        handler.element.widget = this._widget(handler.widgetId);
        handler.callback(handler.element);
      });
    });
  }

  entry(widgetId, element, callback) {
    if (this.status == 'loaded') {
      element.widget = this._widget(widgetId);
      callback(element);
    }

    this.cancel(element);
    this.widgetHandlers.push({ widgetId: widgetId, element: element, callback: callback });
  }

  cancel(element) {
    this.widgetHandlers = _.reject(this.widgetHandlers, handler => { return handler.element === element; });
  }

  _widget(widgetId) {
    var w;
    $.each(this.widgets, (i, widget) => {
      if(widget.widgetId === widgetId){
        w = widget;
      }
    });

    return w || { widgetId: widgetId, categoryId: this.groupId };
  }
}
