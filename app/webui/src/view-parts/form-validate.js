import 'bootstrap';
import _ from 'underscore';
import {inject, customAttribute} from 'aurelia-framework';
import {EventAggregator} from 'aurelia-event-aggregator';
import {EventAggregatorWrapper} from '../lib/event-aggregator-wrapper';


@customAttribute('formvalid')
@inject(Element, EventAggregator)
export class FormValidate {

  constructor(element, eventAggregator) {
    this.element = element;
    this.events = new EventAggregatorWrapper(this, eventAggregator);
  }

  valueChanged(newValue){
    const form = $(this.element);
    if(newValue){
      const $formInputs = form.find(':input');

      $formInputs.change(e => {
        $formInputs.each(i => {
          let $input = $formInputs.eq(i);
          $input.parent('.form-group')
            .removeClass('has-error')
            .removeClass('has-feedback')
            .find('span.glyphicon-warning-sign').remove();
          if ($input.data('bs.tooltip')) {
            $input.data('bs.tooltip').options.title = '';
          }
        });

        this.events.publish(newValue);
      });

      this.events.subscribe(newValue + '.error', error => {
        const messages = {};
        const $inputs = {};
        error.fieldErrors.forEach(e => {
          $formInputs.each(i => {
            let $input = $formInputs.eq(i);
            if($input.attr('value.bind') === e.field){
              messages[e.field] = messages[e.field] || '';
              messages[e.field] += '<li>' + e.message + '</li>';
              $inputs[e.field] = $input;
            }
          });
        });

        $formInputs.each(i => {
          let $input = $formInputs.eq(i);
          let field = $input.attr('value.bind');
          if($inputs[field]){
            $input.parent('.form-group')
              .addClass('has-error')
              .addClass('has-feedback');
            $input
              .after('<span class="glyphicon glyphicon-warning-sign form-control-feedback" aria-hidden="true"></span>');

            if ($input.data('bs.tooltip')) {
              $input.data('bs.tooltip').options.title = '<ul>' + messages[field] + '</ul>';
            } else {
              $input.tooltip({
                html: true,
                trigger: 'focus',
                title: '<ul>' + messages[field] + '</ul>'
              });
            }
          }
        });

      });
    }
  }

}
