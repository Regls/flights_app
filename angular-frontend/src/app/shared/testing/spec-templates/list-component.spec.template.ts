/**
 * ⚠️ LIST SPEC TEMPLATE
 * ----------------
 * This file is a template and MUST NOT be executed.
 * Do not rename to *.spec.ts
 * 
 * Replace:
 * - Entity -> Client / Airport / Airline / Flight / Booking (entity -> x)
 * - createEntity -> createX
 * - /entitys -> route
 * 
 * - S-tier: core behavior
 * - A-tier: http errors
 * - B-tier: UX behavior
 * - C-tier: fallback / defensive
*/

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { of } from 'rxjs';
import { HttpClientTestingModule } from '@angular/common/http/testing';

import { EntityListComponent } from './entity-list.component';
import { EntityService } from '../entity.service';
import { Entity } from '../entity';


describe('EntityListComponent', () => {
  let component: EntityListComponent;
  let fixture: ComponentFixture<EntityListComponent>;
  let entityService: EntityService;
  let router: Router;

  const mockEntitys: Entity[] = [
    { id: 1 },
    { id: 2 }
  ];

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EntityListComponent ],
      imports: [ HttpEntityTestingModule ],
      providers: [
        {
          provide: EntityService,
          useValue: {
            getEntitys: jasmine.createSpy('getEntitys'),
            deleteEntity: jasmine.createSpy('deleteEntity')
          }
        },
        {
          provide: Router,
          useValue: {
            navigate: jasmine.createSpy('navigate')
          }
        }
      ]
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EntityListComponent);
    component = fixture.componentInstance;
    entityService = TestBed.inject(EntityService);
    router = TestBed.inject(Router);
  });

  // S - tier
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // S - tier
  it('should load entitys on init', () => {
    (entityService.getEntitys as jasmine.Spy).and.returnValue(of(mockEntitys));

    fixture.detectChanges();

    expect(entityService.getEntitys).toHaveBeenCalled();
    expect(component.entitys.length).toBe(2);
    expect(component.entitys).toEqual(mockEntitys);
  });

  // S - tier
  it('should handle http error when service fails', () => {
    const errorResponse = new HttpErrorResponse({
      error: { message: 'Failed to load entitys' },
      status: 500
    });

    (entityService.getEntitys as jasmine.Spy).and.returnValue(throwError(errorResponse));

    fixture.detectChanges();

    expect(component.errorMessage).toBe('Failed to load entitys');
    expect(component.entitys.length).toBe(0);
  });

  // A - tier
  it('should delete entity and reload entities', () => {
    (entityService.deleteEntity as jasmine.Spy).and.returnValue(of({}));
    (entityService.getEntity as jasmine.Spy).and.returnValue(of(mockEntity));

    component.deleteEntity(1);

    expect(entityService.deleteEntity).toHaveBeenCalledWith(1);
    expect(entityService.getEntity).toHaveBeenCalled();
  })

  // A - tier
  it('should handle empty entity list', () => {
    (entityService.getEntitys as jasmine.Spy).and.returnValue(of([]));

    expect(component.entitys.length).toEqual([]);
  });

  // B - tier
  it('should navigate to entity details', () => {
    component.entityDetails(1);

    expect(router.navigate).toHaveBeenCalledWith(['entity-details', 1]);
  });

  // B - tier
  it('should navigate to update entity', () => {
    component.updateEntity(1);

    expect(router.navigate).toHaveBeenCalledWith(['update-entity', 1]);
  });
});

