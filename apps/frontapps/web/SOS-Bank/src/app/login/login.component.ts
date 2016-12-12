import {Component, OnInit}        from '@angular/core';
import {
  Router,
  NavigationExtras
} from '@angular/router';
import {AuthService} from "../shared/auth.service";
import {PinpadService} from "./shared/pinpad.service";

@Component({
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit{
  message: string;

  loginModel: {
    username?: string
    , birthdate?: Date
    , remember?: boolean
    , password?: string
    , profile?: { firstname?: string, lastname?: string}
  } = {
    password: '',
    username: '',
    remember: false
  };

  count: number = 0;

  userPinpad: {pinpadId?: string, imgUrl?: Date} = null;

  backstretchImgs:Array<string> = [
    "../assets/pages/img/login/bg1.jpg",
    "../assets/pages/img/login/bg2.jpg",
    "../assets/pages/img/login/bg3.jpg"
  ];

  backstretchConfig = {
    fade: 1000,
    duration: 8000
  };


  get diagnostic() {
    return JSON.stringify(this.loginModel);
  }


  constructor(public pinpad: PinpadService, public authService: AuthService, public router: Router) {
  }


  ngOnInit(): void {
    this.load();
  }


  login() {
    this.authService.login(this.loginModel,this.userPinpad.pinpadId).subscribe(() => {
      if (this.authService.isLoggedIn) {
        // Get the redirect URL from our auth service
        // If no redirect has been set, use the default
        let redirect = this.authService.redirectUrl ? this.authService.redirectUrl : '/connected';

        // Set our navigation extras object
        // that passes on our global query params and fragment
        let navigationExtras: NavigationExtras = {
          preserveQueryParams: true,
          preserveFragment: true
        };

        // Redirect the user
        this.router.navigate([redirect], navigationExtras);
      }
    });
  }

  logout() {
    this.authService.logout();
  }


  load() {
    this.pinpad.getPinpad().subscribe(
      res => {
        this.userPinpad = res;
      },
      err => {
        console.log("err");
      }
    );
  }

  activeState(num: number): string {
    if (this.count > num) return "fa fa-circle";
    return "fa fa-circle-o";
  }

  padClick(num: number) {
    this.count++;
    this.loginModel.password += num.toString();
    if (this.count == 6) {
      /*      this.user.login(this.loginModel,this.userPinpad.pinpadId)    .subscribe(
       data => this.user.loginSuccess(data),
       err => {
       console.log(err);
       this.errorLogin(err.json().message);
       if(this.loginModel.profile == null){
       this.navCtrl.setRoot(LoginPage,this.loginModel);
       }
       }
       );*/
    }
  }

  padReset(){
    this.count=0;
    this.loginModel.password="";
  }

}
