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

  loadByGroup(groupId, callback) {
    if (this._loading) return this._members;

    this._loading = true;

    let promise = this.http
      .get("/api/task-manage/groups/" + groupId + '/members/')
      .then(response => {
        let foundMembers = response.content.data.members;
        this._members.length = 0;
        $.merge(this._members, foundMembers);
        if(callback) callback(this._members);
        this._loading = false;
      })
      .catch(response => {
        this._loading = false;
        this.eventAggregator.publish(new GlobalError(response.content.errors));
      });

    return this._members;
  }
}
