import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JPosSharedModule } from '../../shared';
import {
    StockService,
    StockPopupService,
    StockComponent,
    StockDetailComponent,
    StockDialogComponent,
    StockPopupComponent,
    StockDeletePopupComponent,
    StockDeleteDialogComponent,
    stockRoute,
    stockPopupRoute,
    StockResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...stockRoute,
    ...stockPopupRoute,
];

@NgModule({
    imports: [
        JPosSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        StockComponent,
        StockDetailComponent,
        StockDialogComponent,
        StockDeleteDialogComponent,
        StockPopupComponent,
        StockDeletePopupComponent,
    ],
    entryComponents: [
        StockComponent,
        StockDialogComponent,
        StockPopupComponent,
        StockDeleteDialogComponent,
        StockDeletePopupComponent,
    ],
    providers: [
        StockService,
        StockPopupService,
        StockResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JPosStockModule {}
