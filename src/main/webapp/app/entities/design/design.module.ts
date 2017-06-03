import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JPosSharedModule } from '../../shared';
import {
    DesignService,
    DesignPopupService,
    DesignComponent,
    DesignDetailComponent,
    DesignDialogComponent,
    DesignPopupComponent,
    DesignDeletePopupComponent,
    DesignDeleteDialogComponent,
    designRoute,
    designPopupRoute,
    DesignResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...designRoute,
    ...designPopupRoute,
];

@NgModule({
    imports: [
        JPosSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        DesignComponent,
        DesignDetailComponent,
        DesignDialogComponent,
        DesignDeleteDialogComponent,
        DesignPopupComponent,
        DesignDeletePopupComponent,
    ],
    entryComponents: [
        DesignComponent,
        DesignDialogComponent,
        DesignPopupComponent,
        DesignDeleteDialogComponent,
        DesignDeletePopupComponent,
    ],
    providers: [
        DesignService,
        DesignPopupService,
        DesignResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JPosDesignModule {}
