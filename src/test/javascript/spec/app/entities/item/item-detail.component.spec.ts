import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { JPosTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ItemDetailComponent } from '../../../../../../main/webapp/app/entities/item/item-detail.component';
import { ItemService } from '../../../../../../main/webapp/app/entities/item/item.service';
import { Item } from '../../../../../../main/webapp/app/entities/item/item.model';

describe('Component Tests', () => {

    describe('Item Management Detail Component', () => {
        let comp: ItemDetailComponent;
        let fixture: ComponentFixture<ItemDetailComponent>;
        let service: ItemService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [JPosTestModule],
                declarations: [ItemDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ItemService,
                    EventManager
                ]
            }).overrideComponent(ItemDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ItemDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ItemService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Item(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.item).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
