import _ from 'underscore';
import {EventAggregator} from 'aurelia-event-aggregator';
import {GlobalError} from './global-error';

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

    if(!!this.authContext && this.authContext.isAuthenticated()) {
      this.http.configure(builder => {
        builder.withHeader('X-AuthAccess-AuthId', this.authContext.getAuthId());
        builder.withHeader('X-ClientId', this.authContext.getClientId());
      });
    }

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
        if (response.content && response.content.errors) {
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

  object(url, responseResolver, requireAuth = true) {
    if (requireAuth && !!this.authContext && !this.authContext.isAuthenticated()) return {};
    if (this.cache[url]) return this.cachedValue[url];

    if(!!this.authContext) {
      this.http.configure(builder => {
        builder.withHeader('X-AuthAccess-AuthId', this.authContext.getAuthId());
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
        if (response.content && response.content.errors) {
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
        builder.withHeader('X-AuthAccess-AuthId', this.authContext.getAuthId());
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
        if (response.content && response.content.errors) {
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

export class StompClient {

  stompClient = null;
  subsriptions = {};
  connecting = true;
  subscriptionPromises = [];

  constructor(url, authContext) {
    if(!authContext.isAuthenticated()) return;

    this.stompClient = window.Stomp.over(new WebSocket(url));

    this.connecting = true;
    this.stompClient.connect({}, () => {
      this.connecting = false;
      _.each(this.subscriptionPromises, s => s());
    });
    this.authContext = authContext;
  }

  subscribe(key, callback) {

    if(!this.stompClient) return;

    _.each(this.subsriptions, (value, aKey) => {
      if (key !== aKey) {
        value.unsubscribe();
      }
    });

    if(this.subsriptions[key]) return;

    let me = this;
    let subscription = function() {
      me.subsriptions[key] = me.stompClient.subscribe(key, response => {
        let result = JSON.parse(response.body);
        if (callback) {
          callback(result);
        }
      });
    }

    if (this.connecting) {
      this.subscriptionPromises.push(subscription);
    } else {
      subscription();
    }

  }
}
