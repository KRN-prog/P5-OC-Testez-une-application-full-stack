import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { LoginComponent } from './login.component';
import { LoginRequest } from '../../interfaces/loginRequest.interface';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { of, throwError } from 'rxjs';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService: AuthService;
  let router: Router;
  let sessionService: SessionService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [SessionService],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule]
    })
      .compileComponents();
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService);
    router = TestBed.inject(Router);
    sessionService = TestBed.inject(SessionService);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });


  it('should submit login form and navigate to /sessions on successful login', () => {
    const loginRequest: LoginRequest = { email: 'test@example.com', password: 'password' };
    const sessionInformation: SessionInformation = {
      token: '',
      type: '',
      id: 0,
      username: '',
      firstName: '',
      lastName: '',
      admin: false
    };
    component.form.setValue(loginRequest);

    const loginSpy = jest.spyOn(authService, 'login').mockReturnValue(of(sessionInformation));
    const logInSpy = jest.spyOn(sessionService, 'logIn');
    const navigateSpy = jest.spyOn(router, 'navigate');

    component.submit();
    expect(loginSpy).toHaveBeenCalledWith(loginRequest);
    expect(logInSpy).toHaveBeenCalledWith(sessionInformation);
    expect(navigateSpy).toHaveBeenCalledWith(['/sessions']);
    expect(component.onError).toBeFalsy();
  });

  it('should set onError flag to true on login error', () => {
    const loginRequest: LoginRequest = { email: 'test@example.com', password: 'password' };
    const error = 'Login failed';
    component.form.setValue(loginRequest);

    const loginSpy = jest.spyOn(authService, 'login').mockReturnValue(throwError(error));

    component.submit();
    expect(loginSpy).toHaveBeenCalledWith(loginRequest);
    expect(component.onError).toBeTruthy();
  });

  it('should not call login method when form is invalid', () => {
    component.form.setValue({ email: '', password: '' });

    const loginSpy = jest.spyOn(authService, 'login');

    component.submit();
    expect(loginSpy).not.toHaveBeenCalled();
  });
});
