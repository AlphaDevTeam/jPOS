import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ContactInfo } from './contact-info.model';
import { ContactInfoService } from './contact-info.service';
@Injectable()
export class ContactInfoPopupService {
    private isOpen = false;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private contactInfoService: ContactInfoService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.contactInfoService.find(id).subscribe((contactInfo) => {
                this.contactInfoModalRef(component, contactInfo);
            });
        } else {
            return this.contactInfoModalRef(component, new ContactInfo());
        }
    }

    contactInfoModalRef(component: Component, contactInfo: ContactInfo): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.contactInfo = contactInfo;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.isOpen = false;
        });
        return modalRef;
    }
}
