import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute, Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';

import { ClientDetailsComponent } from './client-details.component';
import { ClientService } from '../flight.service';
import { ClientResponse } from '../models/flight-reponse';


describe('ClientDetailsComponent', () => {
  let component: ClientDetailsComponent;
  let fixture: ComponentFixture<ClientDetailsComponent>;
  let clientService: jasmine.SpyObj<ClientService>;
  let router: Router;

  const mockClient: ClientResponse = {
    id: 1,
    cpf: '12345678901',
    firstName: 'Renan',
    lastName: 'Reginato',
    active: true
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ClientDetailsComponent],
      providers: [
        { provide: ClientService,
          useValue: {
            getClientById: jasmine.createSpy('getClientById')
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

    clientService = TestBed.inject(ClientService) as jasmine.SpyObj<ClientService>;
    router = TestBed.inject(Router);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ClientDetailsComponent);
    component = fixture.componentInstance;
  });

  // S - tier
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // S -tier
  it('should load client by id on init', () => {
    clientService.getClientById.and.returnValue(of(mockClient));

    fixture.detectChanges();

    expect(clientService.getClientById).toHaveBeenCalledWith(1);
    expect(component.client).toEqual(mockClient);
  });

  // S -tier
  it('should show http error message on details fail', () => {
    
    const errorResponse = new HttpErrorResponse({
      error: { message: 'Client not found' },
      status: 404
    });

    (clientService.getClientById as jasmine.Spy)
      .and.returnValue(throwError(errorResponse));

    fixture.detectChanges();

    expect(component.errorMessage).toBe('Client not found');
  });

  // B - tier
  it('should navigate back to list', () => {
    component.goToClientList();

    expect(router.navigate).toHaveBeenCalledWith(['clients']);
  })

});