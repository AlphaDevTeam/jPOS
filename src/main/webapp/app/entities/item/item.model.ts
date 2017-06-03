import { Product } from '../product';
import { Design } from '../design';
import { Location } from '../location';
export class Item {
    constructor(
        public id?: number,
        public itemCode?: string,
        public itemTitle?: string,
        public itemBarcode?: string,
        public itemDescription?: string,
        public itemCost?: number,
        public itemUnitPrice?: number,
        public itemReorderLevel?: number,
        public relatedProduct?: Product,
        public relatedDesign?: Design,
        public itemLocation?: Location,
    ) {
    }
}
