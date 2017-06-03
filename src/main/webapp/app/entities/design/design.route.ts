import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { DesignComponent } from './design.component';
import { DesignDetailComponent } from './design-detail.component';
import { DesignPopupComponent } from './design-dialog.component';
import { DesignDeletePopupComponent } from './design-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class DesignResolvePagingParams implements Resolve<any> {

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

export const designRoute: Routes = [
    {
        path: 'design',
        component: DesignComponent,
        resolve: {
            'pagingParams': DesignResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jPosApp.design.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'design/:id',
        component: DesignDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jPosApp.design.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const designPopupRoute: Routes = [
    {
        path: 'design-new',
        component: DesignPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jPosApp.design.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'design/:id/edit',
        component: DesignPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jPosApp.design.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'design/:id/delete',
        component: DesignDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jPosApp.design.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
