import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager  } from 'ng-jhipster';

import { ContactInfo } from './contact-info.model';
import { ContactInfoService } from './contact-info.service';

@Component({
    selector: 'jhi-contact-info-detail',
    templateUrl: './contact-info-detail.component.html'
})
export class ContactInfoDetailComponent implements OnInit, OnDestroy {

    contactInfo: ContactInfo;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private contactInfoService: ContactInfoService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInContactInfos();
    }

    load(id) {
        this.contactInfoService.find(id).subscribe((contactInfo) => {
            this.contactInfo = contactInfo;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInContactInfos() {
        this.eventSubscriber = this.eventManager.subscribe(
            'contactInfoListModification',
            (response) => this.load(this.contactInfo.id)
        );
    }
}
