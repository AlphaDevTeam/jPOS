import { Location } from '../location';
import { Item } from '../item';
export class Stock {
    constructor(
        public id?: number,
        public stockQty?: number,
        public stockLocation?: Location,
        public stockItem?: Item,
    ) {
    }
}
