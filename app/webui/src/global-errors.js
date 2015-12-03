import _ from 'underscore';
import {inject, customElement, bindable} from 'aurelia-framework';
import {EventAggregator} from 'aurelia-event-aggregator';
import {EventAggregatorWrapper} from './lib/event-aggregator-wrapper';
import {GlobalError} from 'global-error';

@customElement('global-errors')
@inject(EventAggregator)
export class GlobalErrors {
  @bindable event = null;
  errors = [];

  constructor(eventAggregator){
    this.events = new EventAggregatorWrapper(this, eventAggregator);
  }

  eventChanged(newValue) {
    if(!newValue) return;

    let target = !newValue.event ? { event: GlobalError, excludeField: false } : newValue;
    this.events.subscribe(target.event, event => {
      const targetErrors = !!target.excludeField ? event.globalErrors : event.errors;
      if (_.isEmpty(targetErrors)) return;

      this.errors.length = 0;
      jQuery.merge(this.errors, targetErrors);

      this.open();
    });
  }

  open(){
    if(!this.showing){
      this.showing = true;
      $(this.messageContainer).slideToggle( "slow", () => {
        _.delay(() => { this.close(); }, 3000);
      });
    }
  }

  close(){
    if(this.showing){
      this.showing = false;
      $(this.messageContainer).slideToggle( "slow" )
    }
  }
}

