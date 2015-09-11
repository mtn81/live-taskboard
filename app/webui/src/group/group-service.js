import {inject} from 'aurelia-framework';
import {HttpClient} from 'aurelia-http-client';
import {EventAggregator} from 'aurelia-event-aggregator';
import {GlobalError} from '../global-error';
import {AuthContext} from 'auth/auth-context';
import {Stomp} from 'stomp-websocket';


var _registeringGroups = [];
var _memberGroups = [];
var _removeRegisteringGroup = function(groups) {
  $.each(groups, (i, group) => {
    $.each(_registeringGroups, (i, registeringGroup) => {
      if(group.groupId == registeringGroup.groupId) {
        _registeringGroups.splice(i, 1);
      }
    });
  });
};

@inject(HttpClient, EventAggregator, AuthContext)
export class GroupService {

  constructor(http, eventAggregator, authContext) {
    this.http = http;
    this.eventAggregator = eventAggregator;
    this.authContext = authContext;
  }

  register(group){
    this.http
      .post("/api/task-manage/members/" + this.memberId() + "/groups/", group)
      .then(response => {
        let newGroup = response.content.data;
        this.eventAggregator.publish(new GroupRegistered());
        _registeringGroups.push(newGroup);

        let websocket = new WebSocket('ws://localhost:28080/task-manage/websocket/notify');
        let stompClient = window.Stomp.over(websocket);
        stompClient.connect({}, frame => {
          stompClient.subscribe('/user/queue/group_available', response => {
            this.groups();
          });
          stompClient.send('/api/groups/' + newGroup.groupId + '/watch_available');
        });
      })
      .catch(response => {
        this.eventAggregator.publish(new GlobalError(response.content.errors));
      });
  }

  groups(promiseHolder) {

    let promise = this.http
      .get("/api/task-manage/members/" + this.memberId() + '/groups/?type=belonging')
      .then(response => {
        let foundGroups = response.content.data.groups;
        _memberGroups.length = 0;
        $.merge(_memberGroups, foundGroups);
        _removeRegisteringGroup(foundGroups);
      })
      .catch(response => {
        this.eventAggregator.publish(new GlobalError(response.content.errors));
      });

    if(promiseHolder) {
      promiseHolder.promise = promise;
    }

    return _memberGroups;
  }

  registeringGroups(){
    return _registeringGroups;
  }
  
  remove(group){
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
