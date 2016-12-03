"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var core_1 = require('@angular/core');
require('rxjs/add/observable/of');
require('rxjs/add/operator/do');
require('rxjs/add/operator/delay');
var http_1 = require("@angular/http");
var environment_1 = require("../../../../../mobile/SOS-Bank/src/environment/environment");
var AuthService = (function () {
    function AuthService(http) {
        this.http = http;
        this.loginUrl = environment_1.environment.restBase + "login";
        this.logoutUrl = environment_1.environment.restBase + "logout";
        this.profileUrl = environment_1.environment.restBase + "profile";
        this.invalidateProfileUrl = environment_1.environment.restBase + "profile/invalidate";
        this.isLoggedIn = false;
    }
    AuthService.prototype.login = function (userPassword, pindpadId) {
        var _this = this;
        var username = userPassword.username;
        var password = userPassword.password;
        var birthdate = userPassword.birthdate;
        var rememberProfile = userPassword.remember;
        var creds = "username=" + username + "&password=" + password + "&birthdate=" + birthdate + "&pinpad=" + pindpadId
            + "&remember-profile=" + rememberProfile;
        var headers = new http_1.Headers();
        headers.append('Content-Type', 'application/x-www-form-urlencoded');
        return this.http.post(this.loginUrl, creds, {
            headers: headers
        }).do(function (data) {
            _this.tokenId = data.headers.get('x-auth-token');
            _this.isLoggedIn = true;
        });
    };
    AuthService.prototype.logout = function () {
        var headers = new http_1.Headers({ 'x-auth-token': this.tokenId });
        var options = new http_1.RequestOptions({ headers: headers });
        this.tokenId = null;
        this.isLoggedIn = false;
        this.http.post(this.logoutUrl, {}, options)
            .subscribe(function () { return console.log('Logout'); });
    };
    AuthService.prototype.getToken = function () {
        return this.tokenId;
    };
    AuthService = __decorate([
        core_1.Injectable()
    ], AuthService);
    return AuthService;
}());
exports.AuthService = AuthService;
