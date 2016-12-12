import {Component, OnInit, ViewChild, ElementRef, AfterViewInit, Input} from '@angular/core';

@Component({
  selector: 'jquery-backstretch',
  template: '<div style="  background-position: center;  background-size: cover;background-repeat: no-repeat;min-height: 100vh;" #backstretch></div>',
})
export class JqueryBackstretchComponent implements OnInit {
  @ViewChild('backstretch') el:ElementRef;


  @Input()
  images:Array<string>;

  @Input()
  config;

  constructor() { }

  ngOnInit() {
  }

  ngAfterViewInit() {
    var $el:any = $(this.el.nativeElement);
      $el.backstretch(this.images,this.config);
  }

}
