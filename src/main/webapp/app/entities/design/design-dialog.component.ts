import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { Design } from './design.model';
import { DesignPopupService } from './design-popup.service';
import { DesignService } from './design.service';
import { Product, ProductService } from '../product';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-design-dialog',
    templateUrl: './design-dialog.component.html'
})
export class DesignDialogComponent implements OnInit {

    design: Design;
    authorities: any[];
    isSaving: boolean;

    products: Product[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private designService: DesignService,
        private productService: ProductService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.productService.query()
            .subscribe((res: ResponseWrapper) => { this.products = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }
    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.design.id !== undefined) {
            this.subscribeToSaveResponse(
                this.designService.update(this.design));
        } else {
            this.subscribeToSaveResponse(
                this.designService.create(this.design));
        }
    }

    private subscribeToSaveResponse(result: Observable<Design>) {
        result.subscribe((res: Design) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Design) {
        this.eventManager.broadcast({ name: 'designListModification', content: 'OK'});
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
}

@Component({
    selector: 'jhi-design-popup',
    template: ''
})
export class DesignPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private designPopupService: DesignPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.designPopupService
                    .open(DesignDialogComponent, params['id']);
            } else {
                this.modalRef = this.designPopupService
                    .open(DesignDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
