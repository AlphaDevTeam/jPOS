import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { Stock } from './stock.model';
import { StockPopupService } from './stock-popup.service';
import { StockService } from './stock.service';
import { Location, LocationService } from '../location';
import { Item, ItemService } from '../item';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-stock-dialog',
    templateUrl: './stock-dialog.component.html'
})
export class StockDialogComponent implements OnInit {

    stock: Stock;
    authorities: any[];
    isSaving: boolean;

    locations: Location[];

    items: Item[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private stockService: StockService,
        private locationService: LocationService,
        private itemService: ItemService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.locationService.query()
            .subscribe((res: ResponseWrapper) => { this.locations = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.itemService.query()
            .subscribe((res: ResponseWrapper) => { this.items = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }
    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.stock.id !== undefined) {
            this.subscribeToSaveResponse(
                this.stockService.update(this.stock));
        } else {
            this.subscribeToSaveResponse(
                this.stockService.create(this.stock));
        }
    }

    private subscribeToSaveResponse(result: Observable<Stock>) {
        result.subscribe((res: Stock) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Stock) {
        this.eventManager.broadcast({ name: 'stockListModification', content: 'OK'});
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

    trackItemById(index: number, item: Item) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-stock-popup',
    template: ''
})
export class StockPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private stockPopupService: StockPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.stockPopupService
                    .open(StockDialogComponent, params['id']);
            } else {
                this.modalRef = this.stockPopupService
                    .open(StockDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
