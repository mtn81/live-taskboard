import _ from 'underscore';
import {customElement, bindable, inject, child} from 'aurelia-framework';

@customElement('member-name')
export class MemberName {

  @bindable type;
  @bindable name;

}
