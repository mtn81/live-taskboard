import {inject} from 'aurelia-framework';
import {Router} from 'aurelia-router';
import {AuthContext} from './auth/auth-context';
import {EventAggregator} from 'aurelia-event-aggregator';
import {GroupService} from './group/group-service';

@inject(Router, AuthContext, EventAggregator, GroupService)
export class Taskboard {

  groups = [];

  constructor(router, authContext, eventAggregator, groupService){
    this.router = router;
    this.authContext = authContext;
    this.eventAggregator = eventAggregator;
    this.groupService = groupService;
  }

  get auth(){
    return this.authContext.getAuth();
  }

  logout(){
    this.authContext.remove();
    this.router.navigate('login');
  }

  showGroupRegister(){
    $(this.groupRegisterModal).modal('show');
  }

  fire(eventId){
    this.eventAggregator.subscribe(eventId + '.success', payload => {
      $(this.groupRegisterModal).modal('hide');
    });
    this.eventAggregator.publish(eventId);
  }

  //life cycle methods
  activate(){
    this.groups.length = 0;
    jQuery.merge(this.groups, this.groupService.groups(this.authContext.getAuth().userId));
  }

}

