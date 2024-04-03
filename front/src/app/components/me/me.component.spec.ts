import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { SessionService } from 'src/app/services/session.service';

import { MeComponent } from './me.component';
import { UserService } from 'src/app/services/user.service';

import { expect } from '@jest/globals';
import { User } from 'src/app/interfaces/user.interface';
import { of } from 'rxjs';
import { Router } from '@angular/router';

describe('MeComponent', () => {
  let component: MeComponent;
  let userService: UserService;
  let fixture: ComponentFixture<MeComponent>;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  }
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [{ provide: SessionService, useValue: mockSessionService }],
    })
      .compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    userService = TestBed.inject<UserService>(UserService);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should been called', () => {
    const spyNgOnInit = jest.spyOn(userService, 'getById');

    component.ngOnInit();

    expect(spyNgOnInit).toHaveBeenCalled();
  })

  it('should navigate back when back() is called', () => {
    const historyBackMock = jest.fn();
    window.history.back = historyBackMock;
    component.back();
    expect(historyBackMock).toHaveBeenCalled();
  });

  it('should delete', () => {
    const spyDelete = jest.spyOn(userService, 'delete');

    component.delete();

    expect(spyDelete).toHaveBeenCalled();
  })
});
