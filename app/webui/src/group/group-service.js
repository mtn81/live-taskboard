import {inject} from 'aurelia-framework';
import {HttpClient} from 'aurelia-http-client';
import {EventAggregator} from 'aurelia-event-aggregator';
import {GlobalError} from '../global-error';

@inject(HttpClient, EventAggregator)
export class GroupService {

  constructor(http, eventAggregator) {
    this.http = http;
    this.eventAggregator = eventAggregator;
  }

  register(group){
    this.http
      .post("/api/task-manage/groups/", group)
      .then(response => {
        this.eventAggregator.publish(new GroupRegistered());
      })
      .catch(response => {
        this.eventAggregator.publish(new GlobalError(response.content.errors));
      });
  }

  groups(memberId){
    var groups = [];

    this.http
      .get("/api/task-manage/member/" + memberId + '/groups/')
      .then(response => {
        jQuery.merge(groups, response.content.data.groups);
      })
      .catch(response => {
        this.eventAggregator.publish(new GlobalError(response.content.errors));
      });

    return groups;
  }
}

export class GroupRegistered {}
