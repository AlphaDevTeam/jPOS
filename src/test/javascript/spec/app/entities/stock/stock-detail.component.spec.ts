import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { JPosTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { StockDetailComponent } from '../../../../../../main/webapp/app/entities/stock/stock-detail.component';
import { StockService } from '../../../../../../main/webapp/app/entities/stock/stock.service';
import { Stock } from '../../../../../../main/webapp/app/entities/stock/stock.model';

describe('Component Tests', () => {

    describe('Stock Management Detail Component', () => {
        let comp: StockDetailComponent;
        let fixture: ComponentFixture<StockDetailComponent>;
        let service: StockService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [JPosTestModule],
                declarations: [StockDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    StockService,
                    EventManager
                ]
            }).overrideComponent(StockDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(StockDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(StockService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Stock(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.stock).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
