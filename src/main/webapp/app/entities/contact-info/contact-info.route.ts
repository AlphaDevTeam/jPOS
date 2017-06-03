import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { ContactInfoComponent } from './contact-info.component';
import { ContactInfoDetailComponent } from './contact-info-detail.component';
import { ContactInfoPopupComponent } from './contact-info-dialog.component';
import { ContactInfoDeletePopupComponent } from './contact-info-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class ContactInfoResolvePagingParams implements Resolve<any> {

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

export const contactInfoRoute: Routes = [
    {
        path: 'contact-info',
        component: ContactInfoComponent,
        resolve: {
            'pagingParams': ContactInfoResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jPosApp.contactInfo.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'contact-info/:id',
        component: ContactInfoDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jPosApp.contactInfo.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const contactInfoPopupRoute: Routes = [
    {
        path: 'contact-info-new',
        component: ContactInfoPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jPosApp.contactInfo.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'contact-info/:id/edit',
        component: ContactInfoPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jPosApp.contactInfo.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'contact-info/:id/delete',
        component: ContactInfoDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'jPosApp.contactInfo.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
