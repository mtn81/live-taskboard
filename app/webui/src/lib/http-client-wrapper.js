import {EventAggregator} from 'aurelia-event-aggregator';
import {GlobalError} from '../global-error';

export class HttpClientWrapper {

  constructor(http, eventAggregator) {
    this.http = http;
    this.eventAggregator = eventAggregator;
  }

  withAuth(authContext) {
    this.authContext = authContext;
    return this;
  }

  call(httpCall, nosync) {
    if(!nosync && this.loading) return;
    if(!!this.authContext && !this.authContext.isAuthenticated()) return;

    this.http.configure(builder => {
      builder.withHeader('X-AuthAccess-AuthId', this.authContext.getAuth().id);
    });

    if(!nosync) this.loading = true;

    httpCall(this.http, this)
      .then(response => {
        if(!nosync) this.loading = false;
      })
      .catch(response => {
        if(!nosync) this.loading = false;
        this.eventAggregator.publish(new GlobalError(response.content.errors));
      });
  }

}
