import _ from 'underscore';
import {customElement, bindable, inject, child} from 'aurelia-framework';
import {EventAggregator} from 'aurelia-event-aggregator';
import {EventAggregatorWrapper} from '../lib/event-aggregator-wrapper';

@customElement('editable-text')
@inject(EventAggregator)
export class EditableLabel {
  @bindable key;
  @bindable event;
  showAsEdit = false;

  constructor(eventAggregator) {
    this.events = new EventAggregatorWrapper(this, eventAggregator);
  }

  change(showAsEdit) {
    this.showAsEdit = showAsEdit;
    if(showAsEdit) {
      _.delay(() => { this.$input.focus(); }, 500);
    }
  }

  attached() {
    this.$input = $(this.textInput);
    this.$view = $(this.textView);

    this.$input.val(this.$view.text());

    this.$input.blur(() => {
      this.change(false);
    });
    this.$input.keydown((e) => {
      if(e.which == 13) this.change(false);
    });
    this.$view.dblclick(() => {
      this.change(true);
    });

    this.$input.change(() => {
      const text = this.$input.val();
      this.$view.text(text);
      if (this.event) {
        this.events.publish(this.event, [this.key, text]);
      }
    });
  }
}

