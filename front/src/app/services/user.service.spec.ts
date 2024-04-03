import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { UserService } from './user.service';
import { User } from '../interfaces/user.interface';
import { HttpTestingController } from '@angular/common/http/testing';

describe('UserService', () => {
  let service: UserService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientModule
      ]
    });
    service = TestBed.inject(UserService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should delete teacher by ID', () => {
    const actualDate = new Date();
    const mockUser: User = { id: 1, email: 'johnDoe@mail.com', lastName: 'John', firstName: 'Doe', admin: false, password: 'password123',createdAt: actualDate, updatedAt: actualDate };
    const teacherId = '1';
    const url = 'api/user';

    service.delete(teacherId).subscribe((user : User) => {
      expect(user).toBeTruthy();
      expect(user.id).toBe('1');
      expect(user.email).toBe('johnDoe@mail.com');
      expect(user.lastName).toBe('John');
      expect(user.firstName).toBe('Doe');
      expect(user.admin).toBe(false);
      expect(user.password).toBe('password123');
      expect(user.createdAt).toBe(actualDate);
      expect(user.updatedAt).toBe(actualDate);
    });

    const req = httpMock.expectOne(`${url}/${teacherId}`);
    expect(req.request.method).toBe('DELETE');
    req.flush({});
  });
});
