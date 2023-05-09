import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CheckoutComponent } from './checkout/checkout.component';
import { CancelComponent } from './cancel/cancel.component';
import { SucessComponent } from './sucess/sucess.component';
import { SubscriptionComponent } from './subscription/subscription.component';

const routes: Routes = [
  {
    path: 'checkout',
    component: CheckoutComponent
  },
  { path: 'cancel', component: CancelComponent },
  { path: 'success', component: SucessComponent },
  { path: 'subscription', component: SubscriptionComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
