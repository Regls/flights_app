import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';

import { CreateAirportComponent } from './create-airport.component';
import { AirportService } from '../airport.service';
import { Airport } from '../airport';

describe('CreateAirportComponent', () => {
  let component: CreateAirportComponent;
  let fixture: ComponentFixture<CreateAirportComponent>;
  let airportService: AirportService;
  let router: Router;

  const mockAirport: Airport = {
    id: 1,
    iataCode: 'GRU',
    airportName: 'Guarulhos International Airport',
    city: 'Sao Paulo',
    operational: true
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CreateAirportComponent ],
      imports: [ FormsModule ],
      providers: [
        {
          provide: AirportService,
          useValue: {
            createAirport: jasmine.createSpy('createAirport')
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
    fixture = TestBed.createComponent(CreateAirportComponent);
    component = fixture.componentInstance;
    airportService = TestBed.inject(AirportService);
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });

  // S - tier
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // S - tier
  it('should call service and navigate on submit', () => {
    (airportService.createAirport as jasmine.Spy).and.returnValue(of({}));

    component.airport = mockAirport

    component.onSubmit();

    expect(airportService.createAirport).toHaveBeenCalledWith(mockAirport);
    expect(router.navigate).toHaveBeenCalledWith(['/airports']);
    expect(component.isSubmitting).toBe(true);
  });

  // S - tier
  it('should show http error message when create fails', () => {
    const errorResponse = new HttpErrorResponse({
      error: { message: 'Airport already exists' },
      status: 400,
      statusText: 'Bad Request'
    });

    (airportService.createAirport as jasmine.Spy)
      .and.returnValue(throwError(errorResponse));

    component.onSubmit();

    expect(component.errorMessage).toBe('Airport already exists');
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
    (airportService.createAirport as jasmine.Spy).and.returnValue(throwError({ message: 'Error' }));

    component.onSubmit();

    expect(component.errorMessage).toBe('Error');
    expect(component.isSubmitting).toBe(false);
  });
});
