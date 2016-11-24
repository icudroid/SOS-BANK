import {Component} from "@angular/core";
import {NavController, NavParams} from "ionic-angular";
import {PinpadProvider} from "../../providers/pinpad-provider";
import {LoginPage} from "../login/login";

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

  login: {username?: string, birthdate?: Date, remember?:boolean} = {};
  userPinpad: {pinpadId?: string, imgUrl?: Date} = {};
  count:number = 0;
  password : string ="";

  constructor(
      public navCtrl: NavController
    , public pinpad : PinpadProvider
    , public params:NavParams
  ) {
    this.login = params.data;
    this.count = 0;
    this.password = "";
  }

  ionViewDidLoad() {
    this.pinpad.getPinpad().subscribe(res => {
      this.userPinpad = res;
    });
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
    this.password+=num.toString();
    if(this.count==6){
      console.log("log user");
    }
  }

  padReset(){
    this.count=0;
    this.password="";
  }

}
