import {customElement, inject, bindable} from 'aurelia-framework';
import {EventAggregator} from 'aurelia-event-aggregator';
import 'components/jqueryui';
import 'components/jqueryui/themes/base/jquery-ui.css!';

function widgetKey(status) {
  return 'taskstatus-' + status;
}

@customElement('task-status')
@inject(EventAggregator)
export class TaskStatus {
  @bindable status = '';
  @bindable widgets = null;
  @bindable tasks = null;

  constructor(eventAggregator) {
    this.eventAggregator = eventAggregator;
  }

  widgetsChanged(newWidgets) {
    if(newWidgets){
      var targetElement = $(this.taskStatusPanel);

      newWidgets.task(widgetKey(this.status), widget => {
        targetElement
          .css({
            position: 'relative',
            top: widget.top,
            left: widget.left
          })
          .width(widget.width)
          .height(widget.height);

        targetElement
          .draggable({
            stop: (event, ui) => {
              widget.top = ui.position.top;
              widget.left = ui.position.left;
              widget.width = targetElement.width();
              widget.height = targetElement.height();
              newWidgets.save(widget);
            }
          })
          .resizable({
            stop: (event, ui) => {
              widget.top = ui.position.top;
              widget.left = ui.position.left;
              widget.width = ui.size.width;
              widget.height = ui.size.height;
              newWidgets.save(widget);
            }
          });
      });

    }
  }

  attached() {
    this.eventAggregator.publish(new TaskStatusAttached());
  }

}

export class TaskStatusAttached {}
