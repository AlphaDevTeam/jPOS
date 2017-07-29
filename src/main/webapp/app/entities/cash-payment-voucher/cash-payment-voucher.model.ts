import { Location } from '../location';
import { Customer } from '../customer';
export class CashPaymentVoucher {
    constructor(
        public id?: number,
        public paymentNumber?: string,
        public paymentRefNumber?: string,
        public description?: string,
        public paymentDate?: any,
        public paymentAmount?: number,
        public relatedLocation?: Location,
        public relatedCustomer?: Customer,
    ) {
    }
}
