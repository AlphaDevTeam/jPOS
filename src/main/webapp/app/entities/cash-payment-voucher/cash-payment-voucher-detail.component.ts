import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager  } from 'ng-jhipster';

import { CashPaymentVoucher } from './cash-payment-voucher.model';
import { CashPaymentVoucherService } from './cash-payment-voucher.service';

@Component({
    selector: 'jhi-cash-payment-voucher-detail',
    templateUrl: './cash-payment-voucher-detail.component.html'
})
export class CashPaymentVoucherDetailComponent implements OnInit, OnDestroy {

    cashPaymentVoucher: CashPaymentVoucher;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private cashPaymentVoucherService: CashPaymentVoucherService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInCashPaymentVouchers();
    }

    load(id) {
        this.cashPaymentVoucherService.find(id).subscribe((cashPaymentVoucher) => {
            this.cashPaymentVoucher = cashPaymentVoucher;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCashPaymentVouchers() {
        this.eventSubscriber = this.eventManager.subscribe(
            'cashPaymentVoucherListModification',
            (response) => this.load(this.cashPaymentVoucher.id)
        );
    }
}
