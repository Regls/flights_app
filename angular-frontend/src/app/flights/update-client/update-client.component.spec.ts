import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute, Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { FormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';

import { UpdateClientComponent } from './update-client.component';
import { ClientService } from '../flight.service';
import { ClientResponse } from '../models/flight-reponse';
import { UpdateClientRequest } from '../models/update-client-request';



describe('UpdateClientComponent', () => {
  let component: UpdateClientComponent;
  let fixture: ComponentFixture<UpdateClientComponent>;
  let clientService: ClientService;
  let router: Router;

  const mockClientResponse: ClientResponse = {
    id: 1,
    cpf: '12345678901',
    firstName: 'Renan',
    lastName: 'Reginato',
    active: true
  }

  const mockUpdateClientRequest: UpdateClientRequest = {
    clientFirstName: 'Renan',
    clientLastName: 'Reginato'
  };

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      declarations: [ UpdateClientComponent ],
      imports: [ FormsModule ],
      providers: [
        {
          provide: ClientService,
          useValue: {
            getClientById: jasmine.createSpy('getClientById'),
            updateClient: jasmine.createSpy('updateClient')
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
    fixture = TestBed.createComponent(UpdateClientComponent);
    component = fixture.componentInstance;
    clientService = TestBed.inject(ClientService);
    router = TestBed.inject(Router);
  });

  // S - tier
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // S - tier
  it('should load client by id on init', () => {
    (clientService.getClientById as jasmine.Spy).and.returnValue(of(mockClientResponse))
    
    fixture.detectChanges();

    expect(clientService.getClientById).toHaveBeenCalledWith(1);
    expect(component.client).toEqual(mockUpdateClientRequest);
  });

  // S - tier
  it('should call service and navigate on update', () => {
    (clientService.getClientById as jasmine.Spy).and.returnValue(of(mockClientResponse));
    (clientService.updateClient as jasmine.Spy).and.returnValue(of({}))

    fixture.detectChanges();

    component.onSubmit();

    expect(clientService.updateClient).toHaveBeenCalledWith(1, mockUpdateClientRequest);
    expect(router.navigate).toHaveBeenCalledWith(['/clients']);
    expect(component.isSubmitting).toBe(true);
  });

  // S - tier
  it('should show http error message on update fails', () => {
    (clientService.getClientById as jasmine.Spy).and.returnValue(of(mockClientResponse));
    
    const errorResponse = new HttpErrorResponse({
      error: { message: 'Update failed' },
      status: 400
    });

    (clientService.updateClient as jasmine.Spy)
      .and.returnValue(throwError(errorResponse));

    fixture.detectChanges();
    component.onSubmit();

    expect(component.errorMessage).toBe('Update failed');
    expect(component.isSubmitting).toBeFalse();
  });

  // B - tier
  it('should disable submit while submitting', () => {
    (clientService.getClientById as jasmine.Spy).and.returnValue(of(mockClientResponse));
    component.isSubmitting = true;
    fixture.detectChanges();

    const button = fixture.nativeElement.querySelector('button');

    expect(button.disabled).toBeTrue();
  });
});
