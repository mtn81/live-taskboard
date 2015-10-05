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
      form.find('input').change(e => {
        this.eventAggregator.publish(newValue.validateEvent);
      });

      this.eventAggregator.subscribe(newValue.errorEvent, error => {
        error.fieldErrors.forEach(e => {
          form.find('input').each((i, input) => {
            if($(input).attr('value.bind') === e.field){
              $(input).after('<p>' + e.message + '</p>');
            }
          });
        });
      });
    }
  }

}
