import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JPosSharedModule } from '../../shared';
import {
    CashBookService,
    CashBookPopupService,
    CashBookComponent,
    CashBookDetailComponent,
    CashBookDialogComponent,
    CashBookPopupComponent,
    CashBookDeletePopupComponent,
    CashBookDeleteDialogComponent,
    cashBookRoute,
    cashBookPopupRoute,
    CashBookResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...cashBookRoute,
    ...cashBookPopupRoute,
];

@NgModule({
    imports: [
        JPosSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        CashBookComponent,
        CashBookDetailComponent,
        CashBookDialogComponent,
        CashBookDeleteDialogComponent,
        CashBookPopupComponent,
        CashBookDeletePopupComponent,
    ],
    entryComponents: [
        CashBookComponent,
        CashBookDialogComponent,
        CashBookPopupComponent,
        CashBookDeleteDialogComponent,
        CashBookDeletePopupComponent,
    ],
    providers: [
        CashBookService,
        CashBookPopupService,
        CashBookResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JPosCashBookModule {}
