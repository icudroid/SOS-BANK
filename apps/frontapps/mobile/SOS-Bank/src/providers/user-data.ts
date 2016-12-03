import { Injectable } from '@angular/core';
import {Http, Headers, Response, RequestOptions} from '@angular/http';
import 'rxjs/add/operator/map';
import {Events} from "ionic-angular";
import {Observable} from "rxjs";
import { Storage } from '@ionic/storage';
import {environment} from "../environment/environment";

/*
  Generated class for the UserData provider.

  See https://angular.io/docs/ts/latest/guide/dependency-injection.html
  for more info on providers and Angular 2 DI.
*/
@Injectable()
export class UserData {

  HAS_LOGGED_IN = 'hasLoggedIn';
  private loginUrl:string = environment.restBase + "login";
  private logoutUrl:string = environment.restBase + "logout";
  private profileUrl:string =environment.restBase + "profile";
  private invalidateProfileUrl:string =environment.restBase + "profile/invalidate";


  constructor(
              public http: Http
            , public events: Events
            , public storage: Storage
  ) {
    console.log('Hello UserData Provider');
  }

  login(userPassword, pindpadId:string){

    var username =userPassword.username;
    var password = userPassword.password;
    var birthdate = userPassword.birthdate;
    var rememberProfile = userPassword.remember;

    var creds = "username=" + username + "&password=" + password +"&birthdate=" + birthdate + "&pinpad=" + pindpadId
                            + "&remember-profile="+rememberProfile;

    var headers = new Headers();
    headers.append('Content-Type', 'application/x-www-form-urlencoded');

    return this.http.post(this.loginUrl, creds, {
      headers: headers
    });
  }

  invalidateProfile(){
    this.http.get(this.invalidateProfileUrl, {}).subscribe(
      data => {
        this.events.publish('user:profile-invalidate');
      },
      err => {}
    );

  }

  profileUser(){
    return this.http.get(this.profileUrl, {})
      .map(res=>res.json());
  }

  // return a promise
  hasLoggedIn() {
    return this.storage.get(this.HAS_LOGGED_IN).then((value) => {
      return value === true;
    });
  }

  logout() {

     this.getAuthToken().then(token => {
       let headers = new Headers({ 'x-auth-token':token});
      let options = new RequestOptions({ headers: headers });

      this.storage.remove(this.HAS_LOGGED_IN);
      this.storage.remove('x-auth-token');
      this.http.post(this.logoutUrl,{},options)
        .subscribe(
          () => console.log('Logout')
        );
      this.events.publish('user:logout');

    });
  }

  setAuthToken(token) {
    this.storage.set('x-auth-token', token);
  }

  getAuthToken() {
    return this.storage.get('x-auth-token').then((value) => {
      return value;
    });
  }

  logError(err: any,userPassword:any) {
    this.storage.remove(this.HAS_LOGGED_IN);
    this.events.publish('user:login-error',userPassword);
  }

  loginSuccess(data: Response) {
    this.setAuthToken(data.headers.get('x-auth-token'));
    this.storage.set(this.HAS_LOGGED_IN, true);
    this.events.publish('user:login');
  }




  private handleError (error: Response | any) {
    // In a real world app, we might use a remote logging infrastructure
    let errMsg: string;
    if (error instanceof Response) {
      const body = error.json() || '';
      const err = body.error || JSON.stringify(body);
      errMsg = `${error.status} - ${error.statusText || ''} ${err}`;
    } else {
      errMsg = error.message ? error.message : error.toString();
    }
    console.error(errMsg);
    return Observable.throw(errMsg);
  }
}
