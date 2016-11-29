import {Component, ViewChild} from '@angular/core';
import {Platform, Nav, Events, MenuController, ToastController} from 'ionic-angular';
import { StatusBar, Splashscreen } from 'ionic-native';
import {LoginPage} from "../pages/login/login";
import {UserData} from "../providers/user-data";
import {HomePage} from "../pages/home/home";


export interface PageInterface {
  title: string;
  component: any;
  icon: string;
  logsOut?: boolean;
  index?: number;
}

@Component({
  templateUrl: 'app.template.html'
})
export class MyApp {
  rootPage = LoginPage;

  @ViewChild(Nav) nav: Nav;

  // List of pages that can be navigated to from the left menu
  // the left menu only works after login
  // the login page disables the left menu
  appPages: PageInterface[] = [
/*
    { title: 'Schedule', component: TabsPage, icon: 'calendar' },
    { title: 'Speakers', component: TabsPage, index: 1, icon: 'contacts' },
    { title: 'Map', component: TabsPage, index: 2, icon: 'map' },
    { title: 'About', component: TabsPage, index: 3, icon: 'information-circle' },
*/
  ];
  loggedInPages: PageInterface[] = [
    { title: 'Home', component: HomePage, icon: 'person' },
    { title: 'Logout', component: LoginPage, icon: 'log-out', logsOut: true }
  ];
  loggedOutPages: PageInterface[] = [
    /*{ title: 'Login', component: LoginPage, icon: 'log-in' },*/
    /*{ title: 'Signup', component: SignupPage, icon: 'person-add' }*/
  ];

  activeMenu: any = 'Login';


  constructor(
          platform: Platform
        , public events: Events
        , public userData: UserData
        , public menu: MenuController
        , public toastCtrl: ToastController
  ) {

    platform.ready().then(() => {
      // Okay, so the platform is ready and our plugins are available.
      // Here you can do any higher level native things you might need.
      StatusBar.styleDefault();
      Splashscreen.hide();
    });

    // decide which menu items should be hidden by current login status stored in local storage
    this.userData.hasLoggedIn().then((hasLoggedIn) => {
      this.enableMenu(hasLoggedIn === true);
    });

    this.listenToLoginEvents();

  }



  isActiveMenu(page: PageInterface):boolean {
    return page.title == this.activeMenu;
  }


  openPage(page: PageInterface) {
    this.activeMenu = page.title;

    // the nav component was found using @ViewChild(Nav)
    // reset the nav to remove previous pages and only have this page
    // we wouldn't want the back button to show in this scenario
    if (page.index) {
      this.nav.setRoot(page.component, {tabIndex: page.index});

    } else {
      this.nav.setRoot(page.component);
    }

    if (page.logsOut === true) {
      // Give the menu time to close before changing to logged out
      setTimeout(() => {
        this.userData.logout();
      }, 1000);
    }
  }

  listenToLoginEvents() {
    this.events.subscribe('user:login', () => {
      this.enableMenu(true);
      this.openPage(this.loggedInPages[0]);
    });

    this.events.subscribe('user:login-error', () => {
      this.enableMenu(true);
    });

    this.events.subscribe('user:logout', () => {
      this.enableMenu(false);
    });

    this.events.subscribe('user:profile-invalidate', () => {
      this.nav.setRoot(LoginPage);
    });


  }

  enableMenu(loggedIn) {
    this.menu.enable(loggedIn, 'loggedInMenu');
  }

  menuChange() {
    this.events.subscribe('tab:change', (title) => {
      this.activeMenu = title;
    });
  }

}
