"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var core_1 = require('@angular/core');
var http_1 = require("@angular/http");
var Observable_1 = require('rxjs/Observable');
require('../../rxjs-operators');
var environment_1 = require("../../../../../../mobile/SOS-Bank/src/environment/environment");
var PinpadService = (function () {
    function PinpadService(http) {
        this.http = http;
        this.pinpadUrl = environment_1.environment.restBase + "pinpad";
    }
    PinpadService.prototype.getPinpad = function () {
        return this.http.get(this.pinpadUrl)
            .map(function (res) { return res.json(); })
            .catch(this.handleError);
    };
    PinpadService.prototype.handleError = function (error) {
        // In a real world app, we might use a remote logging infrastructure
        var errMsg;
        if (error instanceof http_1.Response) {
            var body = error.json() || '';
            var err = body.error || JSON.stringify(body);
            errMsg = error.status + " - " + (error.statusText || '') + " " + err;
        }
        else {
            errMsg = error.message ? error.message : error.toString();
        }
        console.error(errMsg);
        return Observable_1.Observable.throw(errMsg);
    };
    PinpadService = __decorate([
        core_1.Injectable()
    ], PinpadService);
    return PinpadService;
}());
exports.PinpadService = PinpadService;
