import { NgModule }       from '@angular/core';
import { BrowserModule }  from '@angular/platform-browser';
import { FormsModule }    from '@angular/forms';

import { AppComponent }         from './app.component';
import { AppRoutingModule }     from './app-routing.module';

import { HeroesModule }         from './heroes/heroes.module';
import { LoginRoutingModule }   from './login/login-routing.module';
import { LoginComponent }       from './login/login.component';

import { DialogService }        from './dialog.service';
import {HttpModule, JsonpModule} from "@angular/http";

import { CookieService } from 'angular2-cookie/services/cookies.service';
import { JqueryBackstretchComponent } from './shared/component/jquery-backstretch/jquery-backstretch.component';

@NgModule({
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    JsonpModule,
    HeroesModule,
    LoginRoutingModule,
    AppRoutingModule
  ],
  declarations: [
    AppComponent,
    LoginComponent,
    JqueryBackstretchComponent
  ],
  providers: [
    CookieService,
    DialogService
  ],
  bootstrap: [ AppComponent ]
})
export class AppModule {
}


/*
Copyright 2016 Google Inc. All Rights Reserved.
Use of this source code is governed by an MIT-style license that
can be found in the LICENSE file at http://angular.io/license
*/
