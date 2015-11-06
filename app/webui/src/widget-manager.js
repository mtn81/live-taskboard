import _ from 'underscore';
import {inject} from 'aurelia-framework';
import {WidgetService} from './widget/widget-service';
import {EventAggregator} from 'aurelia-event-aggregator';

@inject(WidgetService, EventAggregator)
export class WidgetManager {

  widgetHandlers = [];
  status = '';

  constructor(widgetService, eventAggregator) {
    this.widgetService = widgetService;
    this.eventAggregator = eventAggregator;

    this.eventAggregator.subscribe('group.selected', group => {
      this.groupId = group.groupId;
      this.status = 'loading';

      this.widgetService.loadAll(this.groupId, (widgets) => {
        this.widgets = widgets;
        this.status = 'loaded';

        this.widgetHandlers.forEach(handler => {
          handler.element.widget = this._widget(handler.widgetId);
          handler.callback(handler.element);
        });
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
