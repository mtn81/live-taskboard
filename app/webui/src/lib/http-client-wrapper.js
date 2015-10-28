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

  call(httpCall) {
    if(this.loading) return;
    if(!!this.authContext && !this.authContext.isAuthenticated()) return;

    this.http.configure(builder => {
      builder.withHeader('X-AuthAccess-AuthId', this.authContext.getAuth().id);
    });

    this.loading = true;

    httpCall(this.http)
      .then(response => {
        this.loading = false;
      })
      .catch(response => {
        this._loading = false;
        this.eventAggregator.publish(new GlobalError(response.content.errors));
      });
  }
}
