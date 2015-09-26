import {inject, customAttribute, bindable} from 'aurelia-framework';
import {WidgetManager} from './widget/widget-manager';
import {EventAggregator} from 'aurelia-event-aggregator';

@customAttribute('widget')
@inject(Element, WidgetManager, EventAggregator)
export class Widget {
  _subscriptions = [];

  constructor(element, widgetManager, eventAggregator) {
    this.element = element;
    this.widgetManager = widgetManager;
    this.eventAggregator = eventAggregator;
  }

  valueChanged(newValue){
    if (newValue) {
      this._subscriptions.forEach((s) => s());

      if (this.widgetManager.isLoaded()) {
        this.widgetManager.entry(newValue.widgetId, this.element, newValue.freeOnDrag);
      } else {
        this._subscriptions.push(
          this.eventAggregator.subscribe('widget.reloaded', () => {
            this.widgetManager.entry(newValue.widgetId, this.element, newValue.freeOnDrag);
          })
        );
      }
    }
  }
}
