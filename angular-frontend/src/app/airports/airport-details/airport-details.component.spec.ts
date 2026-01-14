import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute, Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';

import { AirportDetailsComponent } from './airport-details.component';
import { AirportService } from '../airport.service';
import { Airport } from '../airport';


describe('AirportDetailsComponent', () => {
  let component: AirportDetailsComponent;
  let fixture: ComponentFixture<AirportDetailsComponent>;
  let airportService: jasmine.SpyObj<AirportService>;
  let router: Router;

  const mockAirport: Airport = {
    id: 1,
    iataCode: 'GRU',
    airportName: 'Guarulhos International Airport',
    city: 'São Paulo',
    operational: true
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AirportDetailsComponent],
      providers: [
        { provide: AirportService,
          useValue: {
            getAirportById: jasmine.createSpy('getAirportById')
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

    airportService = TestBed.inject(AirportService) as jasmine.SpyObj<AirportService>;
    router = TestBed.inject(Router);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AirportDetailsComponent);
    component = fixture.componentInstance;
  });

  // S - tier
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // S -tier
  it('should load airport by id on init', () => {
    airportService.getAirportById.and.returnValue(of(mockAirport));

    fixture.detectChanges();

    expect(airportService.getAirportById).toHaveBeenCalledWith(1);
    expect(component.airport).toEqual(mockAirport);
  });

  // S -tier
  it('should show http error message on details fail', () => {
    
    const errorResponse = new HttpErrorResponse({
      error: { message: 'Airport not found' },
      status: 404
    });

    (airportService.getAirportById as jasmine.Spy)
      .and.returnValue(throwError(errorResponse));

    fixture.detectChanges();

    expect(component.errorMessage).toBe('Airport not found');
  });

  // B - tier
  it('should navigate back to list', () => {
    component.goToAirportList();

    expect(router.navigate).toHaveBeenCalledWith(['airports']);
  })

});