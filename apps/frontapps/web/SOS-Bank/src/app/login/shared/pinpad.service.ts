import { Injectable } from '@angular/core';
import {Http, Response} from "@angular/http";
import { Observable }     from 'rxjs/Observable';
import '../../rxjs-operators'
import {environment} from "../../../../../../mobile/SOS-Bank/src/environment/environment";

@Injectable()
export class PinpadService {


  private pinpadUrl:string = environment.restBase+"pinpad";

  constructor(public http: Http) { }


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
