import { Component } from '@angular/core';
import {NavController, ModalController, NavParams} from 'ionic-angular';
import {UserData} from "../../providers/user-data";
import {ForgotPasswordPage} from "../forgot-password/forgot-password";
import {PinpadPage} from "../pinpad/pinpad";

/*
  Generated class for the Login page.

  See http://ionicframework.com/docs/v2/components/#navigation for more info on
  Ionic pages and navigation.
*/
@Component({
  selector: 'page-login',
  templateUrl: 'login.html'
})
export class LoginPage {

  login: {username?: string, birthdate?: Date, remember?:boolean} = {
    remember: false
  };
  submitted = false;

  constructor(
        public navCtrl: NavController
      , public userData: UserData
      , public modalCtrl: ModalController
      , public params:NavParams
  ) {
    console.log(params.data);
    this.login = params.data || {remember: false} ;
  }


  ionViewDidLoad() {
    console.log('Hello LoginPage Page');
  }


  onLogin(form) {
    this.submitted = true;

    if (form.valid) {

      //this.userData.login(this.login.username);
      this.navCtrl.setRoot(PinpadPage,this.login);
    }
  }

  onSignup() {
    //this.navCtrl.push(SignupPage);
  }


  presentForgotPassword() {
    let modal = this.modalCtrl.create(ForgotPasswordPage);
    modal.present();
  }
}
