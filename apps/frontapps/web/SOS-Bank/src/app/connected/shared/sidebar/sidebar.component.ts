import {Component, OnInit, HostListener, Input} from '@angular/core';


export interface IMenu {
  id : string;
  title: string;
  icon : string;
  subMenus? : Array<ISubMenu>;
  permission: string;

}

export interface ISubMenu {
  id : string;
  title: string;
  icon : string;
  route : string;
  permission: string;
}


/*

 <li class="nav-item start " [ngClass]="isActive('Dashboard')" (click)="onOpenMenu('Dashboard')">
  <a href="javascript:;" class="nav-link nav-toggle" >
    <i class="icon-home"></i>
     <span class="title">Dashboard</span>
     <span class="arrow"></span>
   </a>
  <ul class="sub-menu">
 <li class="nav-item start ">
 <a href="index.html" class="nav-link ">
 <i class="icon-bar-chart"></i>
 <span class="title">Dashboard 1</span>
 </a>
 </li>
 <li class="nav-item start ">
 <a href="dashboard_2.html" class="nav-link ">
 <i class="icon-bulb"></i>
 <span class="title">Dashboard 2</span>
 <span class="badge badge-success">1</span>
 </a>
 </li>
 <li class="nav-item start ">
 <a href="dashboard_3.html" class="nav-link ">
 <i class="icon-graph"></i>
 <span class="title">Dashboard 3</span>
 <span class="badge badge-danger">5</span>
 </a>
 </li>
 </ul>
 </li>

 */


@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent implements OnInit {

  menus: IMenu[] = [
    {
      id : 'Menu_1',
      title: 'Menu_1',
      icon : 'icon-home',
      subMenus : [
        {
          id : 'Sub_1',
          title: 'Sub_1',
          icon : 'icon-bulb',
          route : '/route1',
          permission: ''
        },
        {
          id : 'Sub_2',
          title: 'Sub_2',
          icon : 'icon-bulb',
          route : '/route2',
          permission: ''
        }
      ],
      permission: ''
    }

  ];


  @Input()
  height:string;

  @Input()
  sideBarClose:boolean;

  openMenu:string;

  constructor() {
  }

  ngOnInit() {
  }

  ngAfterViewInit() {

  }

  onOpenMenu(menu){
    if(this.openMenu == menu){
      this.openMenu = '';
    }else{
      this.openMenu = menu;
    }
  }

  isActive(menu){
    if(menu == this.openMenu)
      return 'active open';

    return '';
  }

  isOpenMenu(menu){
    if(menu == this.openMenu)
      return 'arrow open';

    return 'arrow';
  }


}
