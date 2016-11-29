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

  login:  {
      username?: string
    , birthdate?: Date
    , remember?:boolean
    , profile?: { firstname?: string, lastname?: string}
  } = {
    remember: false
  };

  submitted = false;
  status: string = "LOADING";

  constructor(
        public navCtrl: NavController
      , public userData: UserData
      , public modalCtrl: ModalController
      , public params:NavParams
  ) {
    this.login = params.data || {remember: false} ;
    this.status = "LOADING";
  }


  ionViewDidLoad() {
    //lire les cookies
    this.status = "LOADING";
    this.userData.profileUser().subscribe(
      profile => {
        if(profile.firstname == null && profile.lastname == null){
          this.login.profile = null;
        }else{
          this.login.profile = profile;
        }
        this.status = "LOADED";
      }
    );
  }


  onLoginProfile(){
    this.navCtrl.setRoot(PinpadPage,this.login);
  }

  removeProfile(){
    this.userData.invalidateProfile();
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
