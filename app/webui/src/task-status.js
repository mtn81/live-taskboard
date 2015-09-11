import {customElement, inject, bindable} from 'aurelia-framework';
import {EventAggregator} from 'aurelia-event-aggregator';
import bootbox from 'bootbox';
import {WidgetManager} from './widget/widget-manager';
import {TaskService, TaskRemoved} from './task/task-service';
import 'components/jqueryui';
import 'components/jqueryui/themes/base/jquery-ui.css!';


@customElement('task-status')
@inject(EventAggregator, WidgetManager, TaskService)
export class TaskStatus {
  @bindable status = '';
  @bindable tasks = null;
  _subscription = [];

  constructor(eventAggregator, widgetManager, taskService) {
    this.eventAggregator = eventAggregator;
    this.widgetManager = widgetManager;
    this.taskService = taskService;
  }

  removeTask(task) {
    bootbox.confirm('削除します。よろしいですか？', confirmation => {
      if(confirmation) {
        this.taskService.remove(this.group.groupId, task.taskId);
      }
    });
  }
  
  bind(bindingContext) {
    //this.taskboardContext = bindingContext.taskboardContext;
  }

  attached() {
    let targetElement = $(this.taskStatusPanel);

    this._subscription.push(
      this.eventAggregator.subscribe('widget.reloaded', () => {
        this.widgetManager.entry(this._widgetKey(this.status), targetElement);
      })
    );
    this._subscription.push(
      this.eventAggregator.subscribe('group.selected', group => {
        this.group = group;
      })
    );
    this._subscription.push(
      this.eventAggregator.subscribe(TaskRemoved, () => {
        this.eventAggregator.publish('task-status.remove.success');
      })
    );

    this.eventAggregator.publish(new TaskStatusAttached(this.status));
  }

  detached(){
    for(var i=0; i < this._subscription.length; i++){
      this._subscription[i]();
    }
  }

  _widgetKey(status) {
    return 'taskstatus-' + status;
  }
}

export class TaskStatusAttached {
  constructor(status){
    this.status = status;
  }
}
