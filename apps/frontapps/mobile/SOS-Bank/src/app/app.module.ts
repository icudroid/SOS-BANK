import {NgModule} from "@angular/core";
import {IonicApp, IonicModule} from "ionic-angular";
import {MyApp} from "./app.component";
import {HomePage} from "../pages/home/home";
import {LoginPage} from "../pages/login/login";
import {UserData} from "../providers/user-data";
import {PinpadPage} from "../pages/pinpad/pinpad";
import {PinpadProvider} from "../providers/pinpad-provider";
import {Storage} from "@ionic/storage";
import {ForgotPasswordPage} from "../pages/forgot-password/forgot-password";


@NgModule({
  declarations: [
    MyApp,
    HomePage,
    LoginPage,
    ForgotPasswordPage,
    PinpadPage
  ],
  imports: [
    IonicModule.forRoot(MyApp)
  ],
  bootstrap: [IonicApp],
  entryComponents: [
    MyApp,
    HomePage,
    LoginPage,
    ForgotPasswordPage,
    PinpadPage
  ],
  providers: [UserData,PinpadProvider,Storage]
})
export class AppModule {}
