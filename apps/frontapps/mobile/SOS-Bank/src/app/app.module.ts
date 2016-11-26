import {NgModule} from "@angular/core";
import {IonicApp, IonicModule} from "ionic-angular";
import {MyApp} from "./app.component";
import {AboutPage} from "../pages/about/about";
import {ContactPage} from "../pages/contact/contact";
import {HomePage} from "../pages/home/home";
import {TabsPage} from "../pages/tabs/tabs";
import {LoginPage} from "../pages/login/login";
import {UserData} from "../providers/user-data";
import {ForgotPasswordPage} from "../pages/forgot-password/forgot-password";
import {PinpadPage} from "../pages/pinpad/pinpad";
import {PinpadProvider} from "../providers/pinpad-provider";
import {Storage} from "@ionic/storage";
@NgModule({
  declarations: [
    MyApp,
    AboutPage,
    ContactPage,
    HomePage,
    TabsPage,
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
    AboutPage,
    ContactPage,
    HomePage,
    TabsPage,
    LoginPage,
    ForgotPasswordPage,
    PinpadPage
  ],
  providers: [UserData,PinpadProvider,Storage]
})
export class AppModule {}
