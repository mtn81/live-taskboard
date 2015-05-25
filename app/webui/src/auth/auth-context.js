export class AuthContext {
  auth = {};

  store(auth){
    jQuery.extend(this.auth, auth);
  }

  isAuthenticated(){
    return !!this.auth;
  }
}
