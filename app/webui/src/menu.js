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

  selectGroupMenu(){
    this.closeAllMenu();
    this.router.navigate('taskboard');
    this.toggleMenu(this.groupMenuContent);
  }
  selectJoinMenu(){
    this.closeAllMenu();
    this.router.navigate('join');
  }
  showGroupRegister(){
    $(this.groupRegisterModal).modal('show');
  }

  removeGroup(group){
    this.groupService.remove(group);
  }

  selectGroup(group){
    this.group = group;
    this.eventAggregator.publish('group.selected', this.group);
    this.closeAllMenu();
  }

  fire(eventId, hideTarget){
    this.eventAggregator.subscribe(eventId + '.success', payload => {
      $(hideTarget).modal('hide');
      this.closeAllMenu();
    });
    this.eventAggregator.publish(eventId);
  }

  attached() {
    if(this.authContext.isAuthenticated()){
      this.groups = this.groupService.groups();
      this.groupService.watchGroupAvailable(e => {
        this.eventAggregator.publish(new GlobalInfo([
          { message: `グループ(${e.groupName})が利用可能になりました。` }
        ]));
      });
    }
  }

  closeAllMenu() {
    $('.menu-content').collapse('hide');
  }
  toggleMenu(menuContent, collapse){
    if (collapse == null) {
      $(menuContent).collapse('toggle')
    } else {
      $(menuContent).collapse(collapse);
    }
  }

}
