import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { JPosTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { DesignDetailComponent } from '../../../../../../main/webapp/app/entities/design/design-detail.component';
import { DesignService } from '../../../../../../main/webapp/app/entities/design/design.service';
import { Design } from '../../../../../../main/webapp/app/entities/design/design.model';

describe('Component Tests', () => {

    describe('Design Management Detail Component', () => {
        let comp: DesignDetailComponent;
        let fixture: ComponentFixture<DesignDetailComponent>;
        let service: DesignService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [JPosTestModule],
                declarations: [DesignDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    DesignService,
                    EventManager
                ]
            }).overrideComponent(DesignDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(DesignDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DesignService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Design(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.design).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
