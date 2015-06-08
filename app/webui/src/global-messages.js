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
      jQuery.merge(this.errors, event.errors);

      this.toggleContainer();
    });
  }

  toggleContainer(){
    jQuery(this.messageContainer).slideToggle( "slow" );
  }

}
