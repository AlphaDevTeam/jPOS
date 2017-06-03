import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { EventManager  } from 'ng-jhipster';

import { Design } from './design.model';
import { DesignService } from './design.service';

@Component({
    selector: 'jhi-design-detail',
    templateUrl: './design-detail.component.html'
})
export class DesignDetailComponent implements OnInit, OnDestroy {

    design: Design;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: EventManager,
        private designService: DesignService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInDesigns();
    }

    load(id) {
        this.designService.find(id).subscribe((design) => {
            this.design = design;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInDesigns() {
        this.eventSubscriber = this.eventManager.subscribe(
            'designListModification',
            (response) => this.load(this.design.id)
        );
    }
}
