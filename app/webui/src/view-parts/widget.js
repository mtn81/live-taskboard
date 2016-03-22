import {inject, customAttribute, bindable} from 'aurelia-framework';
import {EventAggregator} from 'aurelia-event-aggregator';
import {EventAggregatorWrapper} from '../lib/event-aggregator-wrapper';
import {WidgetService} from '../services/widget/widget-service';
import {WidgetManager} from './widget-manager';

@customAttribute('widget')
@inject(Element, WidgetManager, WidgetService, EventAggregator)
export class Widget {

  constructor(element, widgetManager, widgetService, eventAggregator) {
    this.element = element;
    this.widgetManager = widgetManager;
    this.widgetService = widgetService;
    this.events = new EventAggregatorWrapper(this, eventAggregator);
  }

  valueChanged(newValue){
    this.value = newValue;
    if (this.value) {

      this.widgetManager.entry(this.value.widgetId, this.element,
        (element) => {
          let widget = element.widget;
          let targetElement = $(element);
          targetElement
            .css({
              position: 'absolute',
              top: widget.top,
              left: widget.left
            })
            .width(widget.width)
            .height(widget.height);

          if (targetElement.draggable('instance'))
            targetElement.draggable('destroy');
          if (targetElement.resizable('instance'))
            targetElement.resizable('destroy');

          targetElement
            .draggable({
              revert: this.value.freeOnDrag ? false : 'invalid',
              stop: (event, ui) => {
                if (this.value.freeOnDrag) {
                  widget.top = ui.position.top;
                  widget.left = ui.position.left;
                  widget.width = targetElement.width();
                  widget.height = targetElement.height();
                  this.widgetService.save(widget);
                }
              }
            })
            .resizable({
              stop: (event, ui) => {
                widget.top = ui.position.top;
                widget.left = ui.position.left;
                widget.width = ui.size.width;
                widget.height = ui.size.height;
                this.widgetService.save(widget);
              }
            });
        }
      );
    }
  }

  unbind() {
    this.widgetManager.cancel(this.element);
  }

}
