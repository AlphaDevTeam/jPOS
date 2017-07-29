import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { CashPaymentVoucherComponent } from './cash-payment-voucher.component';
import { CashPaymentVoucherDetailComponent } from './cash-payment-voucher-detail.component';
import { CashPaymentVoucherPopupComponent } from './cash-payment-voucher-dialog.component';
import { CashPaymentVoucherDeletePopupComponent } from './cash-payment-voucher-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class CashPaymentVoucherResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: PaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const cashPaymentVoucherRoute: Routes = [
    {
        path: 'cash-payment-voucher',
        component: CashPaymentVoucherComponent,
        resolve: {
            'pagingParams': CashPaymentVoucherResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jPosApp.cashPaymentVoucher.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'cash-payment-voucher/:id',
        component: CashPaymentVoucherDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jPosApp.cashPaymentVoucher.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const cashPaymentVoucherPopupRoute: Routes = [
    {
        path: 'cash-payment-voucher-new',
        component: CashPaymentVoucherPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jPosApp.cashPaymentVoucher.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'cash-payment-voucher/:id/edit',
        component: CashPaymentVoucherPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jPosApp.cashPaymentVoucher.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'cash-payment-voucher/:id/delete',
        component: CashPaymentVoucherDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jPosApp.cashPaymentVoucher.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
