import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';

import { CreateAirlineComponent } from './create-airline.component';
import { AirlineService } from '../airline.service';
import { Airline } from '../airline';

describe('CreateAirlineComponent', () => {
  let component: CreateAirlineComponent;
  let fixture: ComponentFixture<CreateAirlineComponent>;
  let airlineService: AirlineService;
  let router: Router;

  const mockAirline: Airline = {
    id: 1,
    iataCode: 'G3',
    airlineName: 'Gol Airlines',
    status: true
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CreateAirlineComponent ],
      imports: [ FormsModule ],
      providers: [
        {
          provide: AirlineService,
          useValue: {
            createAirline: jasmine.createSpy('createAirline')
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
    fixture = TestBed.createComponent(CreateAirlineComponent);
    component = fixture.componentInstance;
    airlineService = TestBed.inject(AirlineService);
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });

  // S - tier
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // S - tier
  it('should call service and navigate on submit', () => {
    (airlineService.createAirline as jasmine.Spy).and.returnValue(of({}));

    component.airline = mockAirline

    component.onSubmit();

    expect(airlineService.createAirline).toHaveBeenCalledWith(mockAirline);
    expect(router.navigate).toHaveBeenCalledWith(['/airlines']);
    expect(component.isSubmitting).toBe(true);
  });

  // S - tier
  it('should show http error message when create fails', () => {
    const errorResponse = new HttpErrorResponse({
      error: { message: 'Airline already exists' },
      status: 400,
      statusText: 'Bad Request'
    });

    (airlineService.createAirline as jasmine.Spy)
      .and.returnValue(throwError(errorResponse));

    component.onSubmit();

    expect(component.errorMessage).toBe('Airline already exists');
    expect(component.isSubmitting).toBe(false);
  });

  // B - tier
  it('should disable submit while submitting', () => {
    component.isSubmitting = true;
    fixture.detectChanges();

    const button = fixture.nativeElement.querySelector('button');

    expect(button.disabled).toBeTrue();
  });

  // C - tier
  it('should show generic error message when service fails', () => {
    (airlineService.createAirline as jasmine.Spy).and.returnValue(throwError({ message: 'Error' }));

    component.onSubmit();

    expect(component.errorMessage).toBe('Error');
    expect(component.isSubmitting).toBe(false);
  });
});
