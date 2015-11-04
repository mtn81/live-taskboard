import {inject} from 'aurelia-framework';
import {WidgetService} from './widget-service';

@inject(WidgetService)
export class WidgetManager {

  widgets = [];
  elements = { draggable:[], resizable:[] };

  constructor(widgetService) {
    this.widgetService = widgetService;
  }

  isLoaded() {
    return !!this.groupId;
  }

  load(groupId, callback) {
    this.widgets = this.widgetService.loadAll(groupId, callback);
    this.groupId = groupId;

    this.elements.draggable.forEach(e => e.draggable('destroy'));
    this.elements.draggable.length = 0;
    this.elements.resizable.forEach(e => e.resizable('destroy'));
    this.elements.resizable.length = 0;
  }

  droppable(acceptSelector, element, callback) {
    let me = this;
    let droppableElement = $(element);
    droppableElement.droppable({
      accept: acceptSelector,
      drop: function(event, ui) {
        let draggableElement = ui.draggable[0];
        let widget = draggableElement.widget;
        if(widget){
          console.log(droppableElement.offset(), ui.offset);
          widget.top = ui.offset.top - droppableElement.offset().top;
          widget.left = ui.offset.left - droppableElement.offset().left;
          widget.width = $(draggableElement).width();
          widget.height = $(draggableElement).height();
          me.widgetService.save(widget);
          if(callback) callback(widget);
        }
      }
    });

  }

  entry(widgetId, element, freeOnDrag) {
    let me = this;
    let widget = this._get(widgetId);

    element.widget = widget;

    let targetElement = $(element);
    targetElement
      .css({
        position: 'absolute',
        top: widget.top,
        left: widget.left
      })
      .width(widget.width)
      .height(widget.height);

    targetElement
      .draggable({
        revert: freeOnDrag ? false : 'invalid',
        stop: (event, ui) => {
          if (freeOnDrag) {
            widget.top = ui.position.top;
            widget.left = ui.position.left;
            widget.width = $(element).width();
            widget.height = $(element).height();
            me.widgetService.save(widget);
          }
        }
      })
      .resizable({
        stop: (event, ui) => {
          widget.top = ui.position.top;
          widget.left = ui.position.left;
          widget.width = ui.size.width;
          widget.height = ui.size.height;
          me.widgetService.save(widget);
        }
      });

    this.elements.draggable.push(targetElement);
    this.elements.resizable.push(targetElement);
  }

  _get(widgetId) {
    var w;
    $.each(this.widgets, (i, widget) => {
      if(widget.widgetId === widgetId){
        w = widget;
      }
    });

    return w || { widgetId: widgetId, categoryId: this.groupId };
  }
}
