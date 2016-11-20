import { Component } from '@angular/core';
import {ViewController} from 'ionic-angular';

/*
  Generated class for the ForgotPassword page.

  See http://ionicframework.com/docs/v2/components/#navigation for more info on
  Ionic pages and navigation.
*/
@Component({
  selector: 'page-forgot-password',
  templateUrl: 'forgot-password.html'
})
export class ForgotPasswordPage {

  constructor(
              public viewCtrl: ViewController
  ) {}

  ionViewDidLoad() {
    console.log('Hello ForgotPasswordPage Page');
  }

  dismiss(){
    this.viewCtrl.dismiss();
  }

}
