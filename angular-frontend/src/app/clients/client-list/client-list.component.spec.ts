import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { HttpErrorResponse } from '@angular/common/http';

import { ClientListComponent } from './client-list.component';
import { ClientService } from '../client.service';
import { ClientResponse } from '../models/client-reponse';


describe('ClientListComponent', () => {
  let component: ClientListComponent;
  let fixture: ComponentFixture<ClientListComponent>;
  let clientService: ClientService;
  let router: Router;

  const mockClients: ClientResponse[] = [
    { id: 1, cpf: '12345678901', firstName: 'Renan', lastName: 'Reginato', active: true},
    { id: 2, cpf: '98765432101', firstName: 'Jane', lastName: 'Silver', active: false}
  ];

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ClientListComponent ],
      imports: [ HttpClientTestingModule ],
      providers: [
        {
          provide: ClientService,
          useValue: {
            getClients: jasmine.createSpy('getClients'),
            deleteClient: jasmine.createSpy('deleteClient')
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
    fixture = TestBed.createComponent(ClientListComponent);
    component = fixture.componentInstance;
    clientService = TestBed.inject(ClientService);
    router = TestBed.inject(Router);
  });

  // S - tier
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // S - tier
  it('should load clients on init', () => {
    (clientService.getClients as jasmine.Spy).and.returnValue(of(mockClients));

    fixture.detectChanges();

    expect(clientService.getClients).toHaveBeenCalled();
    expect(component.clients.length).toBe(2);
    expect(component.clients).toEqual(mockClients);
  });

  // S - tier
  it('should handle http error when service fails', () => {
    const errorResponse = new HttpErrorResponse({
      error: { message: 'Failed to load clients' },
      status: 500
    });

    (clientService.getClients as jasmine.Spy).and.returnValue(throwError(errorResponse));

    fixture.detectChanges();

    expect(component.errorMessage).toBe('Failed to load clients');
    expect(component.clients.length).toBe(0);
  });

  // A - tier
  it('should delete client and reload clients', () => {
    (clientService.deleteClient as jasmine.Spy).and.returnValue(of({}));
    (clientService.getClients as jasmine.Spy).and.returnValue(of(mockClients));

    component.deleteClient(1);

    expect(clientService.deleteClient).toHaveBeenCalledWith(1);
    expect(clientService.getClients).toHaveBeenCalled();
  });

  // A - tier
  it('should handle empty client list', () => {
    (clientService.getClients as jasmine.Spy).and.returnValue(of([]));

    expect(component.clients).toEqual([]);
  });

  // B - tier
  it('should navigate to client details', () => {
    component.clientDetails(1);

    expect(router.navigate).toHaveBeenCalledWith(['client-details', 1]);
  });

  // B - tier
  it('should navigate to update client', () => {
    component.updateClient(1);

    expect(router.navigate).toHaveBeenCalledWith(['update-client', 1]);
  });
});
