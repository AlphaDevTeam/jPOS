import { CustomerCategory } from '../customer-category';
import { Address } from '../address';
import { ContactInfo } from '../contact-info';
export class Customer {
    constructor(
        public id?: number,
        public firstName?: string,
        public lastName?: string,
        public custCode?: string,
        public customerNIC?: string,
        public creditLimit?: number,
        public isActive?: boolean,
        public vatNumber?: string,
        public customerCategory?: CustomerCategory,
        public address?: Address,
        public contactInfo?: ContactInfo,
    ) {
        this.isActive = false;
    }
}
