/**
 * ⚠️ UPDATE SPEC TEMPLATE
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
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';

import { UpdateEntityComponent } from './update-entity.component';
import { EntityService } from '../entity.service';
import { Entity } from '../entity';


describe('UpdateEntityComponent', () => {
  let component: UpdateEntityComponent;
  let fixture: ComponentFixture<UpdateEntityComponent>;
  let entityService: EntityService;
  let router: Router;

  const mockEntity: Entity = {
    id: 1
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UpdateEntityComponent ],
      imports: [ FormsModule ],
      providers: [
        {
          provide: EntityService,
          useValue: {
            getEntityById: jasmine.createSpy('getEntityById'),
            updateEntity: jasmine.createSpy('updateEntity')
          }
        },
        {
          provide: Router,
          useValue: {
            navigate: jasmine.createSpy('navigate')
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
        }
      ]
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UpdateEntityComponent);
    component = fixture.componentInstance;
    entityService = TestBed.inject(EntityService);
    router = TestBed.inject(Router);
  });

  // S - tier
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // S - tier
  it('should load entity by id on init', () => {
    (entityService.getEntityById as jasmine.Spy)
      .and.returnValue(of(mockEntity));

    fixture.detectChanges();

    expect(entityService.getEntityById).toHaveBeenCalledWith(1);
    expect(component.entity).toEqual(mockEntity);
  });

  // S - tier
  it('should call service and navigate on update', () => {
    (entityService.getEntityById as jasmine.Spy)
      .and.returnValue(of(mockEntity));

    (entityService.updateEntity as jasmine.Spy)
      .and.returnValue(of({}));

    fixture.detectChanges();

    component.onSubmit();

    expect(entityService.updateEntity).toHaveBeenCalledWith(1, mockEntity);

    expect(router.navigate).toHaveBeenCalledWith(['/entities']);
  });

  // S - tier
  it('should show http error message when update fails', () => {
    (entityService.getEntityById as jasmine.Spy).and.returnValue(of(mockEntity));

    const errorResponse = new HttpErrorResponse({
      error: { message: 'Update failed' },
      status: 400
    });

    (entityService.updateEntity as jasmine.Spy)
      .and.returnValue(throwError(errorResponse));

    fixture.detectChanges();
    component.onSubmit();

    expect(component.errorMessage).toBe('Update failed');
    expect(component.isSubmitting).toBeFalse();
  });

  // B - tier
  it('should disable submit while submitting', () => {
    (entityService.getEntityById as jasmine.Spy).and.returnValue(of(mockEntity));
    component.isSubmitting = true;
    fixture.detectChanges();

    const button = fixture.nativeElement.querySelector('button');

    expect(button.disabled).toBeTrue();
  });


});

