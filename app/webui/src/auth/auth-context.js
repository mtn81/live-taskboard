export class AuthContext {

  constructor() {
    this.storage = localStorage;
  }

  store(auth){
    auth.clientId = `${auth.userId}_${new Date().getTime()}`;
    this.storage.setItem('auth', JSON.stringify(auth));
  }

  isAuthenticated(){
    return !!this.getAuth();
  }

  getAuth(){
    var auth = this.storage.getItem('auth');
    if (!auth) return auth;
    return JSON.parse(auth);
  }
  getUserId() {
    if(!this.isAuthenticated()) return '';
    return this.getAuth().userId;
  }
  getClientId(){
    if(!this.isAuthenticated()) return '';
    return this.getAuth().clientId;
  }
  hasClientId(clientId) {
    return this.getClientId() === clientId;
  }

  remove(){
    this.storage.removeItem('auth');
  }
}
