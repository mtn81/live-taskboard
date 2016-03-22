import 'components/jqueryui';
import {customElement, inject, bindable} from 'aurelia-framework';
import {Router} from 'aurelia-router';
import {EventAggregator} from 'aurelia-event-aggregator';
import {EventAggregatorWrapper} from '../../lib/event-aggregator-wrapper';
import {GlobalInfo} from '../../lib/global-info';
import {AuthContext} from '../../services/auth/auth-context';
import {GroupService, GroupRemoved} from '../../services/group/group-service';

@customElement('menu')
@inject(Router, AuthContext, EventAggregator, GroupService)
export class Menu {

  groups = [];
  group = null;
  groupRegisterModalAttrs = null;

  constructor(router, authContext, eventAggregator, groupService){
    this.router = router;
    this.authContext = authContext;
    this.events = new EventAggregatorWrapper(this, eventAggregator);
    this.groupService = groupService;
  }

  selectGroupMenu(){
    this.closeAllMenu();
    this.router.navigate('taskboard');

    this.groups = this.groupService.groups();
    this.toggleMenu(this.groupMenuContent);
  }
  selectMemberMenu(){
    this.closeAllMenu();
    this.router.navigate(`member/${this.group.groupId}`);
  }
  selectJoinMenu(){
    this.closeAllMenu();
    this.router.navigate('join');
  }
  selectAcceptMenu(){
    this.closeAllMenu();
    this.router.navigate('accept');
  }
  showGroupRegister(){
    this.groupRegisterModalAttrs = {
      title: 'グループ登録',
      eventLabel: '登録',
      event: 'group-register.register'
    };
    this._showGroupRegisterModal();
  }
  showGroupEdit(group){
    this.groupRegisterModalAttrs = {
      title: 'グループ編集',
      eventLabel: '変更',
      event: 'group-register.change'
    };
    this._showGroupRegisterModal(group.groupId);
  }
  _showGroupRegisterModal(groupId){
    this.events.publish('group-register.init', groupId);
    this.events.subscribe('group-register.enable', payload => {
      $(this.groupRegisterModal).find('.btn-primary').removeAttr('disabled');
    });
    this.events.subscribe('group-register.disable', payload => {
      $(this.groupRegisterModal).find('.btn-primary').attr('disabled', true);
    });
    $(this.groupRegisterModal).modal('show');
  }

  removeGroup(group){
    if (group.removing) return;
    this.groupService.remove(group);
  }

  selectGroup(group){
    if (group.removing) return;
    this.group = group;
    this.events.publish('group.selected', this.group);
    this.closeAllMenu();
  }

  fire(eventId, hideTarget){
    this.events.subscribe(eventId + '.success', payload => {
      $(hideTarget).modal('hide');
      this.closeAllMenu();
    });
    this.events.publish(eventId);
  }

  attached() {
    this.groupService.watchGroupAvailable(groupNotify => {
      this.events.publish(new GlobalInfo([
        { message: `グループ(${groupNotify.groupName})が利用可能になりました。` }
      ]));
    });
    this.events.subscribe(GroupRemoved, () => {
      this.groups = this.groupService.groups();
    });
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
