/*** Handle jQuery plugin naming conflict between jQuery UI and Bootstrap ***/
import 'components/jqueryui';
$.widget.bridge('uibutton', $.ui.button);
$.widget.bridge('uitooltip', $.ui.tooltip);

import 'bootstrap';
import 'bootstrap/css/bootstrap.css!';

import 'bootstrap-select/js/bootstrap-select';
import 'bootstrap-select/css/bootstrap-select.css!';

export function configure(aurelia) {
  aurelia.use
    .standardConfiguration()
    .developmentLogging()
    .globalResources('./dist/lib/date-format');

  aurelia.start().then(a => a.setRoot());
}
