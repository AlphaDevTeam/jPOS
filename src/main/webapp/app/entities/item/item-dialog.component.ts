import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { Item } from './item.model';
import { ItemPopupService } from './item-popup.service';
import { ItemService } from './item.service';
import { Product, ProductService } from '../product';
import { Design, DesignService } from '../design';
import { Location, LocationService } from '../location';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-item-dialog',
    templateUrl: './item-dialog.component.html'
})
export class ItemDialogComponent implements OnInit {

    item: Item;
    authorities: any[];
    isSaving: boolean;

    products: Product[];

    designs: Design[];

    locations: Location[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private itemService: ItemService,
        private productService: ProductService,
        private designService: DesignService,
        private locationService: LocationService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.productService.query()
            .subscribe((res: ResponseWrapper) => { this.products = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.designService.query()
            .subscribe((res: ResponseWrapper) => { this.designs = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.locationService.query()
            .subscribe((res: ResponseWrapper) => { this.locations = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }
    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.item.id !== undefined) {
            this.subscribeToSaveResponse(
                this.itemService.update(this.item));
        } else {
            this.subscribeToSaveResponse(
                this.itemService.create(this.item));
        }
    }

    private subscribeToSaveResponse(result: Observable<Item>) {
        result.subscribe((res: Item) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Item) {
        this.eventManager.broadcast({ name: 'itemListModification', content: 'OK'});
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

    trackProductById(index: number, item: Product) {
        return item.id;
    }

    trackDesignById(index: number, item: Design) {
        return item.id;
    }

    trackLocationById(index: number, item: Location) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-item-popup',
    template: ''
})
export class ItemPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private itemPopupService: ItemPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.itemPopupService
                    .open(ItemDialogComponent, params['id']);
            } else {
                this.modalRef = this.itemPopupService
                    .open(ItemDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
