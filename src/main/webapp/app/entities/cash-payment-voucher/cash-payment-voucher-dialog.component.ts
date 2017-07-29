import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { CashPaymentVoucher } from './cash-payment-voucher.model';
import { CashPaymentVoucherPopupService } from './cash-payment-voucher-popup.service';
import { CashPaymentVoucherService } from './cash-payment-voucher.service';
import { Location, LocationService } from '../location';
import { Customer, CustomerService } from '../customer';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-cash-payment-voucher-dialog',
    templateUrl: './cash-payment-voucher-dialog.component.html'
})
export class CashPaymentVoucherDialogComponent implements OnInit {

    cashPaymentVoucher: CashPaymentVoucher;
    authorities: any[];
    isSaving: boolean;

    locations: Location[];

    customers: Customer[];
    paymentDateDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private cashPaymentVoucherService: CashPaymentVoucherService,
        private locationService: LocationService,
        private customerService: CustomerService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.locationService.query()
            .subscribe((res: ResponseWrapper) => { this.locations = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.customerService.query()
            .subscribe((res: ResponseWrapper) => { this.customers = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }
    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.cashPaymentVoucher.id !== undefined) {
            this.subscribeToSaveResponse(
                this.cashPaymentVoucherService.update(this.cashPaymentVoucher));
        } else {
            this.subscribeToSaveResponse(
                this.cashPaymentVoucherService.create(this.cashPaymentVoucher));
        }
    }

    private subscribeToSaveResponse(result: Observable<CashPaymentVoucher>) {
        result.subscribe((res: CashPaymentVoucher) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: CashPaymentVoucher) {
        this.eventManager.broadcast({ name: 'cashPaymentVoucherListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    trackLocationById(index: number, item: Location) {
        return item.id;
    }

    trackCustomerById(index: number, item: Customer) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-cash-payment-voucher-popup',
    template: ''
})
export class CashPaymentVoucherPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private cashPaymentVoucherPopupService: CashPaymentVoucherPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.cashPaymentVoucherPopupService
                    .open(CashPaymentVoucherDialogComponent, params['id']);
            } else {
                this.modalRef = this.cashPaymentVoucherPopupService
                    .open(CashPaymentVoucherDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
