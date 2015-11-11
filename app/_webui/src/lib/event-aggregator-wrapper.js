import _ from 'underscore';
import {EventAggregator} from 'aurelia-event-aggregator';

export class EventAggregatorWrapper {

  subscriptionClears = [];

  constructor(vm, eventAggregator) {
    this.eventAggregator = eventAggregator;

    let me = this;

    let vmDeactivate = vm.deactivate;
    vm.deactivate = _.bind(() => {
      if(vmDeactivate) vmDeactivate();
      me.subscriptionClears.forEach(s => s());
    }, vm);

    let vmDetached = vm.detached;
    vm.detached = _.bind(() => {
      if(vmDetached) vmDetached();
      me.subscriptionClears.forEach(s => s());
    }, vm);
  }

  subscribe(key, callback) {
    this.subscriptionClears.push(
        this.eventAggregator.subscribe(key, callback));
  }
  subscribe2(keys, callback) {
    keys.forEach(key => {
      this.subscribe(key, callback);
    });
  }
  publish(key, args) {
    this.eventAggregator.publish(key, args);
  }
}
