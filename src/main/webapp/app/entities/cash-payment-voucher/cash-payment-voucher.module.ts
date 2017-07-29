import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JPosSharedModule } from '../../shared';
import {
    CashPaymentVoucherService,
    CashPaymentVoucherPopupService,
    CashPaymentVoucherComponent,
    CashPaymentVoucherDetailComponent,
    CashPaymentVoucherDialogComponent,
    CashPaymentVoucherPopupComponent,
    CashPaymentVoucherDeletePopupComponent,
    CashPaymentVoucherDeleteDialogComponent,
    cashPaymentVoucherRoute,
    cashPaymentVoucherPopupRoute,
    CashPaymentVoucherResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...cashPaymentVoucherRoute,
    ...cashPaymentVoucherPopupRoute,
];

@NgModule({
    imports: [
        JPosSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        CashPaymentVoucherComponent,
        CashPaymentVoucherDetailComponent,
        CashPaymentVoucherDialogComponent,
        CashPaymentVoucherDeleteDialogComponent,
        CashPaymentVoucherPopupComponent,
        CashPaymentVoucherDeletePopupComponent,
    ],
    entryComponents: [
        CashPaymentVoucherComponent,
        CashPaymentVoucherDialogComponent,
        CashPaymentVoucherPopupComponent,
        CashPaymentVoucherDeleteDialogComponent,
        CashPaymentVoucherDeletePopupComponent,
    ],
    providers: [
        CashPaymentVoucherService,
        CashPaymentVoucherPopupService,
        CashPaymentVoucherResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JPosCashPaymentVoucherModule {}
