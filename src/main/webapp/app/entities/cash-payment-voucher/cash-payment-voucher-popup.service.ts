import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { CashPaymentVoucher } from './cash-payment-voucher.model';
import { CashPaymentVoucherService } from './cash-payment-voucher.service';
@Injectable()
export class CashPaymentVoucherPopupService {
    private isOpen = false;
    constructor(
        private modalService: NgbModal,
        private router: Router,
        private cashPaymentVoucherService: CashPaymentVoucherService

    ) {}

    open(component: Component, id?: number | any): NgbModalRef {
        if (this.isOpen) {
            return;
        }
        this.isOpen = true;

        if (id) {
            this.cashPaymentVoucherService.find(id).subscribe((cashPaymentVoucher) => {
                if (cashPaymentVoucher.paymentDate) {
                    cashPaymentVoucher.paymentDate = {
                        year: cashPaymentVoucher.paymentDate.getFullYear(),
                        month: cashPaymentVoucher.paymentDate.getMonth() + 1,
                        day: cashPaymentVoucher.paymentDate.getDate()
                    };
                }
                this.cashPaymentVoucherModalRef(component, cashPaymentVoucher);
            });
        } else {
            return this.cashPaymentVoucherModalRef(component, new CashPaymentVoucher());
        }
    }

    cashPaymentVoucherModalRef(component: Component, cashPaymentVoucher: CashPaymentVoucher): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.cashPaymentVoucher = cashPaymentVoucher;
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
