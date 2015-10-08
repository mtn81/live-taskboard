import _ from 'underscore';
import {inject, customElement, bindable} from 'aurelia-framework';
import {EventAggregator} from 'aurelia-event-aggregator';
import {GlobalError} from 'global-error';

@customElement('global-messages')
@inject(EventAggregator)
export class GlobalMessages {
  @bindable event = null;
  errors = [];

  constructor(eventAggregator){
    this.eventAggregator = eventAggregator;
  }

  eventChanged(newValue) {
    if(!newValue) return;

    let target = !newValue.event ? { event: GlobalError, excludeField: false } : newValue;
    this.eventAggregator.subscribe(target.event, event => {
      const targetErrors = !!target.excludeField ? event.globalErrors : event.errors;
      if (_.isEmpty(targetErrors)) return;

      this.errors.length = 0;
      jQuery.merge(this.errors, targetErrors);

      if(!this.showing){
        this.showing = true;
        toggleSlide(this.messageContainer);
      }
    });
  }

  close(){
    if(this.showing){
      this.showing = false;
      toggleSlide(this.messageContainer);
    }
  }
}

function toggleSlide(element){
  jQuery(element).slideToggle( "slow" );
}
