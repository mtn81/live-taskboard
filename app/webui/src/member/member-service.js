import {inject} from 'aurelia-framework';
import {HttpClient} from 'aurelia-http-client';
import {EventAggregator} from 'aurelia-event-aggregator';
import {GlobalError} from '../global-error';

@inject(HttpClient, EventAggregator)
export class MemberService {
  _members = [];

  constructor(http, eventAggregator) {
    this.http = http;
    this.eventAggregator = eventAggregator;
  }

  loadByGroup(groupId) {
    let promise = this.http
      .get("/api/task-manage/groups/" + groupId + '/members/')
      .then(response => {
        let foundMembers = response.content.data.members;
        this._members.length = 0;
        $.merge(this._members, foundMembers);
      })
      .catch(response => {
        this.eventAggregator.publish(new GlobalError(response.content.errors));
      });

    return this._members;
  }
}
