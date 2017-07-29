import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { CashBookBalance } from './cash-book-balance.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class CashBookBalanceService {

    private resourceUrl = 'api/cash-book-balances';
    private resourceSearchUrl = 'api/_search/cash-book-balances';

    constructor(private http: Http) { }

    create(cashBookBalance: CashBookBalance): Observable<CashBookBalance> {
        const copy = this.convert(cashBookBalance);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(cashBookBalance: CashBookBalance): Observable<CashBookBalance> {
        const copy = this.convert(cashBookBalance);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<CashBookBalance> {
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

    private convert(cashBookBalance: CashBookBalance): CashBookBalance {
        const copy: CashBookBalance = Object.assign({}, cashBookBalance);
        return copy;
    }
}
