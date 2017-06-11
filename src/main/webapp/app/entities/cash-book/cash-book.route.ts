import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { CashBookComponent } from './cash-book.component';
import { CashBookDetailComponent } from './cash-book-detail.component';
import { CashBookPopupComponent } from './cash-book-dialog.component';
import { CashBookDeletePopupComponent } from './cash-book-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class CashBookResolvePagingParams implements Resolve<any> {

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

export const cashBookRoute: Routes = [
    {
        path: 'cash-book',
        component: CashBookComponent,
        resolve: {
            'pagingParams': CashBookResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jPosApp.cashBook.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'cash-book/:id',
        component: CashBookDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jPosApp.cashBook.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const cashBookPopupRoute: Routes = [
    {
        path: 'cash-book-new',
        component: CashBookPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jPosApp.cashBook.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'cash-book/:id/edit',
        component: CashBookPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jPosApp.cashBook.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'cash-book/:id/delete',
        component: CashBookDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jPosApp.cashBook.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
