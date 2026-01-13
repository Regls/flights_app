import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { HttpErrorResponse } from '@angular/common/http';

import { AirportListComponent } from './airport-list.component';
import { AirportService } from '../airport.service';
import { Airport } from '../airport';


fdescribe('AirportListComponent', () => {
  let component: AirportListComponent;
  let fixture: ComponentFixture<AirportListComponent>;
  let airportService: AirportService;
  let router: Router;

  const mockAirports: Airport[] = [
    { id: 1, iataCode: 'GRU', airportName: 'Guarulhos International Airport', city: 'São Paulo', operational: true},
    { id: 2, iataCode: 'VCP', airportName: 'Viracopos International Airport', city: 'Campinas', operational: false}
  ];

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AirportListComponent ],
      imports: [ HttpClientTestingModule ],
      providers: [
        {
          provide: AirportService,
          useValue: {
            getAirports: jasmine.createSpy('getAirports'),
            deleteAirport: jasmine.createSpy('deleteAirport')
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
    fixture = TestBed.createComponent(AirportListComponent);
    component = fixture.componentInstance;
    airportService = TestBed.inject(AirportService);
    router = TestBed.inject(Router);
  });

  // S - tier
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // S - tier
  it('should load airports on init', () => {
    (airportService.getAirports as jasmine.Spy).and.returnValue(of(mockAirports));

    fixture.detectChanges();

    expect(airportService.getAirports).toHaveBeenCalled();
    expect(component.airports.length).toBe(2);
    expect(component.airports).toEqual(mockAirports);
  });

  // S - tier
  it('should handle http error when service fails', () => {
    const errorResponse = new HttpErrorResponse({
      error: { message: 'Failed to load airports' },
      status: 500
    });

    (airportService.getAirports as jasmine.Spy).and.returnValue(throwError(errorResponse));

    fixture.detectChanges();

    expect(component.errorMessage).toBe('Failed to load airports');
    expect(component.airports.length).toBe(0);
  });

  // A - tier
  it('should delete airport and reload entities', () => {
    (airportService.deleteAirport as jasmine.Spy).and.returnValue(of({}));
    (airportService.getAirports as jasmine.Spy).and.returnValue(of(mockAirports));

    component.deleteAirport(1);

    expect(airportService.deleteAirport).toHaveBeenCalledWith(1);
    expect(airportService.getAirports).toHaveBeenCalled();
  })

  // A - tier
  it('should handle empty airport list', () => {
    (airportService.getAirports as jasmine.Spy).and.returnValue(of([]));

    expect(component.airports).toEqual([]);
  });

  // B - tier
  it('should navigate to airport details', () => {
    component.airportDetails(1);

    expect(router.navigate).toHaveBeenCalledWith(['airport-details', 1]);
  });

  // B - tier
  it('should navigate to update airport', () => {
    component.updateAirport(1);

    expect(router.navigate).toHaveBeenCalledWith(['update-airport', 1]);
  });
});

