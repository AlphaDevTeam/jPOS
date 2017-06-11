import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { JPosTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { CashBookDetailComponent } from '../../../../../../main/webapp/app/entities/cash-book/cash-book-detail.component';
import { CashBookService } from '../../../../../../main/webapp/app/entities/cash-book/cash-book.service';
import { CashBook } from '../../../../../../main/webapp/app/entities/cash-book/cash-book.model';

describe('Component Tests', () => {

    describe('CashBook Management Detail Component', () => {
        let comp: CashBookDetailComponent;
        let fixture: ComponentFixture<CashBookDetailComponent>;
        let service: CashBookService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [JPosTestModule],
                declarations: [CashBookDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    CashBookService,
                    EventManager
                ]
            }).overrideComponent(CashBookDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CashBookDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CashBookService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new CashBook(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.cashBook).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
