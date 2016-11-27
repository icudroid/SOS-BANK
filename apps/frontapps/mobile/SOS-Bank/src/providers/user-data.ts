import { Injectable } from '@angular/core';
import {Http, Headers, Response} from '@angular/http';
import 'rxjs/add/operator/map';
import {Events} from "ionic-angular";
import {Observable} from "rxjs";
import { Storage } from '@ionic/storage';
import {environment} from "../app/environment";

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

    var creds = "username=" + username + "&password=" + password +"&birthdate=" + birthdate + "&pinpad=" + pindpadId;

    var headers = new Headers();
    headers.append('Content-Type', 'application/x-www-form-urlencoded');

    this.http.post(this.loginUrl, creds, {
      headers: headers
    })
    .subscribe(
      data => this.loginSuccess(data),
      err => this.logError(err,userPassword),
      () => console.log('Authentication Complete')
    );
  }

  // return a promise
  hasLoggedIn() {
    return this.storage.get(this.HAS_LOGGED_IN).then((value) => {
      return value === true;
    });
  }

  logout() {
    this.storage.remove(this.HAS_LOGGED_IN);
    this.http.post(this.logoutUrl,{})
      .subscribe(
        () => console.log('Logout')
      );
    this.events.publish('user:logout');
  }

  setAuthToken(token) {
    this.storage.set('x-auth-token', token);
  }

  getAuthToken() {
    return this.storage.get('x-auth-token').then((value) => {
      return value;
    });
  }

  private logError(err: any,userPassword:any) {
    this.storage.remove(this.HAS_LOGGED_IN);
    this.events.publish('user:login-error',userPassword);
  }

  private loginSuccess(data: Response) {
    this.setAuthToken(data.headers.get('x-auth-token'))
    this.storage.set(this.HAS_LOGGED_IN, true);
    this.events.publish('user:login');
  }
}
