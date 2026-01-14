import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute, Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';

import { AirlineDetailsComponent } from './airline-details.component';
import { AirlineService } from '../airline.service';
import { Airline } from '../airline';


fdescribe('AirlineDetailsComponent', () => {
  let component: AirlineDetailsComponent;
  let fixture: ComponentFixture<AirlineDetailsComponent>;
  let airlineService: jasmine.SpyObj<AirlineService>;
  let router: Router;

  const mockAirline: Airline = {
    id: 1,
    iataCode: 'G3',
    airlineName: 'Gol Airlines',
    status: true
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AirlineDetailsComponent],
      providers: [
        { provide: AirlineService,
          useValue: {
            getAirlineById: jasmine.createSpy('getAirlineById')
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

    airlineService = TestBed.inject(AirlineService) as jasmine.SpyObj<AirlineService>;
    router = TestBed.inject(Router);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AirlineDetailsComponent);
    component = fixture.componentInstance;
  });

  // S - tier
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // S -tier
  it('should load airline by id on init', () => {
    airlineService.getAirlineById.and.returnValue(of(mockAirline));

    fixture.detectChanges();

    expect(airlineService.getAirlineById).toHaveBeenCalledWith(1);
    expect(component.airline).toEqual(mockAirline);
  });

  // S -tier
  it('should show http error message on details fail', () => {
    
    const errorResponse = new HttpErrorResponse({
      error: { message: 'Airline not found' },
      status: 404
    });

    (airlineService.getAirlineById as jasmine.Spy)
      .and.returnValue(throwError(errorResponse));

    fixture.detectChanges();

    expect(component.errorMessage).toBe('Airline not found');
  });

  // B - tier
  it('should navigate back to list', () => {
    component.goToAirlineList();

    expect(router.navigate).toHaveBeenCalledWith(['airlines']);
  })

});