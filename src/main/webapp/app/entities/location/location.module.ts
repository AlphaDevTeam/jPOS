import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JPosSharedModule } from '../../shared';
import {
    LocationService,
    LocationPopupService,
    LocationComponent,
    LocationDetailComponent,
    LocationDialogComponent,
    LocationPopupComponent,
    LocationDeletePopupComponent,
    LocationDeleteDialogComponent,
    locationRoute,
    locationPopupRoute,
    LocationResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...locationRoute,
    ...locationPopupRoute,
];

@NgModule({
    imports: [
        JPosSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        LocationComponent,
        LocationDetailComponent,
        LocationDialogComponent,
        LocationDeleteDialogComponent,
        LocationPopupComponent,
        LocationDeletePopupComponent,
    ],
    entryComponents: [
        LocationComponent,
        LocationDialogComponent,
        LocationPopupComponent,
        LocationDeleteDialogComponent,
        LocationDeletePopupComponent,
    ],
    providers: [
        LocationService,
        LocationPopupService,
        LocationResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JPosLocationModule {}
