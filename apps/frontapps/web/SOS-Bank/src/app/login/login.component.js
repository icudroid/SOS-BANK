"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var core_1 = require('@angular/core');
var LoginComponent = (function () {
    function LoginComponent(pinpad, authService, router) {
        this.pinpad = pinpad;
        this.authService = authService;
        this.router = router;
        this.loginModel = {
            password: '',
            username: '',
            remember: false
        };
        this.count = 0;
        this.userPinpad = null;
    }
    Object.defineProperty(LoginComponent.prototype, "diagnostic", {
        get: function () {
            return JSON.stringify(this.loginModel);
        },
        enumerable: true,
        configurable: true
    });
    LoginComponent.prototype.ngOnInit = function () {
        this.load();
    };
    LoginComponent.prototype.login = function () {
        var _this = this;
        this.authService.login(this.loginModel, this.userPinpad.pinpadId).subscribe(function () {
            if (_this.authService.isLoggedIn) {
                // Get the redirect URL from our auth service
                // If no redirect has been set, use the default
                var redirect = _this.authService.redirectUrl ? _this.authService.redirectUrl : '/admin';
                // Set our navigation extras object
                // that passes on our global query params and fragment
                var navigationExtras = {
                    preserveQueryParams: true,
                    preserveFragment: true
                };
                // Redirect the user
                _this.router.navigate([redirect], navigationExtras);
            }
        });
    };
    LoginComponent.prototype.logout = function () {
        this.authService.logout();
    };
    LoginComponent.prototype.load = function () {
        var _this = this;
        this.pinpad.getPinpad().subscribe(function (res) {
            _this.userPinpad = res;
        }, function (err) {
            console.log("err");
        });
    };
    LoginComponent.prototype.activeState = function (num) {
        if (this.count > num)
            return "fa fa-circle";
        return "fa fa-circle-o";
    };
    LoginComponent.prototype.padClick = function (num) {
        this.count++;
        this.loginModel.password += num.toString();
        if (this.count == 6) {
        }
    };
    LoginComponent.prototype.padReset = function () {
        this.count = 0;
        this.loginModel.password = "";
    };
    LoginComponent = __decorate([
        core_1.Component({
            templateUrl: './login.component.html',
            styleUrls: ['./login.component.scss']
        })
    ], LoginComponent);
    return LoginComponent;
}());
exports.LoginComponent = LoginComponent;
