import { ComponentFixture, TestBed } from '@angular/core/testing';
import { UpdateClientComponent } from './update-client.component';
import { ClientService } from '../client.service';
import { ActivatedRoute, Router } from '@angular/router';
import { of } from 'rxjs';
import { Client } from '../client';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormsModule } from '@angular/forms';

describe('UpdateClientComponent', () => {
  let component: UpdateClientComponent;
  let fixture: ComponentFixture<UpdateClientComponent>;
  let clientService: ClientService;
  let router: Router;

  const mockClient: Client = {
    id: 1,
    cpf: '12345678901',
    clientFirstName: 'Renan',
    clientLastName: 'Reginato',
    status: true
  }

  beforeEach(async() => {
    await TestBed.configureTestingModule({
      declarations: [ UpdateClientComponent ],
      imports: [ HttpClientTestingModule, FormsModule ],
      providers: [
        {
          provide: ClientService,
          useValue: {
            updateClient: jasmine.createSpy('updateClient')
          }
        },
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              params: { id: 1 }
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
    fixture.detectChanges();
  });

  //test creation (S-tier)
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  //test service save and navigate (S-tier)
  it('should call updateClient and navigate on submit', () => {
    (clientService.updateClient as jasmine.Spy).and.returnValue(of({}))

    component.id = 1;
    component.client = mockClient;

    component.onSubmit();

    expect(clientService.updateClient).toHaveBeenCalledWith(1, mockClient);
    expect(router.navigate).toHaveBeenCalledWith(['/clients']);
  });

  it('should load client by id on init', () => {
    spyOn(clientService, 'getClientById').and.returnValue(of(mockClient))
    
    component.ngOnInit();

    expect(clientService.getClientById).toHaveBeenCalledWith(1);
    expect(component.client.clientFirstName).toEqual('Renan');
  });
});
