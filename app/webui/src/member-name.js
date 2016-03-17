import _ from 'underscore';
import {customElement, bindable, inject, child} from 'aurelia-framework';

@customElement('member-name')
export class MemberName {

  @bindable type;
  @bindable name;

  get memberType() {
    if(this.type == 'GOOGLE') return 'google';
    if(this.type == 'YAHOO') return 'yahoo';
    if(this.type == 'FACEBOOK') return 'facebook';
    if(this.type == 'TWITTER') return 'twitter';
    return 'other';
  }
}
