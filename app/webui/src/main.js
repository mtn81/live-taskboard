/*** Handle jQuery plugin naming conflict between jQuery UI and Bootstrap ***/
import 'components/jqueryui';
$.widget.bridge('uibutton', $.ui.button);
$.widget.bridge('uitooltip', $.ui.tooltip);

import 'bootstrap';
import 'bootstrap/css/bootstrap.css!';

export function configure(aurelia) {
  aurelia.use
    .standardConfiguration()
    .developmentLogging();

  aurelia.start().then(a => a.setRoot());
}
