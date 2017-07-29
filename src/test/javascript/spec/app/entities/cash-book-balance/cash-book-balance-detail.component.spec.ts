import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { JPosTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { CashBookBalanceDetailComponent } from '../../../../../../main/webapp/app/entities/cash-book-balance/cash-book-balance-detail.component';
import { CashBookBalanceService } from '../../../../../../main/webapp/app/entities/cash-book-balance/cash-book-balance.service';
import { CashBookBalance } from '../../../../../../main/webapp/app/entities/cash-book-balance/cash-book-balance.model';

describe('Component Tests', () => {

    describe('CashBookBalance Management Detail Component', () => {
        let comp: CashBookBalanceDetailComponent;
        let fixture: ComponentFixture<CashBookBalanceDetailComponent>;
        let service: CashBookBalanceService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [JPosTestModule],
                declarations: [CashBookBalanceDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    CashBookBalanceService,
                    EventManager
                ]
            }).overrideComponent(CashBookBalanceDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CashBookBalanceDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CashBookBalanceService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new CashBookBalance(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.cashBookBalance).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
