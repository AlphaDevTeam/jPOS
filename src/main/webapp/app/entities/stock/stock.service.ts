import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { Stock } from './stock.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class StockService {

    private resourceUrl = 'api/stocks';
    private resourceSearchUrl = 'api/_search/stocks';

    constructor(private http: Http) { }

    create(stock: Stock): Observable<Stock> {
        const copy = this.convert(stock);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(stock: Stock): Observable<Stock> {
        const copy = this.convert(stock);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<Stock> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            return res.json();
        });
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

    private convert(stock: Stock): Stock {
        const copy: Stock = Object.assign({}, stock);
        return copy;
    }
}
