import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { CancelComponent } from './cancel/cancel.component';
import { SucessComponent } from './sucess/sucess.component';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { HttpClientModule } from '@angular/common/http';
import { CheckoutComponent } from './checkout/checkout.component';
import { SubscriptionComponent } from './subscription/subscription.component';

@NgModule({
  declarations: [
    AppComponent,
    CancelComponent,
    SucessComponent,
    CheckoutComponent,
    SubscriptionComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,

    MatCardModule,
    MatButtonModule,
    HttpClientModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
