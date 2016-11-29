import {Component} from "@angular/core";
import {NavController, NavParams, ToastController} from "ionic-angular";
import {PinpadProvider} from "../../providers/pinpad-provider";
import {LoginPage} from "../login/login";
import {UserData} from "../../providers/user-data";

/*
  Generated class for the Pinpad page.

  See http://ionicframework.com/docs/v2/components/#navigation for more info on
  Ionic pages and navigation.
*/
@Component({
  selector: 'page-pinpad',
  templateUrl: 'pinpad.html'
})
export class PinpadPage {

  login:any = {};
  userPinpad: {pinpadId?: string, imgUrl?: Date} = {};
  count:number = 0;
  status:string = 'LOADING';

  constructor(
      public navCtrl: NavController
    , public pinpad : PinpadProvider
    , public params:NavParams
    , public user : UserData
    , public toastCtrl: ToastController
  ) {
    this.login = params.data;
    this.count = 0;
    this.login.password = "";
  }

  ionViewDidLoad() {
    this.load();
  }

  load(){
    this.pinpad.getPinpad().subscribe(
      res => {
        this.status = 'LOADED';
        this.userPinpad = res;
      },
      err=>{
        this.status = 'ERROR';

        let toast = this.toastCtrl.create({
          message: 'Erreur lors du chargement',
          position: 'top',
          showCloseButton : true,
          closeButtonText : 'OK',
          cssClass : 'toast-warning'
        });

        toast.present();

      }
    );
  }

  goBack(){
    this.navCtrl.setRoot(LoginPage,this.login);
  }

  activeState(num:number):string{
    if(this.count > num) return "active";
    return "";
  }

  padClick(num:number){
    this.count++;
    this.login.password+=num.toString();
    if(this.count==6){
      this.user.login(this.login,this.userPinpad.pinpadId)    .subscribe(
        data => this.user.loginSuccess(data),
        err => {
          console.log(err);
          this.errorLogin(err.json().message);
          if(this.login.profile == null){
            this.navCtrl.setRoot(LoginPage,this.login);
          }
        }
      );
    }
  }

  errorLogin(message){
    let toast = this.toastCtrl.create({
      message: message,
      position: 'top',
      showCloseButton : true,
      closeButtonText : 'OK',
      cssClass : 'toast-warning'
    });

    toast.present();
    this.padReset();
    this.load();

  }

  padReset(){
    this.count=0;
    this.login.password="";
  }

}
