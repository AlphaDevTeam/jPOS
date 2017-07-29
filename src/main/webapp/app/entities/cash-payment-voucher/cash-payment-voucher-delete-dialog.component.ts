import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager } from 'ng-jhipster';

import { CashPaymentVoucher } from './cash-payment-voucher.model';
import { CashPaymentVoucherPopupService } from './cash-payment-voucher-popup.service';
import { CashPaymentVoucherService } from './cash-payment-voucher.service';

@Component({
    selector: 'jhi-cash-payment-voucher-delete-dialog',
    templateUrl: './cash-payment-voucher-delete-dialog.component.html'
})
export class CashPaymentVoucherDeleteDialogComponent {

    cashPaymentVoucher: CashPaymentVoucher;

    constructor(
        private cashPaymentVoucherService: CashPaymentVoucherService,
        public activeModal: NgbActiveModal,
        private eventManager: EventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.cashPaymentVoucherService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'cashPaymentVoucherListModification',
                content: 'Deleted an cashPaymentVoucher'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-cash-payment-voucher-delete-popup',
    template: ''
})
export class CashPaymentVoucherDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private cashPaymentVoucherPopupService: CashPaymentVoucherPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.cashPaymentVoucherPopupService
                .open(CashPaymentVoucherDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
