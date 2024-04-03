import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionApiService } from './session-api.service';
import { HttpTestingController } from '@angular/common/http/testing';
import { Session } from '../interfaces/session.interface';
import { Observable, of } from 'rxjs';

describe('SessionsService', () => {
  let service: SessionApiService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientModule
      ]
    });
    service = TestBed.inject(SessionApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should send DELETE request to the correct URL', () => {
    const id = '123';

    service.delete(id).subscribe();

    const req = httpMock.expectOne(`api/session/${id}`); // Adjust the URL according to your application's structure
    expect(req.request.method).toBe('DELETE');
  });

  it('should return an observable with the response', () => {
    const id = '123';

    const testData = { id: '123', message: 'Item deleted successfully' };
    service.delete(id).subscribe(response => {
      expect(response).toEqual(testData);
    });

    const req = httpMock.expectOne(`api/session/${id}`); // Adjust the URL according to your application's structure
    expect(req.request.method).toBe('DELETE');

    req.flush(testData);
  });
});
