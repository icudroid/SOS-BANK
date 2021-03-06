import { Injectable } from '@angular/core';

import { Observable } from 'rxjs/Observable';
import 'rxjs/add/observable/of';
import 'rxjs/add/operator/do';
import 'rxjs/add/operator/delay';
import {Headers, Http, Response, RequestOptions} from "@angular/http";
import {CookieService} from 'angular2-cookie/core';
import {environment} from "../../../../../mobile/SOS-Bank/src/environment/environment";

@Injectable()
export class AuthService {

  private loginUrl:string = environment.restBase + "login";
  private logoutUrl:string = environment.restBase + "logout";
  private profileUrl:string =environment.restBase + "profile";
  private invalidateProfileUrl:string =environment.restBase + "profile/invalidate";

  isLoggedIn: boolean = false;
  tokenId:string;

  // store the URL so we can redirect after logging in
  redirectUrl: string;

  constructor(public http: Http,
              private cookieService:CookieService
  ) {
    this.tokenId = cookieService.get("x-auth-token");
    console.log(this.tokenId);
    this.isLoggedIn = this.tokenId !=null ;
    console.log(this.isLoggedIn);
  }

  login(userPassword,pindpadId): Observable<Response> {

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
    }).do(data => {
      this.tokenId = data.headers.get('x-auth-token');
      this.isLoggedIn = true;
      this.cookieService.put('x-auth-token',this.tokenId);
    });

  }

  logout(): void {
    this.cookieService.remove('x-auth-token');
    let headers = new Headers({ 'x-auth-token':this.tokenId});
    let options = new RequestOptions({ headers: headers });
    this.tokenId = null;
    this.isLoggedIn = false;
    this.http.post(this.logoutUrl,{},options)
      .subscribe(
        () => console.log('Logout')
      );
  }

  getToken(){
    return this.tokenId;
  }


}
