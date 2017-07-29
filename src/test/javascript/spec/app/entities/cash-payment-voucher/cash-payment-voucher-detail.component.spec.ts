import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { JPosTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { CashPaymentVoucherDetailComponent } from '../../../../../../main/webapp/app/entities/cash-payment-voucher/cash-payment-voucher-detail.component';
import { CashPaymentVoucherService } from '../../../../../../main/webapp/app/entities/cash-payment-voucher/cash-payment-voucher.service';
import { CashPaymentVoucher } from '../../../../../../main/webapp/app/entities/cash-payment-voucher/cash-payment-voucher.model';

describe('Component Tests', () => {

    describe('CashPaymentVoucher Management Detail Component', () => {
        let comp: CashPaymentVoucherDetailComponent;
        let fixture: ComponentFixture<CashPaymentVoucherDetailComponent>;
        let service: CashPaymentVoucherService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [JPosTestModule],
                declarations: [CashPaymentVoucherDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    CashPaymentVoucherService,
                    EventManager
                ]
            }).overrideComponent(CashPaymentVoucherDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CashPaymentVoucherDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CashPaymentVoucherService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new CashPaymentVoucher(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.cashPaymentVoucher).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
