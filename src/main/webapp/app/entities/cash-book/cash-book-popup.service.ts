import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { CashBook } from './cash-book.model';
import { CashBookService } from './cash-book.service';
@Injectable()
export class CashBookPopupService {
    private isOpen = false;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private cashBookService: CashBookService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.cashBookService.find(id).subscribe((cashBook) => {
                if (cashBook.relatedDate) {
                    cashBook.relatedDate = {
                        year: cashBook.relatedDate.getFullYear(),
                        month: cashBook.relatedDate.getMonth() + 1,
                        day: cashBook.relatedDate.getDate()
                    };
                }
                this.cashBookModalRef(component, cashBook);
            });
        } else {
            return this.cashBookModalRef(component, new CashBook());
        }
    }

    cashBookModalRef(component: Component, cashBook: CashBook): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.cashBook = cashBook;
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
