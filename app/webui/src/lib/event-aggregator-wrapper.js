import _ from 'underscore';
import {EventAggregator} from 'aurelia-event-aggregator';
import {GlobalInfo} from '../global-info';

export class EventAggregatorWrapper {

  subscriptionClears = [];

  constructor(vm, eventAggregator) {
    this.eventAggregator = eventAggregator;

    let me = this;

    let vmDeactivate = vm.deactivate;
    vm.deactivate = _.bind(() => {
      if(vmDeactivate) vmDeactivate();
      me.subscriptionClears.forEach(s => s.dispose());
    }, vm);

    let vmDetached = vm.detached;
    vm.detached = _.bind(() => {
      if(vmDetached) vmDetached();
      me.subscriptionClears.forEach(s => s.dispose());
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
  info(targetEvent, message) {
    this.subscribe(targetEvent, () => {
      this.publish(new GlobalInfo([ { message: message } ]));
    });
  }
  publish(key, args) {
    this.eventAggregator.publish(key, args);
  }
}
