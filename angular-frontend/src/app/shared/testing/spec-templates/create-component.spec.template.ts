/**
 * ⚠️ CREATE SPEC TEMPLATE
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
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';

import { CreateEntityComponent } from './create-entity.component';
import { EntityService } from '../entity.service';
import { Entity } from '../entity';

describe('CreateEntityComponent', () => {
  let component: CreateEntityComponent;
  let fixture: ComponentFixture<CreateEntityComponent>;
  let entityService: EntityService;
  let router: Router;

  const mockEntity: Entity = {
    id: 1
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CreateEntityComponent ],
      imports: [ FormsModule ],
      providers: [
        {
          provide: EntityService,
          useValue: {
            createEntity: jasmine.createSpy('createEntity')
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
    fixture = TestBed.createComponent(CreateEntityComponent);
    component = fixture.componentInstance;
    entityService = TestBed.inject(EntityService);
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });

  //test creation (S-tier)
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  //test service save and navigate (S-tier)
  it('should call service and navigate on submit', () => {
    (entityService.createEntity as jasmine.Spy).and.returnValue(of({}));

    component.entity = mockEntity

    component.onSubmit();

    expect(entityService.createEntity).toHaveBeenCalledWith(mockEntity);
    expect(router.navigate).toHaveBeenCalledWith(['/entities']);
    expect(component.isSubmitting).toBe(true);
  });

  //test http error message(A-tier)
  it('should show http error message when service fails', () => {
    const errorResponse = new HttpErrorResponse({
      error: { message: 'Entity already exists' },
      status: 400,
      statusText: 'Bad Request'
    });

    (entityService.createEntity as jasmine.Spy)
      .and.returnValue(throwError(errorResponse));

    component.onSubmit();

    expect(component.errorMessage).toBe('Entity already exists');
    expect(component.isSubmitting).toBe(false);
  });

  //test disabled on btn (B-tier)
  it('should disable submit while submitting', () => {
    component.isSubmitting = true;
    fixture.detectChanges();

    const button = fixture.nativeElement.querySelector('button');

    expect(button.disabled).toBeTrue();
  });

  //test error message (C-tier)
  it('should show generic error message when service fails', () => {
    (entityService.createEntity as jasmine.Spy).and.returnValue(throwError({ message: 'Error' }));

    component.onSubmit();

    expect(component.errorMessage).toBe('Error');
    expect(component.isSubmitting).toBe(false);
  });
});
