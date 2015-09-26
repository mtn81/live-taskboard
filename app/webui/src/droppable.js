import {inject, customAttribute} from 'aurelia-framework';
import {WidgetManager} from './widget/widget-manager';
import {EventAggregator} from 'aurelia-event-aggregator';

@customAttribute('droppable')
@inject(Element, WidgetManager, EventAggregator)
export class Droppable {

  constructor(element, widgetManager, eventAggregator) {
    this.element = element;
    this.widgetManager = widgetManager;
    this.eventAggregator = eventAggregator;
  }

  valueChanged(newValue){
    if (newValue) {
      this.widgetManager.droppable(newValue.acceptableSelector, this.element, (widget) => {
        newValue.callback(widget.widgetId);
      });
    }
  }
}
