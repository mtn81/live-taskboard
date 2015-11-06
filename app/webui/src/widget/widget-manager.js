import _ from 'underscore';
import {inject} from 'aurelia-framework';
import {WidgetService} from './widget-service';

@inject(WidgetService)
export class WidgetManager {

  widgets = [];
  elements = [];

  constructor(widgetService) {
    this.widgetService = widgetService;
  }

  isLoaded() {
    return !!this.groupId;
  }

  load(groupId, callback) {
    this.widgets = this.widgetService.loadAll(groupId, callback);
    this.groupId = groupId;

    console.log(this.elements.length);
    let e = this.elements.shift();
    while(e){
      e.draggable('destroy');
      e.resizable('destroy');
      e = this.elements.shift();
    }
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
            widget.width = targetElement.width();
            widget.height = targetElement.height();
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

    if(!this._entried(element)) {
      this.elements.push(targetElement);
    }
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
  _entried(element) {
    return _.some(this.elements, e => e[0] === element);
  }
}
