import {customElement, inject, bindable} from 'aurelia-framework';
import {Router} from 'aurelia-router';
import {AuthContext} from './auth/auth-context';
import {EventAggregator} from 'aurelia-event-aggregator';
import {GroupService} from './group/group-service';
import {GlobalInfo} from './global-info';
import 'components/jqueryui';

@customElement('menu')
@inject(Router, AuthContext, EventAggregator, GroupService)
export class Menu {

  groups = [];

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
    this.toggleMenu(this.groupMenuContent, 'hide');
  }

  fire(eventId, hideTarget){
    this.eventAggregator.subscribe(eventId + '.success', payload => {
      $(hideTarget).modal('hide');
      this.toggleMenu(this.groupMenuContent, 'hide');
    });
    this.eventAggregator.publish(eventId);
  }

  bind() {
    this.eventAggregator.subscribe('select.default.group', e => {
      this.selectGroup();
    });

    this.eventAggregator.subscribe('init.menu', e => {
      this._initGroup();
    });

    if(this.authContext.isAuthenticated()){
      this._initGroup();
    }
  }

  _initGroup() {
    this.groups = this.groupService.groups();
    this.groupService.watchGroupAvailable(e => {
      this.eventAggregator.publish(new GlobalInfo([
        { message: `グループ(${e.groupName})が利用可能になりました。` }
      ]));
    });
  }

  toggleMenu(menuContent, collapse){
    if (collapse == null) {
      $(menuContent).collapse('toggle')
    } else {
      $(menuContent).collapse(collapse);
    }
  }

}
