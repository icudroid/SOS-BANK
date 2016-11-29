import {Injectable} from "@angular/core";
import {Http, Response} from "@angular/http";
import "rxjs/add/operator/map";
import {Observable} from "rxjs";
import {environment} from "../app/environment";
import {Events} from "ionic-angular";


/*
  Generated class for the PinpadProvider provider.

  See https://angular.io/docs/ts/latest/guide/dependency-injection.html
  for more info on providers and Angular 2 DI.
*/
@Injectable()
export class PinpadProvider {

  private pinpadUrl:string = environment.restBase+"pinpad";

  constructor(public http: Http
    , public events: Events
  ) {
    console.log('Hello PinpadProvider Provider');
  }

  getPinpad (): Observable<any> {
    return this.http.get(this.pinpadUrl)
      .map(res => res.json())
      .catch(this.handleError);
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
