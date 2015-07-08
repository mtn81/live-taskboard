import {inject} from 'aurelia-framework';
import {HttpClient} from 'aurelia-http-client';
import {EventAggregator} from 'aurelia-event-aggregator';
import {GlobalError} from '../global-error';
import {AuthContext} from 'auth/auth-context';

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
        this.eventAggregator.publish(new GroupRegistered());
      })
      .catch(response => {
        this.eventAggregator.publish(new GlobalError(response.content.errors));
      });
  }

  groups(){
    var groups = [];

    this.http
      .get("/api/task-manage/members/" + this.memberId() + '/groups/?type=belonging')
      .then(response => {
        jQuery.merge(groups, response.content.data.groups);
      })
      .catch(response => {
        this.eventAggregator.publish(new GlobalError(response.content.errors));
      });

    return groups;
  }

  memberId() {
    return this.authContext.getAuth().userId;
  }
}

export class GroupRegistered {}
