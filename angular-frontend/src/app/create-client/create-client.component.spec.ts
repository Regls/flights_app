import { ComponentFixture, TestBed } from '@angular/core/testing'
import { CreateClientComponent } from './create-client.component';
import { ClientService } from '../client.service';
import { Router } from '@angular/router';
import { of } from 'rxjs';
import { FormsModule } from '@angular/forms';

describe('CreateClientComponent', () => {
  let component: CreateClientComponent;
  let fixture: ComponentFixture<CreateClientComponent>;
  let clientService: ClientService;
  let router: Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CreateClientComponent ],
      imports: [ FormsModule ],
      providers: [
        {
          provide: ClientService,
          useValue: {
            createClient: jasmine.createSpy('createClient')
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
    fixture = TestBed.createComponent(CreateClientComponent);
    component = fixture.componentInstance;
    clientService = TestBed.inject(ClientService);
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });

  //test component creation
  it('should create component', () => {
    expect(component).toBeTruthy();
  });

  //test saveClient
  it('should save client and navigate to client list', () => {
    (clientService.createClient as jasmine.Spy).and.returnValue(of({}));
    spyOn(component, 'goToClientList');

    component.saveClient();

    expect(clientService.createClient).toHaveBeenCalled();
    expect(component.goToClientList).toHaveBeenCalled();
  });

  //test goToClientList
  it('should navigate to client list', () => {
    component.goToClientList();

    expect(router.navigate).toHaveBeenCalledWith(['/clients']);
  });

  //test onSubmit
  it('should call saveClient on form submission', () => {
    spyOn(component, 'saveClient');

    component.onSubmit();

    expect(component.saveClient).toHaveBeenCalled();
  });

  it('should call service and navigate on submit', () => {
    (clientService.createClient as jasmine.Spy).and.returnValue(of({}));

    component.client = {
        id: 1,
        cpf: '12345678901',
        clientFirstName: 'Renan',
        clientLastName: 'Reginato',
        status: true
    };
    component.onSubmit();

    expect(clientService.createClient).toHaveBeenCalledWith(component.client);
    expect(router.navigate).toHaveBeenCalledWith(['/clients']);
  });
});