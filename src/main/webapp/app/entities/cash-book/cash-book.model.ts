import { Location } from '../location';
export class CashBook {
    constructor(
        public id?: number,
        public description?: string,
        public crAmount?: number,
        public drAmount?: number,
        public balanceAmount?: number,
        public relatedDate?: any,
        public relatedLocation?: Location,
    ) {
    }
}
