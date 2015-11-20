import _ from 'underscore';
import {EventAggregator} from 'aurelia-event-aggregator';
import {GlobalError} from '../global-error';

export class HttpClientWrapper {
  keyLoading = {};

  constructor(http, eventAggregator) {
    this.http = http;
    this.eventAggregator = eventAggregator;
  }

  withAuth(authContext) {
    this.authContext = authContext;
    return this;
  }

  call(httpCall, nosync, syncKey) {
    if(this._isLoading(nosync, syncKey)) return;
    if(!!this.authContext && !this.authContext.isAuthenticated()) return;

    this.http.configure(builder => {
      builder.withHeader('X-AuthAccess-AuthId', this.authContext.getAuth().id);
    });

    this._setLoading(nosync, syncKey, true);

    httpCall(this.http, this)
      .then(response => {
        this._setLoading(nosync, syncKey, false);
      })
      .catch(response => {
        console.log('error !', response.content);
        this._setLoading(nosync, syncKey, false);

        let errors;
        if (response.content.errors) {
          errors = response.content.errors;
        } else if (_.isString(response.content) && response.content.includes('ECONNREFUSED')) {
          errors = [{ message: 'サーバに接続できませんでした。'}];
        } else {
          errors = [{ message: 'エラーが発生しました。'}];
        }
        this.eventAggregator.publish(new GlobalError(errors));
      });
  }

  _setLoading(nosync, syncKey, isLoading) {
    if(!nosync){
      if(syncKey){
        this.keyLoading[syncKey] = isLoading;
      } else {
        this.loading = isLoading;
      }
    }
  }

  _isLoading(nosync, syncKey) {
    return !nosync && (this.loading || this.keyLoading[syncKey]);
  }

}
