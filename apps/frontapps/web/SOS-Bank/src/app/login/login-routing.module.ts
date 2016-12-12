import { NgModule }             from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent }       from './login.component';
import {AuthGuard} from "../shared/auth-guard.service";
import {AuthService} from "../shared/auth.service";
import {PinpadService} from "./shared/pinpad.service";
import {CookieService} from 'angular2-cookie/core';

const loginRoutes: Routes = [
  { path: 'login', component: LoginComponent }
];

@NgModule({
  imports: [
    RouterModule.forChild(loginRoutes)
  ],
  exports: [
    RouterModule
  ],
  providers: [
    CookieService,
    AuthGuard,
    AuthService,
    PinpadService
  ]
})
export class LoginRoutingModule {}


/*
Copyright 2016 Google Inc. All Rights Reserved.
Use of this source code is governed by an MIT-style license that
can be found in the LICENSE file at http://angular.io/license
*/
