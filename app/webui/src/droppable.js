import {inject, customAttribute} from 'aurelia-framework';
import {WidgetService} from './widget/widget-service';

@customAttribute('droppable')
@inject(Element, WidgetService)
export class Droppable {

  constructor(element, widgetService) {
    this.element = element;
    this.widgetService = widgetService;
  }

  valueChanged(newValue){
    this.value = newValue;
    if (this.value) {

      let droppableElement = $(this.element);
      droppableElement.droppable({
        accept: this.value.acceptSelector,
        drop: (event, ui) => {
          let draggableElement = ui.draggable[0];
          let widget = draggableElement.widget;
          if(widget){
            widget.top = ui.offset.top - droppableElement.offset().top;
            widget.left = ui.offset.left - droppableElement.offset().left;
            widget.width = $(draggableElement).width();
            widget.height = $(draggableElement).height();
            this.widgetService.save(widget);
            if(this.value.callback) this.value.callback(widget.widgetId);
          }
        }
      });
    }
  }
}
