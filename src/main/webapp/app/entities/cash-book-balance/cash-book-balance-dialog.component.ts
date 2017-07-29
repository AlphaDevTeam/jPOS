import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { CashBookBalance } from './cash-book-balance.model';
import { CashBookBalancePopupService } from './cash-book-balance-popup.service';
import { CashBookBalanceService } from './cash-book-balance.service';
import { Location, LocationService } from '../location';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-cash-book-balance-dialog',
    templateUrl: './cash-book-balance-dialog.component.html'
})
export class CashBookBalanceDialogComponent implements OnInit {

    cashBookBalance: CashBookBalance;
    authorities: any[];
    isSaving: boolean;

    locations: Location[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private cashBookBalanceService: CashBookBalanceService,
        private locationService: LocationService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.locationService.query()
            .subscribe((res: ResponseWrapper) => { this.locations = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }
    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.cashBookBalance.id !== undefined) {
            this.subscribeToSaveResponse(
                this.cashBookBalanceService.update(this.cashBookBalance));
        } else {
            this.subscribeToSaveResponse(
                this.cashBookBalanceService.create(this.cashBookBalance));
        }
    }

    private subscribeToSaveResponse(result: Observable<CashBookBalance>) {
        result.subscribe((res: CashBookBalance) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: CashBookBalance) {
        this.eventManager.broadcast({ name: 'cashBookBalanceListModification', content: 'OK'});
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
}

@Component({
    selector: 'jhi-cash-book-balance-popup',
    template: ''
})
export class CashBookBalancePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private cashBookBalancePopupService: CashBookBalancePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.cashBookBalancePopupService
                    .open(CashBookBalanceDialogComponent, params['id']);
            } else {
                this.modalRef = this.cashBookBalancePopupService
                    .open(CashBookBalanceDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
