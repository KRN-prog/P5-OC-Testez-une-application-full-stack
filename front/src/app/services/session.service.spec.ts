import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';

describe('SessionService', () => {
  let service: SessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should initialize with isLogged as false and sessionInformation as undefined', () => {
    expect(service.isLogged).toBe(false);
    expect(service.sessionInformation).toBeUndefined();
  });

  it('should emit false initially from $isLogged observable', (done) => {
    service.$isLogged().subscribe((isLogged) => {
      expect(isLogged).toBe(false);
      done();
    });
  });

  it('should emit true after logIn() method is called', (done) => {
    const sessionInfo = { token: "AzErTy132",
      type: 'unitTestType',
      id: 1,
      username: "User1",
      firstName: "Jean",
      lastName: "Doe",
      admin: false };
    service.logIn(sessionInfo);

    service.$isLogged().subscribe((isLogged) => {
      expect(isLogged).toBe(true);
      done();
    });
  });

  it('should emit false after logOut() method is called', (done) => {
    service.logIn({ token: "AzErTy132",
      type: 'unitTestType',
      id: 1,
      username: "User1",
      firstName: "Jean",
      lastName: "Doe",
      admin: false });
    service.logOut();

    service.$isLogged().subscribe((isLogged) => {
      expect(isLogged).toBe(false);
      done();
    });
  });

  it('should update sessionInformation after logIn() method is called', () => {
    const sessionInfo = { token: "AzErTy132",
      type: 'unitTestType',
      id: 1,
      username: "User1",
      firstName: "Jean",
      lastName: "Doe",
      admin: false };
    service.logIn(sessionInfo);

    expect(service.sessionInformation).toEqual(sessionInfo);
  });

  it('should clear sessionInformation after logOut() method is called', () => {
    service.logIn({ token: "AzErTy132",
      type: 'unitTestType',
      id: 1,
      username: "User1",
      firstName: "Jean",
      lastName: "Doe",
      admin: false });
    service.logOut();

    expect(service.sessionInformation).toBeUndefined();
  });
});
