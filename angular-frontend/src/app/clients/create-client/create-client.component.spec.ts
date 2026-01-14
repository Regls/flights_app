import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';

import { CreateClientComponent } from './create-client.component';
import { ClientService } from '../client.service';
import { Client } from '../client';


fdescribe('CreateClientComponent', () => {
  let component: CreateClientComponent;
  let fixture: ComponentFixture<CreateClientComponent>;
  let clientService: ClientService;
  let router: Router;

  const mockClient: Client = {
    id: 1,
    cpf: '12345678901',
    clientFirstName: 'Renan',
    clientLastName: 'Reginato',
    status: true
  };

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
  });

  // S - tier
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // S - tier
  it('should call service and navigate on submit', () => {
    (clientService.createClient as jasmine.Spy).and.returnValue(of({}));

    component.client = mockClient

    component.onSubmit();

    expect(clientService.createClient).toHaveBeenCalledWith(mockClient);
    expect(router.navigate).toHaveBeenCalledWith(['/clients']);
    expect(component.isSubmitting).toBe(true);
  });

  // S - tier
  it('should show http error message when create fails', () => {
    const errorResponse = new HttpErrorResponse({
      error: { message: 'CPF already exists' },
      status: 400,
      statusText: 'Bad Request'
    });

    (clientService.createClient as jasmine.Spy)
      .and.returnValue(throwError(errorResponse));

    fixture.detectChanges();
    component.onSubmit();

    expect(component.errorMessage).toBe('CPF already exists');
    expect(component.isSubmitting).toBe(false);
  });

  // B - tier
  it('should disable submit while submitting', () => {
    component.isSubmitting = true;
    fixture.detectChanges();

    const button = fixture.nativeElement.querySelector('button');

    expect(button.disabled).toBeTrue();
  });

  // C - tier
  it('should show generic error message when service fails', () => {
    (clientService.createClient as jasmine.Spy).and.returnValue(throwError({ message: 'Error' }));

    component.onSubmit();

    expect(component.errorMessage).toBe('Error');
    expect(component.isSubmitting).toBe(false);
  });
});