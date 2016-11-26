import {Injectable} from "@angular/core";
import {Http} from "@angular/http";
import "rxjs/add/operator/map";
import {Observable} from "rxjs";
import {environment} from "../app/environment";


/*
  Generated class for the PinpadProvider provider.

  See https://angular.io/docs/ts/latest/guide/dependency-injection.html
  for more info on providers and Angular 2 DI.
*/
@Injectable()
export class PinpadProvider {

  private pinpadUrl:string = environment.restBase+"pinpad";

  constructor(public http: Http) {
    console.log('Hello PinpadProvider Provider');
  }

  getPinpad (): Observable<any> {
    return this.http.get(this.pinpadUrl)
      .map(res => res.json());
  }


}
