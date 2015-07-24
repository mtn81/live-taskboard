export class AuthContext {

  constructor() {
    this.storage = localStorage;
  }

  store(auth){
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

  remove(){
    this.storage.removeItem('auth');
  }
}
