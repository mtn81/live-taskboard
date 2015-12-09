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
      builder.withHeader('X-ClientId', this.authContext.getClientId());
    });

    this._setLoading(nosync, syncKey, true);

    return httpCall(this.http, this)
      .then(response => {
        this._setLoading(nosync, syncKey, false);
        return true;
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
        return false;
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

export class CachedHttpLoader {
  cache = {};
  cachedValue = {};

  constructor(http, eventAggregator) {
    this.http = http;
    this.eventAggregator = eventAggregator;
  }

  withAuth(authContext) {
    this.authContext = authContext;
    return this;
  }

  object(url, responseResolver) {
    if(!!this.authContext && !this.authContext.isAuthenticated()) return {};
    if (this.cache[url]) return this.cachedValue[url];

    if(!!this.authContext) {
      this.http.configure(builder => {
        builder.withHeader('X-AuthAccess-AuthId', this.authContext.getAuth().id);
        builder.withHeader('X-ClientId', this.authContext.getClientId());
      });
    }

    this.cachedValue[url] = {};
    this.cache[url] = true;

    this.http.get(url)
      .then(response => {
        let result = responseResolver(response) || {};
        _.extend(this.cachedValue[url], result);
        this.cache[url] = false;
      })
      .catch(response => {
        this.cache[url] = false;

        let errors;
        if (response.content.errors) {
          errors = response.content.errors;
        } else if (_.isString(response.content) && response.content.includes('ECONNREFUSED')) {
          errors = [{ message: 'サーバに接続できませんでした。'}];
        } else {
          errors = [{ message: 'エラーが発生しました。'}];
        }
        this.eventAggregator.publish(new GlobalError(errors));
        return false;
      });

    return this.cachedValue[url];
  }

  list(url, responseResolver) {
    if(!!this.authContext && !this.authContext.isAuthenticated()) return [];
    if (this.cache[url]) return this.cachedValue[url];

    if(!!this.authContext) {
      this.http.configure(builder => {
        builder.withHeader('X-AuthAccess-AuthId', this.authContext.getAuth().id);
        builder.withHeader('X-ClientId', this.authContext.getClientId());
      });
    }

    this.cachedValue[url] = [];
    this.cache[url] = true;

    this.http.get(url)
      .then(response => {
        let results = responseResolver(response) || [];
        this.cachedValue[url].splice(0, this.cachedValue[url].length, ...results);
        this.cache[url] = false;
      })
      .catch(response => {
        this.cache[url] = false;

        let errors;
        if (response.content.errors) {
          errors = response.content.errors;
        } else if (_.isString(response.content) && response.content.includes('ECONNREFUSED')) {
          errors = [{ message: 'サーバに接続できませんでした。'}];
        } else {
          errors = [{ message: 'エラーが発生しました。'}];
        }
        this.eventAggregator.publish(new GlobalError(errors));
        return false;
      });

    return this.cachedValue[url];
  }

}
