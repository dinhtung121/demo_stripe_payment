import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { loadStripe } from '@stripe/stripe-js';
import { environment } from 'environment';

@Component({
  selector: 'app-subscription',
  templateUrl: './subscription.component.html',
  styleUrls: ['./subscription.component.scss']
})
export class SubscriptionComponent {
  // get the ids of prices
  monthlyPriceId = 'price_1N5RSGL2PfBCDjdOGWmyDezQ';
  yearlyPriceId = 'price_1N5RVBL2PfBCDjdOVC2adjhc';

  // load the stripejs
  stripePromise = loadStripe(environment.stripe);

  constructor(private http: HttpClient) { }

  async checkoutMonthly(): Promise<void> {
    this.checkout(this.monthlyPriceId);
  }

  async checkoutYearly(): Promise<void> {
    this.checkout(this.yearlyPriceId);
  }

  /**
   * this method do the checkout for a priceId and it is async because it awaiting the Promise object
   */
  private async checkout(priceId: string): Promise<void> {
    const checkout = {
      priceId: priceId,
      cancelUrl: 'http://localhost:4200/canceled',
      successUrl: 'http://localhost:4200/success',
    };
    const stripe = await this.stripePromise;
    // this is a normal http calls for a backend api
    this.http
      .post(`${environment.serverUrl}/subscription`, checkout)
      .subscribe((data: any) => {
        // I use stripe to redirect To Checkout page of Stripe platform
        if (stripe)
        stripe.redirectToCheckout({
          sessionId: data.sessionId,
        });
      });
  }
}
