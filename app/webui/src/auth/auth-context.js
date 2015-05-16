export class AuthContext {
  auth = null;

  store(auth){
    this.auth = auth;
  }

  isAuthenticated(){
    return !!this.auth;
  }
}
