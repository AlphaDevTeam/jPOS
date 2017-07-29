import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JPosSharedModule } from '../../shared';
import {
    CashBookBalanceService,
    CashBookBalancePopupService,
    CashBookBalanceComponent,
    CashBookBalanceDetailComponent,
    CashBookBalanceDialogComponent,
    CashBookBalancePopupComponent,
    CashBookBalanceDeletePopupComponent,
    CashBookBalanceDeleteDialogComponent,
    cashBookBalanceRoute,
    cashBookBalancePopupRoute,
    CashBookBalanceResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...cashBookBalanceRoute,
    ...cashBookBalancePopupRoute,
];

@NgModule({
    imports: [
        JPosSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        CashBookBalanceComponent,
        CashBookBalanceDetailComponent,
        CashBookBalanceDialogComponent,
        CashBookBalanceDeleteDialogComponent,
        CashBookBalancePopupComponent,
        CashBookBalanceDeletePopupComponent,
    ],
    entryComponents: [
        CashBookBalanceComponent,
        CashBookBalanceDialogComponent,
        CashBookBalancePopupComponent,
        CashBookBalanceDeleteDialogComponent,
        CashBookBalanceDeletePopupComponent,
    ],
    providers: [
        CashBookBalanceService,
        CashBookBalancePopupService,
        CashBookBalanceResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JPosCashBookBalanceModule {}
