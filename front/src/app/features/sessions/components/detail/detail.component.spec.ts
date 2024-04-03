import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule, } from '@angular/router/testing';
import { expect } from '@jest/globals'; 
import { SessionService } from '../../../../services/session.service';

import { DetailComponent } from './detail.component';
import { SessionApiService } from '../../services/session-api.service';


describe('DetailComponent', () => {
  let component: DetailComponent;
  let sessionApiService: SessionApiService;
  let fixture: ComponentFixture<DetailComponent>; 
  let service: SessionService;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule
      ],
      declarations: [DetailComponent], 
      providers: [{ provide: SessionService, useValue: mockSessionService }],
    })
      .compileComponents();
      service = TestBed.inject(SessionService);
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    sessionApiService = TestBed.inject<SessionApiService>(SessionApiService);-
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should navigate back when back() is called', () => {
    const historyBackMock = jest.fn();
    window.history.back = historyBackMock;
    component.back();
    expect(historyBackMock).toHaveBeenCalled();
  });

  it('should delete', () => {
    const spyDelete = jest.spyOn(component, 'delete');

    component.delete();

    expect(spyDelete).toHaveBeenCalled();
  })

  it('should participate', () => {
    const spyParticipate = jest.spyOn(component, 'participate');

    component.participate();

    expect(spyParticipate).toHaveBeenCalled();
  })

  it('should unParticipate', () => {
    const spyUnParticipate = jest.spyOn(component, 'unParticipate');

    component.unParticipate();

    expect(spyUnParticipate).toHaveBeenCalled();
  })
});

