import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { DateUtils } from 'ng-jhipster';

import { CashPaymentVoucher } from './cash-payment-voucher.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class CashPaymentVoucherService {

    private resourceUrl = 'api/cash-payment-vouchers';
    private resourceSearchUrl = 'api/_search/cash-payment-vouchers';

    constructor(private http: Http, private dateUtils: DateUtils) { }

    create(cashPaymentVoucher: CashPaymentVoucher): Observable<CashPaymentVoucher> {
        const copy = this.convert(cashPaymentVoucher);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(cashPaymentVoucher: CashPaymentVoucher): Observable<CashPaymentVoucher> {
        const copy = this.convert(cashPaymentVoucher);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<CashPaymentVoucher> {
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
        entity.paymentDate = this.dateUtils
            .convertLocalDateFromServer(entity.paymentDate);
    }

    private convert(cashPaymentVoucher: CashPaymentVoucher): CashPaymentVoucher {
        const copy: CashPaymentVoucher = Object.assign({}, cashPaymentVoucher);
        copy.paymentDate = this.dateUtils
            .convertLocalDateToServer(cashPaymentVoucher.paymentDate);
        return copy;
    }
}
