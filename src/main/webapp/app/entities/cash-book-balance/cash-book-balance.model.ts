import { Location } from '../location';
export class CashBookBalance {
    constructor(
        public id?: number,
        public cashBalance?: number,
        public bankBalance?: number,
        public relatedLocation?: Location,
    ) {
    }
}
