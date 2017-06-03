import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { ContactInfo } from './contact-info.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class ContactInfoService {

    private resourceUrl = 'api/contact-infos';
    private resourceSearchUrl = 'api/_search/contact-infos';

    constructor(private http: Http) { }

    create(contactInfo: ContactInfo): Observable<ContactInfo> {
        const copy = this.convert(contactInfo);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(contactInfo: ContactInfo): Observable<ContactInfo> {
        const copy = this.convert(contactInfo);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<ContactInfo> {
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

    private convert(contactInfo: ContactInfo): ContactInfo {
        const copy: ContactInfo = Object.assign({}, contactInfo);
        return copy;
    }
}
