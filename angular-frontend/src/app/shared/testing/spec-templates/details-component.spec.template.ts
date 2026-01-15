/**
 * ⚠️ DETAILS SPEC TEMPLATE
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
import { ActivatedRoute, Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';

import { EntityDetailsComponent } from './entity-details.component';
import { EntityService } from '../entity.service';
import { Entity } from '../entity';


describe('EntityDetailsComponent', () => {
  let component: EntityDetailsComponent;
  let fixture: ComponentFixture<EntityDetailsComponent>;
  let entityService: jasmine.SpyObj<EntityService>;
  let router: Router;

  const mockEntity: Entity = {
    id: 1,
    cpf: '12345678901',
    entityFirstName: 'Renan',
    entityLastName: 'Reginato',
    status: true
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [EntityDetailsComponent],
      providers: [
        { provide: EntityService,
          useValue: {
            getEntityById: jasmine.createSpy('getEntityById')
          }
        },
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: {
                  get: () => '1'
              }
            }
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

    entityService = TestBed.inject(EntityService) as jasmine.SpyObj<EntityService>;
    router = TestBed.inject(Router);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EntityDetailsComponent);
    component = fixture.componentInstance;
  });

  // S - tier
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // S -tier
  it('should load entity by id on init', () => {
    entityService.getEntityById.and.returnValue(of(mockEntity));

    fixture.detectChanges();

    expect(entityService.getEntityById).toHaveBeenCalledWith(1);
    expect(component.entity).toEqual(mockEntity);
  });

  // S -tier
  it('should show http error message on details fail', () => {
    
    const errorResponse = new HttpErrorResponse({
      error: { message: 'Entity not found' },
      status: 404
    });

    (entityService.getEntityById as jasmine.Spy)
      .and.returnValue(throwError(errorResponse));

    fixture.detectChanges();

    expect(component.errorMessage).toBe('Entity not found');
  });

  // B - tier
  it('should navigate back to list', () => {
    component.goToEntityList();

    expect(router.navigate).toHaveBeenCalledWith(['entitys']);
  })

});