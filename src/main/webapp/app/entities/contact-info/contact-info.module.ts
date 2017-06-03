import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JPosSharedModule } from '../../shared';
import {
    ContactInfoService,
    ContactInfoPopupService,
    ContactInfoComponent,
    ContactInfoDetailComponent,
    ContactInfoDialogComponent,
    ContactInfoPopupComponent,
    ContactInfoDeletePopupComponent,
    ContactInfoDeleteDialogComponent,
    contactInfoRoute,
    contactInfoPopupRoute,
    ContactInfoResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...contactInfoRoute,
    ...contactInfoPopupRoute,
];

@NgModule({
    imports: [
        JPosSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ContactInfoComponent,
        ContactInfoDetailComponent,
        ContactInfoDialogComponent,
        ContactInfoDeleteDialogComponent,
        ContactInfoPopupComponent,
        ContactInfoDeletePopupComponent,
    ],
    entryComponents: [
        ContactInfoComponent,
        ContactInfoDialogComponent,
        ContactInfoPopupComponent,
        ContactInfoDeleteDialogComponent,
        ContactInfoDeletePopupComponent,
    ],
    providers: [
        ContactInfoService,
        ContactInfoPopupService,
        ContactInfoResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JPosContactInfoModule {}
