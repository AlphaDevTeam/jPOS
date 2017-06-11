import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager  } from 'ng-jhipster';

import { Stock } from './stock.model';
import { StockService } from './stock.service';

@Component({
    selector: 'jhi-stock-detail',
    templateUrl: './stock-detail.component.html'
})
export class StockDetailComponent implements OnInit, OnDestroy {

    stock: Stock;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private stockService: StockService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInStocks();
    }

    load(id) {
        this.stockService.find(id).subscribe((stock) => {
            this.stock = stock;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInStocks() {
        this.eventSubscriber = this.eventManager.subscribe(
            'stockListModification',
            (response) => this.load(this.stock.id)
        );
    }
}
