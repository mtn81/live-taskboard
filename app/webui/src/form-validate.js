import 'bootstrap';
import _ from 'underscore';
import {inject, customAttribute} from 'aurelia-framework';
import {EventAggregator} from 'aurelia-event-aggregator';

@customAttribute('formvalid')
@inject(Element, EventAggregator)
export class FormValidate {

  constructor(element, eventAggregator) {
    this.element = element;
    this.eventAggregator = eventAggregator;
  }

  valueChanged(newValue){
    const form = $(this.element);
    if(newValue){
      const $formInputs = form.find('input');
      $formInputs.change(e => {
        form.find('.glyphicon-warning-sign').remove();
        form.find('.form-group')
            .removeClass('has-error')
            .removeClass('has-feedback');
        $formInputs.each(i => {
          let $input = $formInputs.eq(i);
          $input.tooltip('destroy');
        });

        this.eventAggregator.publish(newValue);
      });

      this.eventAggregator.subscribe(newValue + '.error', error => {
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

        _.each($inputs, ($input, field) => {
          $input
            .parent('.form-group')
            .addClass('has-error')
            .addClass('has-feedback');
          $input
            .after('<span class="glyphicon glyphicon-warning-sign form-control-feedback" aria-hidden="true"></span>')
            .attr('data-html', true)
            .attr('data-container', 'body')
            .attr('data-trigger', 'focus')
            .attr('data-title', '<ul style="position:relative;left:-10px;margin-bottom:0px;">' + messages[field] + '</ul>')
            .attr('data-placement', 'top');
          $input
            .tooltip('show');
        });

      });
    }
  }

}
