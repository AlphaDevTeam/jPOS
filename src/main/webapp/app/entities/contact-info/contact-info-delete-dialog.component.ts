import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager } from 'ng-jhipster';

import { ContactInfo } from './contact-info.model';
import { ContactInfoPopupService } from './contact-info-popup.service';
import { ContactInfoService } from './contact-info.service';

@Component({
    selector: 'jhi-contact-info-delete-dialog',
    templateUrl: './contact-info-delete-dialog.component.html'
})
export class ContactInfoDeleteDialogComponent {

    contactInfo: ContactInfo;

    constructor(
        private contactInfoService: ContactInfoService,
        public activeModal: NgbActiveModal,
        private eventManager: EventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.contactInfoService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'contactInfoListModification',
                content: 'Deleted an contactInfo'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-contact-info-delete-popup',
    template: ''
})
export class ContactInfoDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private contactInfoPopupService: ContactInfoPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.contactInfoPopupService
                .open(ContactInfoDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
