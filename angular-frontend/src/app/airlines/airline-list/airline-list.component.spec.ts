import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { HttpErrorResponse } from '@angular/common/http';

import { AirlineListComponent } from './airline-list.component';
import { AirlineService } from '../airline.service';
import { Airline } from '../airline';


describe('AirlineListComponent', () => {
  let component: AirlineListComponent;
  let fixture: ComponentFixture<AirlineListComponent>;
  let airlineService: AirlineService;
  let router: Router;

  const mockAirlines: Airline[] = [
    { id: 1, iataCode: 'G3', airlineName: 'Gol Airrlines', status: true},
    { id: 2, iataCode: 'AZ', airlineName: 'Azul Airlines', status: false}
  ];

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AirlineListComponent ],
      imports: [ HttpClientTestingModule ],
      providers: [
        {
          provide: AirlineService,
          useValue: {
            getAirlines: jasmine.createSpy('getAirlines'),
            deleteAirline: jasmine.createSpy('deleteAirline')
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
    fixture = TestBed.createComponent(AirlineListComponent);
    component = fixture.componentInstance;
    airlineService = TestBed.inject(AirlineService);
    router = TestBed.inject(Router);
  });

  // S - tier
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // S - tier
  it('should load airlines on init', () => {
    (airlineService.getAirlines as jasmine.Spy).and.returnValue(of(mockAirlines));

    fixture.detectChanges();

    expect(airlineService.getAirlines).toHaveBeenCalled();
    expect(component.airlines.length).toBe(2);
    expect(component.airlines).toEqual(mockAirlines);
  });

  // S - tier
  it('should handle http error when service fails', () => {
    const errorResponse = new HttpErrorResponse({
      error: { message: 'Failed to load airlines' },
      status: 500
    });

    (airlineService.getAirlines as jasmine.Spy).and.returnValue(throwError(errorResponse));

    fixture.detectChanges();

    expect(component.errorMessage).toBe('Failed to load airlines');
    expect(component.airlines.length).toBe(0);
  });

  // A - tier
  it('should delete airline and reload entities', () => {
    (airlineService.deleteAirline as jasmine.Spy).and.returnValue(of({}));
    (airlineService.getAirlines as jasmine.Spy).and.returnValue(of(mockAirlines));

    component.deleteAirline(1);

    expect(airlineService.deleteAirline).toHaveBeenCalledWith(1);
    expect(airlineService.getAirlines).toHaveBeenCalled();
  })

  // A - tier
  it('should handle empty airline list', () => {
    (airlineService.getAirlines as jasmine.Spy).and.returnValue(of([]));

    expect(component.airlines).toEqual([]);
  });

  // B - tier
  it('should navigate to airline details', () => {
    component.airlineDetails(1);

    expect(router.navigate).toHaveBeenCalledWith(['airline-details', 1]);
  });

  // B - tier
  it('should navigate to update airline', () => {
    component.updateAirline(1);

    expect(router.navigate).toHaveBeenCalledWith(['update-airline', 1]);
  });
});

