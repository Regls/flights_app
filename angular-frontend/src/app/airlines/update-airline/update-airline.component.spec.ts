import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';

import { UpdateAirlineComponent } from './update-airline.component';
import { AirlineService } from '../airline.service';
import { Airline } from '../airline';


describe('UpdateAirlineComponent', () => {
  let component: UpdateAirlineComponent;
  let fixture: ComponentFixture<UpdateAirlineComponent>;
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
      declarations: [ UpdateAirlineComponent ],
      imports: [ FormsModule ],
      providers: [
        {
          provide: AirlineService,
          useValue: {
            getAirlineById: jasmine.createSpy('getAirlineById'),
            updateAirline: jasmine.createSpy('updateAirline')
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
    fixture = TestBed.createComponent(UpdateAirlineComponent);
    component = fixture.componentInstance;
    airlineService = TestBed.inject(AirlineService);
    router = TestBed.inject(Router);
  });

  // S - tier
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // S - tier
  it('should load airline by id on init', () => {
    (airlineService.getAirlineById as jasmine.Spy).and.returnValue(of(mockAirline));

    fixture.detectChanges();

    expect(airlineService.getAirlineById).toHaveBeenCalledWith(1);
    expect(component.airline).toEqual(mockAirline);
  });

  // S - tier
  it('should call service and navigate on update', () => {
    (airlineService.getAirlineById as jasmine.Spy).and.returnValue(of(mockAirline));

    (airlineService.updateAirline as jasmine.Spy).and.returnValue(of({}));

    fixture.detectChanges();

    component.onSubmit();

    expect(airlineService.updateAirline).toHaveBeenCalledWith(1, mockAirline);
    expect(router.navigate).toHaveBeenCalledWith(['/airlines']);
    expect(component.isSubmitting).toBe(true);
  });

  // S - tier
  it('should show http error message when update fails', () => {
    (airlineService.getAirlineById as jasmine.Spy).and.returnValue(of(mockAirline));

    const errorResponse = new HttpErrorResponse({
      error: { message: 'Update failed' },
      status: 400
    });

    (airlineService.updateAirline as jasmine.Spy)
      .and.returnValue(throwError(errorResponse));

    fixture.detectChanges();
    component.onSubmit();

    expect(component.errorMessage).toBe('Update failed');
    expect(component.isSubmitting).toBeFalse();
  });

  // B - tier
  it('should disable submit while submitting', () => {
    (airlineService.getAirlineById as jasmine.Spy).and.returnValue(of(mockAirline));
    component.isSubmitting = true;
    fixture.detectChanges();

    const button = fixture.nativeElement.querySelector('button');

    expect(button.disabled).toBeTrue();
  });
});

