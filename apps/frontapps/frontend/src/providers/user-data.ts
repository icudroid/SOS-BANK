import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import 'rxjs/add/operator/map';
import {Events} from "ionic-angular";
import {Observable} from "rxjs";
import { Storage } from '@ionic/storage';

/*
  Generated class for the UserData provider.

  See https://angular.io/docs/ts/latest/guide/dependency-injection.html
  for more info on providers and Angular 2 DI.
*/
@Injectable()
export class UserData {

  HAS_LOGGED_IN = 'hasLoggedIn';
  private loginUrl:string ="http://localhost:8080/login";

  constructor(
              public http: Http
            , public events: Events
            , public storage: Storage
  ) {
    console.log('Hello UserData Provider');
  }

  login(userPassword){

    this.http.post(this.loginUrl,userPassword)
      .map(res => res.json());

    /*this.storage.set(this.HAS_LOGGED_IN, true);
    this.setUsername(username);
    this.events.publish('user:login');*/
  }


  // return a promise
  hasLoggedIn() {
    return this.storage.get(this.HAS_LOGGED_IN).then((value) => {
      return value === true;
    });
  }

  logout() {
    this.storage.remove(this.HAS_LOGGED_IN);
    this.storage.remove('username');
    this.events.publish('user:logout');
  }

  setUsername(username) {
    this.storage.set('username', username);
  }

  getUsername() {
    return this.storage.get('username').then((value) => {
      return value;
    });
  }

}
