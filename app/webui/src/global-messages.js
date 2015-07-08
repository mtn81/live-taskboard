import {inject, customElement, bindable} from 'aurelia-framework';
import {EventAggregator} from 'aurelia-event-aggregator';
import {GlobalError} from 'global-error';

@customElement('global-messages')
@inject(EventAggregator)
export class GlobalMessages {
  errors = [];

  constructor(eventAggregator){
    eventAggregator.subscribe(GlobalError, event => {
      this.errors.length = 0;
      if (event.errors) jQuery.merge(this.errors, event.errors);

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
