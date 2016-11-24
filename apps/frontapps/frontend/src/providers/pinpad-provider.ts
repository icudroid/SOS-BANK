import {Injectable} from "@angular/core";
import {Http} from "@angular/http";
import "rxjs/add/operator/map";
import {Observable} from "rxjs";

/*
  Generated class for the PinpadProvider provider.

  See https://angular.io/docs/ts/latest/guide/dependency-injection.html
  for more info on providers and Angular 2 DI.
*/
@Injectable()
export class PinpadProvider {

  private pinpadUrl:string ="http://localhost:8080/pinpad";

  constructor(public http: Http) {
    console.log('Hello PinpadProvider Provider');
  }

  getPinpad (): Observable<any> {
    return this.http.get(this.pinpadUrl)
      .map(res => res.json());
  }


}
