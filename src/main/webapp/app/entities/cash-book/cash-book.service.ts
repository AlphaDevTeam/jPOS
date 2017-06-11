import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { DateUtils } from 'ng-jhipster';

import { CashBook } from './cash-book.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class CashBookService {

    private resourceUrl = 'api/cash-books';
    private resourceSearchUrl = 'api/_search/cash-books';

    constructor(private http: Http, private dateUtils: DateUtils) { }

    create(cashBook: CashBook): Observable<CashBook> {
        const copy = this.convert(cashBook);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(cashBook: CashBook): Observable<CashBook> {
        const copy = this.convert(cashBook);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<CashBook> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
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
        for (let i = 0; i < jsonResponse.length; i++) {
            this.convertItemFromServer(jsonResponse[i]);
        }
        return new ResponseWrapper(res.headers, jsonResponse);
    }

    private convertItemFromServer(entity: any) {
        entity.relatedDate = this.dateUtils
            .convertLocalDateFromServer(entity.relatedDate);
    }

    private convert(cashBook: CashBook): CashBook {
        const copy: CashBook = Object.assign({}, cashBook);
        copy.relatedDate = this.dateUtils
            .convertLocalDateToServer(cashBook.relatedDate);
        return copy;
    }
}
