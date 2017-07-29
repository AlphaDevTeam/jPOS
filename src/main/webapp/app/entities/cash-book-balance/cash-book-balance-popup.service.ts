import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { CashBookBalance } from './cash-book-balance.model';
import { CashBookBalanceService } from './cash-book-balance.service';
@Injectable()
export class CashBookBalancePopupService {
    private isOpen = false;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private cashBookBalanceService: CashBookBalanceService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.cashBookBalanceService.find(id).subscribe((cashBookBalance) => {
                this.cashBookBalanceModalRef(component, cashBookBalance);
            });
        } else {
            return this.cashBookBalanceModalRef(component, new CashBookBalance());
        }
    }

    cashBookBalanceModalRef(component: Component, cashBookBalance: CashBookBalance): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.cashBookBalance = cashBookBalance;
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
