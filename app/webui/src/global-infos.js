import _ from 'underscore';
import {inject, customElement, bindable} from 'aurelia-framework';
import {EventAggregator} from 'aurelia-event-aggregator';
import {GlobalInfo} from 'global-info';

@customElement('global-infos')
@inject(EventAggregator)
export class GlobalInfos {
  infos = [];

  constructor(eventAggregator){
    this.eventAggregator = eventAggregator;

    this.eventAggregator.subscribe(GlobalInfo, event => {
      const infos = event.infos;
      if (_.isEmpty(infos)) return;

      this.infos.length = 0;
      jQuery.merge(this.infos, infos);

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
