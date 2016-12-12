import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ConnectedComponent } from './connected.component';
import { HeaderComponent } from './shared/header/header.component';
import { SidebarComponent } from './shared/sidebar/sidebar.component';
import { FooterComponent } from './shared/footer/footer.component';
import {ConnectedRoutingModule} from "./connected-routing.module";

@NgModule({
  imports: [
    CommonModule,
    ConnectedRoutingModule
  ],
  declarations: [ConnectedComponent, HeaderComponent, SidebarComponent, FooterComponent]
})
export class ConnectedModule { }
