import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { Design } from './design.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class DesignService {

    private resourceUrl = 'api/designs';
    private resourceUrlByProducts = 'api//designs/product';
    private resourceSearchUrl = 'api/_search/designs';

    constructor(private http: Http) { }

    create(design: Design): Observable<Design> {
        const copy = this.convert(design);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(design: Design): Observable<Design> {
        const copy = this.convert(design);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<Design> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            return res.json();
        });
    }

     findByProduct(productid: number): Observable<ResponseWrapper> {
        return this.http.get(`${this.resourceUrlByProducts}/?id=${productid}`)
            .map((res: Response) => this.convertResponse(res));
     }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        return new ResponseWrapper(res.headers, jsonResponse);
    }

    private convert(design: Design): Design {
        const copy: Design = Object.assign({}, design);
        return copy;
    }
}
