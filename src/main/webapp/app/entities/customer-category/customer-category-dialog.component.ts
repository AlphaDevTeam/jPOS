import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { CustomerCategory } from './customer-category.model';
import { CustomerCategoryPopupService } from './customer-category-popup.service';
import { CustomerCategoryService } from './customer-category.service';

@Component({
    selector: 'jhi-customer-category-dialog',
    templateUrl: './customer-category-dialog.component.html'
})
export class CustomerCategoryDialogComponent implements OnInit {

    customerCategory: CustomerCategory;
    authorities: any[];
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private customerCategoryService: CustomerCategoryService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
    }
    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.customerCategory.id !== undefined) {
            this.subscribeToSaveResponse(
                this.customerCategoryService.update(this.customerCategory));
        } else {
            this.subscribeToSaveResponse(
                this.customerCategoryService.create(this.customerCategory));
        }
    }

    private subscribeToSaveResponse(result: Observable<CustomerCategory>) {
        result.subscribe((res: CustomerCategory) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: CustomerCategory) {
        this.eventManager.broadcast({ name: 'customerCategoryListModification', content: 'OK'});
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
}

@Component({
    selector: 'jhi-customer-category-popup',
    template: ''
})
export class CustomerCategoryPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private customerCategoryPopupService: CustomerCategoryPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.customerCategoryPopupService
                    .open(CustomerCategoryDialogComponent, params['id']);
            } else {
                this.modalRef = this.customerCategoryPopupService
                    .open(CustomerCategoryDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
