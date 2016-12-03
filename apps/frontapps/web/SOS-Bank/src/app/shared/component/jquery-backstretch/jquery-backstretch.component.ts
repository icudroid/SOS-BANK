import {Component, OnInit, ViewChild, ElementRef, AfterViewInit} from '@angular/core';

@Component({
  selector: 'jquery-backstretch',
  template: '<div style="  background-position: center;  background-size: cover;background-repeat: no-repeat;min-height: 100vh;" #backstretch></div>',
})
export class JqueryBackstretchComponent implements OnInit {
  @ViewChild('backstretch') el:ElementRef;

  images:Array<string> = [
    "../assets/pages/img/login/bg1.jpg",
    "../assets/pages/img/login/bg2.jpg",
    "../assets/pages/img/login/bg3.jpg"
  ];

  config = {
    fade: 1000,
    duration: 8000
  };

  constructor() { }

  ngOnInit() {
  }

  ngAfterViewInit() {
    var $el:any = $(this.el.nativeElement);
      $el.backstretch(this.images,this.config);
  }

}
