import { Product } from '../product';
export class Design {
    constructor(
        public id?: number,
        public designDescription?: string,
        public designCode?: string,
        public profitPerc?: number,
        public designPrefix?: string,
        public relatedProduct?: Product,
    ) {
    }
}
