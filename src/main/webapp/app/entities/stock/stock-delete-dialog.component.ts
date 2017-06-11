import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager } from 'ng-jhipster';

import { Stock } from './stock.model';
import { StockPopupService } from './stock-popup.service';
import { StockService } from './stock.service';

@Component({
    selector: 'jhi-stock-delete-dialog',
    templateUrl: './stock-delete-dialog.component.html'
})
export class StockDeleteDialogComponent {

    stock: Stock;

    constructor(
        private stockService: StockService,
        public activeModal: NgbActiveModal,
        private eventManager: EventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.stockService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'stockListModification',
                content: 'Deleted an stock'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-stock-delete-popup',
    template: ''
})
export class StockDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private stockPopupService: StockPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.stockPopupService
                .open(StockDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
