import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager } from 'ng-jhipster';

import { CashBook } from './cash-book.model';
import { CashBookPopupService } from './cash-book-popup.service';
import { CashBookService } from './cash-book.service';

@Component({
    selector: 'jhi-cash-book-delete-dialog',
    templateUrl: './cash-book-delete-dialog.component.html'
})
export class CashBookDeleteDialogComponent {

    cashBook: CashBook;

    constructor(
        private cashBookService: CashBookService,
        public activeModal: NgbActiveModal,
        private eventManager: EventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.cashBookService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'cashBookListModification',
                content: 'Deleted an cashBook'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-cash-book-delete-popup',
    template: ''
})
export class CashBookDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private cashBookPopupService: CashBookPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.cashBookPopupService
                .open(CashBookDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
