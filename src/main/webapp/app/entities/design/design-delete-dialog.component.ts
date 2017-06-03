import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager } from 'ng-jhipster';

import { Design } from './design.model';
import { DesignPopupService } from './design-popup.service';
import { DesignService } from './design.service';

@Component({
    selector: 'jhi-design-delete-dialog',
    templateUrl: './design-delete-dialog.component.html'
})
export class DesignDeleteDialogComponent {

    design: Design;

    constructor(
        private designService: DesignService,
        public activeModal: NgbActiveModal,
        private eventManager: EventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.designService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'designListModification',
                content: 'Deleted an design'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-design-delete-popup',
    template: ''
})
export class DesignDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private designPopupService: DesignPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.designPopupService
                .open(DesignDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
