import {Component, OnInit, ViewChild, ElementRef} from '@angular/core';



@Component({
  selector: 'app-connected',
  templateUrl: './connected.component.html',
  styleUrls: ['./connected.component.scss']
})
export class ConnectedComponent implements OnInit {

  @ViewChild('pageContent') pageContent:ElementRef;

  componentHeight:string;
  sideMenuClose:boolean = false;

  constructor() { }

  ngAfterViewChecked(){
    this.componentHeight = $(this.pageContent.nativeElement).outerHeight()+"px";
  }

  ngOnInit() {

  }

  onMenuState($event){
    this.sideMenuClose=!this.sideMenuClose;
  }
}
