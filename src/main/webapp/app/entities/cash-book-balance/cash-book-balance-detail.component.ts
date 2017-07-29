import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager  } from 'ng-jhipster';

import { CashBookBalance } from './cash-book-balance.model';
import { CashBookBalanceService } from './cash-book-balance.service';

@Component({
    selector: 'jhi-cash-book-balance-detail',
    templateUrl: './cash-book-balance-detail.component.html'
})
export class CashBookBalanceDetailComponent implements OnInit, OnDestroy {

    cashBookBalance: CashBookBalance;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private cashBookBalanceService: CashBookBalanceService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInCashBookBalances();
    }

    load(id) {
        this.cashBookBalanceService.find(id).subscribe((cashBookBalance) => {
            this.cashBookBalance = cashBookBalance;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCashBookBalances() {
        this.eventSubscriber = this.eventManager.subscribe(
            'cashBookBalanceListModification',
            (response) => this.load(this.cashBookBalance.id)
        );
    }
}
