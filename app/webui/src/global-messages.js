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

  attached() {
    let target = !this.event ? GlobalError : this.event;
    this.eventAggregator.subscribe(target, event => {
      const globalErrors = event.globalErrors;
      if (_.isEmpty(globalErrors)) return;

      this.errors.length = 0;
      jQuery.merge(this.errors, globalErrors);

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
