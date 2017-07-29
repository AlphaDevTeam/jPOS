import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager } from 'ng-jhipster';

import { CashBookBalance } from './cash-book-balance.model';
import { CashBookBalancePopupService } from './cash-book-balance-popup.service';
import { CashBookBalanceService } from './cash-book-balance.service';

@Component({
    selector: 'jhi-cash-book-balance-delete-dialog',
    templateUrl: './cash-book-balance-delete-dialog.component.html'
})
export class CashBookBalanceDeleteDialogComponent {

    cashBookBalance: CashBookBalance;

    constructor(
        private cashBookBalanceService: CashBookBalanceService,
        public activeModal: NgbActiveModal,
        private eventManager: EventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.cashBookBalanceService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'cashBookBalanceListModification',
                content: 'Deleted an cashBookBalance'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-cash-book-balance-delete-popup',
    template: ''
})
export class CashBookBalanceDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private cashBookBalancePopupService: CashBookBalancePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.cashBookBalancePopupService
                .open(CashBookBalanceDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
