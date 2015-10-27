import {inject} from 'aurelia-framework';
import {HttpClient} from 'aurelia-http-client';
import {EventAggregator} from 'aurelia-event-aggregator';
import {GlobalError} from '../global-error';
import {AuthContext} from 'auth/auth-context';
import {Stomp} from 'stomp-websocket';


var _memberGroups = [];
var _watchingAvailableGroup = false;

@inject(HttpClient, EventAggregator, AuthContext)
export class GroupService {

  constructor(http, eventAggregator, authContext) {
    this.http = http;
    this.eventAggregator = eventAggregator;
    this.authContext = authContext;
  }

  register(group){
    if(!this.authContext.isAuthenticated()) return;

    this.http
      .post("/api/task-manage/members/" + this.memberId() + "/groups/", group)
      .then(response => {
        this.groups();
        this.eventAggregator.publish(new GroupRegistered(response.content.data));
      })
      .catch(response => {
        this.eventAggregator.publish(new GlobalError(response.content.errors));
      });
  }

  watchGroupAvailable(callback){
    if(!this.authContext.isAuthenticated()) return;
    if(_watchingAvailableGroup) return;

    let websocket = new WebSocket('ws://localhost:28080/task-manage/websocket/notify');
    let stompClient = window.Stomp.over(websocket);
    stompClient.connect({}, frame => {
      stompClient.subscribe('/topic/' + this.memberId() + '/group_available', response => {
        if (callback) callback(JSON.parse(response.body));
        this.groups();
      });
    });
    _watchingAvailableGroup = true;
  }

  groups(promiseHolder) {
    if(!this.authContext.isAuthenticated()) return [];

    let promise = this.http
      .get("/api/task-manage/members/" + this.memberId() + '/groups/?type=belonging')
      .then(response => {
        let foundGroups = response.content.data.groups;
        _memberGroups.length = 0;
        $.merge(_memberGroups, foundGroups);
      })
      .catch(response => {
        this.eventAggregator.publish(new GlobalError(response.content.errors));
      });

    if(promiseHolder) {
      promiseHolder.promise = promise;
    }

    return _memberGroups;
  }

  remove(group){
    if(!this.authContext.isAuthenticated()) return;

    group.removing = true;

    this.http
      .delete("/api/task-manage/members/" + this.memberId() + "/groups/" + group.groupId)
      .then(response => {
        this.groups();
      })
      .catch(response => {
        this.eventAggregator.publish(new GlobalError(response.content.errors));
      });
  }

  memberId() {
    return this.authContext.getAuth().userId;
  }
}

export class GroupRegistered {}
