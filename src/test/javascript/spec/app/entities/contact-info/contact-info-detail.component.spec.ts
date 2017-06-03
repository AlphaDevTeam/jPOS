import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { JPosTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ContactInfoDetailComponent } from '../../../../../../main/webapp/app/entities/contact-info/contact-info-detail.component';
import { ContactInfoService } from '../../../../../../main/webapp/app/entities/contact-info/contact-info.service';
import { ContactInfo } from '../../../../../../main/webapp/app/entities/contact-info/contact-info.model';

describe('Component Tests', () => {

    describe('ContactInfo Management Detail Component', () => {
        let comp: ContactInfoDetailComponent;
        let fixture: ComponentFixture<ContactInfoDetailComponent>;
        let service: ContactInfoService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [JPosTestModule],
                declarations: [ContactInfoDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ContactInfoService,
                    EventManager
                ]
            }).overrideComponent(ContactInfoDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ContactInfoDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ContactInfoService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new ContactInfo(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.contactInfo).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
