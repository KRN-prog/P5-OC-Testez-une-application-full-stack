import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import {  FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';

import { FormComponent } from './form.component';
import { ActivatedRoute, Router } from '@angular/router';
import { of } from 'rxjs';

describe('FormComponent', () => {
  let component: FormComponent;
  let formBuilder: FormBuilder;
  let fixture: ComponentFixture<FormComponent>;
  
  let matSnackBar: MatSnackBar;
  let route: ActivatedRoute;
  let sessionApiService: SessionApiService;
  let router: Router;

  const mockSessionService = {
    sessionInformation: {
      admin: true
    }
  } 

  beforeEach(async () => {
    await TestBed.configureTestingModule({

      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule, 
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        SessionApiService
      ],
      declarations: [FormComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });


  it('should submit', () => {
    const spySubmit = jest.spyOn(component, 'submit');

    component.submit();

    expect(spySubmit).toHaveBeenCalled();
  })



  it('should initialize with onUpdate as false and sessionForm as undefined', () => {
    expect(component.onUpdate).toBe(false);
    expect(component.sessionForm).toBeUndefined();
  });

  it('should navigate to "/sessions" if sessionInformation is not admin', () => {
    (mockSessionService.sessionInformation as any) = { admin: false };

    component.ngOnInit();

    expect(router.navigate).toHaveBeenCalledWith(['/sessions']);
  });

  it('should initialize form if url does not contain "update"', () => {
    spyOn(component as any, 'initForm');

    (route.snapshot.paramMap.get as jasmine.Spy).and.returnValue('');
    (router.url as string) = '/sessions';

    component.ngOnInit();

    expect((component as any).initForm).toHaveBeenCalled();
  });

  it('should initialize form with session data if url contains "update"', () => {
    spyOn(component as any, 'initForm');

    const session = { name: 'Test Session' };
    (route.snapshot.paramMap.get as jasmine.Spy).and.returnValue('1');
    (router.url as string) = '/sessions/update';

    (sessionApiService.detail as jasmine.Spy).and.returnValue(of(session));

    component.ngOnInit();

    expect((component as any).initForm).toHaveBeenCalledWith(session);
  });

  it('should create session when submit is called and onUpdate is false', () => {
    spyOn(component as any, 'exitPage');
    (component.sessionForm as FormGroup) = formBuilder.group({
      name: ['Test Session'],
      date: ['2024-03-06'],
      teacher_id: ['1'],
      description: ['Test description']
    });

    component.submit();

    expect(sessionApiService.create).toHaveBeenCalled();
    expect((component as any).exitPage).toHaveBeenCalledWith('Session created !');
  });

  it('should update session when submit is called and onUpdate is true', () => {
    spyOn(component as any, 'exitPage');
    component.onUpdate = true;
    //component.id = '1';
    (component.sessionForm as FormGroup) = formBuilder.group({
      name: ['Test Session'],
      date: ['2024-03-06'],
      teacher_id: ['1'],
      description: ['Test description']
    });

    component.submit();

    expect(sessionApiService.update).toHaveBeenCalled();
    expect((component as any).exitPage).toHaveBeenCalledWith('Session updated !');
  });

  it('should display message in snackbar and navigate to "/sessions" when exitPage is called', () => {
    matSnackBar = jasmine.createSpyObj('MatSnackBar', ['open']);
    (component as any).exitPage('Test message');

    expect(matSnackBar.open).toHaveBeenCalledWith('Test message', 'Close', { duration: 3000 });
    expect(router.navigate).toHaveBeenCalledWith(['sessions']);
  });
});
