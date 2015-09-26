import {customElement, inject, bindable} from 'aurelia-framework';
import {Router} from 'aurelia-router';
import {AuthContext} from './auth/auth-context';
import {EventAggregator} from 'aurelia-event-aggregator';
import {GroupService} from './group/group-service';
import 'components/jqueryui';

@customElement('menu')
@inject(Router, AuthContext, EventAggregator, GroupService)
export class Menu {

  groups = [];
  registeringGroups = [];

  constructor(router, authContext, eventAggregator, groupService){
    this.router = router;
    this.authContext = authContext;
    this.eventAggregator = eventAggregator;
    this.groupService = groupService;
  }

  showGroupRegister(){
    $(this.groupRegisterModal).modal('show');
  }

  removeGroup(group){
    this.groupService.remove(group);
  }

  selectGroup(group){
    if(group){
      this.group = group;
    } else {
      if(this.groups.length === 0) return;
      this.group = this.groups[0];
    }

    this.eventAggregator.publish('group.selected', this.group);
  }

  fire(eventId, hideTarget){
    this.eventAggregator.subscribe(eventId + '.success', payload => {
      $(hideTarget).modal('hide');
    });
    this.eventAggregator.publish(eventId);
  }

  bind() {
    this.eventAggregator.subscribe('select.default.group', e => {
      this.selectGroup();
    });

    let promiseHolder = {};
    this.groups = this.groupService.groups(promiseHolder);
    this.registeringGroups = this.groupService.registeringGroups();

    return promiseHolder.promise;
  }

  toggleMenu(menu){
    var icon = $(menu).find("span.glyphicon");
    icon.toggleClass('glyphicon-menu-left');
    icon.toggleClass('glyphicon-menu-right');
  }

}
