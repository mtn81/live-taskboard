import {WidgetLoadSuccess} from './widget-service';

export class GroupWidgetStore {
  widgets = [];
  tasks = [];
  widgetLoaded = false;

  constructor(groupId, widgetService, eventAggregator) {
    this.groupId = groupId;
    this.widgetService = widgetService;

    this.widgets = this.widgetService.loadAll(groupId);

    let me = this;
    eventAggregator.subscribe(WidgetLoadSuccess, payload => {
      $.each(me.tasks, (i, task) => task());
      me.tasks.length = 0;
      me.widgetLoaded = true;
    })
  }

  task(widgetId, widgetTask) {
    var me = this;
    let task = function() {
      let widget = me._get(widgetId);
      widgetTask(widget);
    };

    if(this.widgetLoaded){
      task();
    }else{
      this.tasks.push(task);
    }
  }

  save(widget) {
    this.widgetService.save(widget);
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
