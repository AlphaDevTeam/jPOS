import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { JPosProductModule } from './product/product.module';
import { JPosDesignModule } from './design/design.module';
import { JPosAddressModule } from './address/address.module';
import { JPosContactInfoModule } from './contact-info/contact-info.module';
import { JPosCustomerCategoryModule } from './customer-category/customer-category.module';
import { JPosCustomerModule } from './customer/customer.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        JPosProductModule,
        JPosDesignModule,
        JPosAddressModule,
        JPosContactInfoModule,
        JPosCustomerCategoryModule,
        JPosCustomerModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JPosEntityModule {}
