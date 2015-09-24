import {inject} from 'aurelia-framework';
import {WidgetService} from './widget-service';

@inject(WidgetService)
export class WidgetManager {

  widgets = [];

  constructor(widgetService) {
    this.widgetService = widgetService;
  }

  isLoaded() {
    return !!this.groupId;
  }

  load(groupId, callback) {
    this.widgets = this.widgetService.loadAll(groupId, callback);
    this.groupId = groupId;
  }

  entry(widgetId, element) {
    let me = this;
    let widget = this._get(widgetId);

    $(element)
      .css({
        position: 'relative',
        top: widget.top,
        left: widget.left
      })
      .width(widget.width)
      .height(widget.height);

    $(element)
      .draggable({
        stop: (event, ui) => {
          widget.top = ui.position.top;
          widget.left = ui.position.left;
          widget.width = $(element).width();
          widget.height = $(element).height();
          me.widgetService.save(widget);
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
