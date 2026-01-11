import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ClientListComponent } from './client-list.component';
import { ClientService } from '../client.service';
import { Router } from '@angular/router';
import { of } from 'rxjs';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Client } from '../client';


describe('ClientListComponent', () => {
  let component: ClientListComponent;
  let fixture: ComponentFixture<ClientListComponent>;
  let clientService: ClientService;
  let router: Router;

  const mockClients: Client[] = [
    { id: 1, cpf: '12345678901', clientFirstName: 'Renan', clientLastName: 'Reginato', status: true},
    { id: 2, cpf: '98765432101', clientFirstName: 'Jane', clientLastName: 'Silver', status: false}
  ];

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ClientListComponent ],
      imports: [ HttpClientTestingModule ],
      providers: [
        ClientService,
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
    fixture.detectChanges();
  });

  //test creation
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  //test ngOnInit and getClients
  it('should load clients on init', () => {
    spyOn(clientService, 'getClients').and.returnValue(of(mockClients));

    component.ngOnInit();

    expect(clientService.getClients).toHaveBeenCalled();
    expect(component.clients.length).toBe(2);
    expect(component.clients[0].clientFirstName).toBe('Renan');
  });

  //test clientDetails
  it('should navigate to client details', () => {
    const clientId = 1;
    component.clientDetails(clientId);

    expect(router.navigate).toHaveBeenCalledWith(['client-details', clientId]);
  });

  //test updateClient
  it('should navigate to update client', () => {
    const clientId = 1;
    component.updateClient(clientId);

    expect(router.navigate).toHaveBeenCalledWith(['update-client', clientId]);
  });

  //test deleteClient
  it('should delete client and reload clients', () => {
    const clientId = 1;
    spyOn(clientService, 'deleteClient').and.returnValue(of({}));
    spyOn(clientService, 'getClients').and.returnValue(of(mockClients));

    component.deleteClient(clientId);

    expect(clientService.deleteClient).toHaveBeenCalledWith(clientId);
    expect(clientService.getClients).toHaveBeenCalled();
  });

  //test html
  it('should render clients in the table', () => {
    spyOn(clientService, 'getClients').and.returnValue(of(mockClients));
    
    component.ngOnInit();
    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;
    const rows = compiled.querySelectorAll('tbody tr');


    expect(rows.length).toBe(2);
    expect(rows[0].textContent).toContain('Renan');
    expect(rows[1].textContent).toContain('Jane');
  });

  //test button details
  it('should call clientDetails when details button is clicked', () => {
    spyOn(component, 'clientDetails');

    component.clients = mockClients;
    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;
    const button = compiled.querySelector('.details-btn') as HTMLButtonElement;

    button.click();

    expect(component.clientDetails).toHaveBeenCalledWith(1);
  });

  //test button update
  it('should call updateClient when update button is clicked', () => {
    spyOn(component, 'updateClient');

    component.clients = mockClients;
    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;
    const button = compiled.querySelector('.update-btn') as HTMLButtonElement;

    button.click();

    expect(component.updateClient).toHaveBeenCalledWith(1);
  });

  //test button delete
  it('should call deleteClient when delete button is clicked', () => {
    spyOn(component, 'deleteClient');

    component.clients = mockClients;
    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;
    const button = compiled.querySelector('.delete-btn') as HTMLButtonElement;

    button.click();

    expect(component.deleteClient).toHaveBeenCalledWith(1);
  });

  //test html navigation with details button
  it('should navigate when clicking details button', () => {
    component.clients = mockClients;
    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;
    const button = compiled.querySelector('.details-btn') as HTMLButtonElement;

    button.click();

    expect(router.navigate).toHaveBeenCalledWith(['client-details', 1]);
  });

  //test html navigation with update button
  it('should navigate when clicking update button', () => {
    component.clients = mockClients;
    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;
    const button = compiled.querySelector('.update-btn') as HTMLButtonElement;

    button.click();

    expect(router.navigate).toHaveBeenCalledWith(['update-client', 1]);
  });

  //test html navigation with delete button (return to list itself)
  it('should call service and reload clients when delete button is clicked', () => {
  spyOn(clientService, 'deleteClient').and.returnValue(of({}));
  spyOn(clientService, 'getClients').and.returnValue(of(mockClients));

  component.clients = mockClients;
  fixture.detectChanges();

  const compiled = fixture.nativeElement as HTMLElement;
  const button = compiled.querySelector('.delete-btn') as HTMLButtonElement;

  button.click();

  expect(clientService.deleteClient).toHaveBeenCalledWith(1);
  expect(clientService.getClients).toHaveBeenCalled();
});
});
