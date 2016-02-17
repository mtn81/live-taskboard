import _ from 'underscore';
import {inject} from 'aurelia-framework';
import {HttpClient} from 'aurelia-http-client';
import {EventAggregator} from 'aurelia-event-aggregator';
import {GlobalError} from '../global-error';
import {AuthContext} from 'auth/auth-context';
import {Stomp} from 'stomp-websocket';
import {HttpClientWrapper, CachedHttpLoader, StompClient} from '../lib/http-client-wrapper';
import {tmApi, tmWebSocket} from '../lib/env-resolver';

@inject(HttpClient, EventAggregator, AuthContext)
export class GroupService {

  constructor(http, eventAggregator, authContext) {
    this.http = new HttpClientWrapper(http, eventAggregator).withAuth(authContext);
    this.httpLoader = new CachedHttpLoader(http, eventAggregator).withAuth(authContext);
    this.stomp = new StompClient(tmWebSocket('/notify'), authContext);
    this.eventAggregator = eventAggregator;
    this.authContext = authContext;
  }

  register(group){

    this.http.call(http => {
      return http
        .post(tmApi(`/members/${this.memberId()}/groups/`), group)
        .then(response => {
          this.eventAggregator.publish(new GroupRegistered());
        })
    }, true);
  }

  validate(group){
    this.http.call(http => {
      return http
        .post(tmApi(`/members/${this.memberId()}/groups/?validate`), group)
        .then(response => {
          this.eventAggregator.publish(new GroupValidationSuccess());
        })
        .catch(response => {
          this.eventAggregator.publish(new GroupValidationError(new GlobalError(response.content.errors)));
        });
    }, true);
  }

  modify(group){

    this.http.call(http => {
      return http
        .put(tmApi(`/members/${this.memberId()}/groups/${group.groupId}`), group)
        .then(response => {
          this.eventAggregator.publish(new GroupModified());
        })
    }, true);
  }

  watchGroupAvailable(callback){
    this.stomp.subscribe(`/topic/${this.memberId()}/group_available`, groupNotify => {
      callback(groupNotify);
    });
  }

  groups() {
    return this.httpLoader.list(
        tmApi(`/members/${this.memberId()}/groups/`),
        response => {
          return response.content.data.groups;
        });
  }

  group(groupId, callback) {
    return this.httpLoader.object(
        tmApi(`/members/${this.memberId()}/groups/${groupId}`),
        response => {
          this.eventAggregator.publish(new GroupLoaded());
          let group = response.content.data;
          if(callback) callback(group);
          return group;
        });
  }

  remove(group){
    this.http.call(http => {
      group.removing = true;
      return http
        .delete(tmApi(`/members/${this.memberId()}/groups/${group.groupId}`))
        .then(response => {
          this.eventAggregator.publish(new GroupRemoved());
        });
    }, true);
  }

  memberId() {
    return this.authContext.getUserId();
  }
}

export class GroupLoaded {}
export class GroupRegistered {}
export class GroupModified {}
export class GroupRemoved {}
export class GroupJoinApplied {}
export class GroupJoinCancelled {}
export class GroupValidationSuccess {}
export class GroupValidationError {
  constructor(error){
    this.error = error;
  }
}
