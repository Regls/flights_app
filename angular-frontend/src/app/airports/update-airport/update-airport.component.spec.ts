import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';

import { UpdateAirportComponent } from './update-airport.component';
import { AirportService } from '../airport.service';
import { Airport } from '../airport';


describe('UpdateAirportComponent', () => {
  let component: UpdateAirportComponent;
  let fixture: ComponentFixture<UpdateAirportComponent>;
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
      declarations: [ UpdateAirportComponent ],
      imports: [ FormsModule ],
      providers: [
        {
          provide: AirportService,
          useValue: {
            getAirportById: jasmine.createSpy('getAirportById'),
            updateAirport: jasmine.createSpy('updateAirport')
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
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UpdateAirportComponent);
    component = fixture.componentInstance;
    airportService = TestBed.inject(AirportService);
    router = TestBed.inject(Router);
  });

  // S - tier
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // S - tier
  it('should load airport by id on init', () => {
    (airportService.getAirportById as jasmine.Spy).and.returnValue(of(mockAirport));

    fixture.detectChanges();

    expect(airportService.getAirportById).toHaveBeenCalledWith(1);
    expect(component.airport).toEqual(mockAirport);
  });

  // S - tier
  it('should call service and navigate on update', () => {
    (airportService.getAirportById as jasmine.Spy).and.returnValue(of(mockAirport));

    (airportService.updateAirport as jasmine.Spy).and.returnValue(of({}));

    fixture.detectChanges();

    component.onSubmit();

    expect(airportService.updateAirport).toHaveBeenCalledWith(1, mockAirport);
    expect(router.navigate).toHaveBeenCalledWith(['/airports']);
    expect(component.isSubmitting).toBe(true);
  });

  // S - tier
  it('should show http error message when update fails', () => {
    (airportService.getAirportById as jasmine.Spy).and.returnValue(of(mockAirport));

    const errorResponse = new HttpErrorResponse({
      error: { message: 'Update failed' },
      status: 400
    });

    (airportService.updateAirport as jasmine.Spy)
      .and.returnValue(throwError(errorResponse));

    fixture.detectChanges();
    component.onSubmit();

    expect(component.errorMessage).toBe('Update failed');
    expect(component.isSubmitting).toBeFalse();
  });

  // B - tier
  it('should disable submit while submitting', () => {
    (airportService.getAirportById as jasmine.Spy).and.returnValue(of(mockAirport));
    component.isSubmitting = true;
    fixture.detectChanges();

    const button = fixture.nativeElement.querySelector('button');

    expect(button.disabled).toBeTrue();
  });
});

