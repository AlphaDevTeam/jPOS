import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { CashBook } from './cash-book.model';
import { CashBookPopupService } from './cash-book-popup.service';
import { CashBookService } from './cash-book.service';
import { Location, LocationService } from '../location';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-cash-book-dialog',
    templateUrl: './cash-book-dialog.component.html'
})
export class CashBookDialogComponent implements OnInit {

    cashBook: CashBook;
    authorities: any[];
    isSaving: boolean;

    locations: Location[];
    relatedDateDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private cashBookService: CashBookService,
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
        if (this.cashBook.id !== undefined) {
            this.subscribeToSaveResponse(
                this.cashBookService.update(this.cashBook));
        } else {
            this.subscribeToSaveResponse(
                this.cashBookService.create(this.cashBook));
        }
    }

    private subscribeToSaveResponse(result: Observable<CashBook>) {
        result.subscribe((res: CashBook) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: CashBook) {
        this.eventManager.broadcast({ name: 'cashBookListModification', content: 'OK'});
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
    selector: 'jhi-cash-book-popup',
    template: ''
})
export class CashBookPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private cashBookPopupService: CashBookPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.cashBookPopupService
                    .open(CashBookDialogComponent, params['id']);
            } else {
                this.modalRef = this.cashBookPopupService
                    .open(CashBookDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
