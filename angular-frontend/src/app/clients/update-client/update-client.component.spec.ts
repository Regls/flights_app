import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute, Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { FormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';

import { UpdateClientComponent } from './update-client.component';
import { ClientService } from '../client.service';
import { Client } from '../client';



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
    (clientService.getClientById as jasmine.Spy).and.returnValue(of(mockClient))
    
    fixture.detectChanges();

    expect(clientService.getClientById).toHaveBeenCalledWith(1);
    expect(component.client).toEqual(mockClient);
  });

  // S - tier
  it('should call service and navigate on update', () => {
    (clientService.getClientById as jasmine.Spy).and.returnValue(of(mockClient));
    (clientService.updateClient as jasmine.Spy).and.returnValue(of({}))

    fixture.detectChanges();

    component.onSubmit();

    expect(clientService.updateClient).toHaveBeenCalledWith(1, mockClient);
    expect(router.navigate).toHaveBeenCalledWith(['/clients']);
    expect(component.isSubmitting).toBe(true);
  });

  // S - tier
  it('should show http error message on update fails', () => {
    (clientService.getClientById as jasmine.Spy).and.returnValue(of(mockClient));
    
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
    (clientService.getClientById as jasmine.Spy).and.returnValue(of(mockClient));
    component.isSubmitting = true;
    fixture.detectChanges();

    const button = fixture.nativeElement.querySelector('button');

    expect(button.disabled).toBeTrue();
  });
});
