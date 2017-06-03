import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JPosSharedModule } from '../../shared';
import {
    CustomerCategoryService,
    CustomerCategoryPopupService,
    CustomerCategoryComponent,
    CustomerCategoryDetailComponent,
    CustomerCategoryDialogComponent,
    CustomerCategoryPopupComponent,
    CustomerCategoryDeletePopupComponent,
    CustomerCategoryDeleteDialogComponent,
    customerCategoryRoute,
    customerCategoryPopupRoute,
    CustomerCategoryResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...customerCategoryRoute,
    ...customerCategoryPopupRoute,
];

@NgModule({
    imports: [
        JPosSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        CustomerCategoryComponent,
        CustomerCategoryDetailComponent,
        CustomerCategoryDialogComponent,
        CustomerCategoryDeleteDialogComponent,
        CustomerCategoryPopupComponent,
        CustomerCategoryDeletePopupComponent,
    ],
    entryComponents: [
        CustomerCategoryComponent,
        CustomerCategoryDialogComponent,
        CustomerCategoryPopupComponent,
        CustomerCategoryDeleteDialogComponent,
        CustomerCategoryDeletePopupComponent,
    ],
    providers: [
        CustomerCategoryService,
        CustomerCategoryPopupService,
        CustomerCategoryResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JPosCustomerCategoryModule {}
