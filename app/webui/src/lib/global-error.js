import _ from 'underscore';

export class GlobalError{
  errors = [];

  constructor(errors){
    this.errors = errors;
  }

  get globalErrors(){
    return _.filter(this.errors, e => {
      return !e.field || e.field === '';
    });
  }

  get fieldErrors(){
    return _.filter(this.errors, e => {
      return e.field && e.field.length > 0;
    });
  }

}
