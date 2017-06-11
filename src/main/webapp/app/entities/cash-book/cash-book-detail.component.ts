import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager  } from 'ng-jhipster';

import { CashBook } from './cash-book.model';
import { CashBookService } from './cash-book.service';

@Component({
    selector: 'jhi-cash-book-detail',
    templateUrl: './cash-book-detail.component.html'
})
export class CashBookDetailComponent implements OnInit, OnDestroy {

    cashBook: CashBook;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private cashBookService: CashBookService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInCashBooks();
    }

    load(id) {
        this.cashBookService.find(id).subscribe((cashBook) => {
            this.cashBook = cashBook;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCashBooks() {
        this.eventSubscriber = this.eventManager.subscribe(
            'cashBookListModification',
            (response) => this.load(this.cashBook.id)
        );
    }
}
