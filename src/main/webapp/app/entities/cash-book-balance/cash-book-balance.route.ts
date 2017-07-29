import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { CashBookBalanceComponent } from './cash-book-balance.component';
import { CashBookBalanceDetailComponent } from './cash-book-balance-detail.component';
import { CashBookBalancePopupComponent } from './cash-book-balance-dialog.component';
import { CashBookBalanceDeletePopupComponent } from './cash-book-balance-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class CashBookBalanceResolvePagingParams implements Resolve<any> {

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

export const cashBookBalanceRoute: Routes = [
    {
        path: 'cash-book-balance',
        component: CashBookBalanceComponent,
        resolve: {
            'pagingParams': CashBookBalanceResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jPosApp.cashBookBalance.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'cash-book-balance/:id',
        component: CashBookBalanceDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jPosApp.cashBookBalance.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const cashBookBalancePopupRoute: Routes = [
    {
        path: 'cash-book-balance-new',
        component: CashBookBalancePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jPosApp.cashBookBalance.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'cash-book-balance/:id/edit',
        component: CashBookBalancePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jPosApp.cashBookBalance.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'cash-book-balance/:id/delete',
        component: CashBookBalanceDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jPosApp.cashBookBalance.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
