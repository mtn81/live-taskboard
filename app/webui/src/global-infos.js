import _ from 'underscore';
import {inject, customElement, bindable} from 'aurelia-framework';
import {EventAggregator} from 'aurelia-event-aggregator';
import {EventAggregatorWrapper} from './lib/event-aggregator-wrapper';
import {GlobalInfo} from 'global-info';

@customElement('global-infos')
@inject(EventAggregator)
export class GlobalInfos {
  infos = [];

  constructor(eventAggregator){
    this.events = new EventAggregatorWrapper(this, eventAggregator);
  }

  attached() {
    this.events.subscribe(GlobalInfo, event => {
      const infos = event.infos;
      if (_.isEmpty(infos)) return;

      this.infos.splice(0, this.infos.length, ...infos);
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
