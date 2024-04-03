import { HttpClientModule } from '@angular/common/http';
import { HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { TeacherService } from './teacher.service';
import { Teacher } from '../interfaces/teacher.interface';

describe('TeacherService', () => {
  let service: TeacherService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientModule
      ]
    });
    service = TestBed.inject(TeacherService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should retrieve teacher by ID', () => {
    const actualDate = new Date();
    const mockTeacher: Teacher = { id: 1, lastName: 'John', firstName: 'Doe', createdAt: actualDate, updatedAt: actualDate };
    const teacherId = '1';
    const url = 'api/teacher';

    service.detail(teacherId).subscribe((teacher: Teacher) => {
      expect(teacher).toBeTruthy();
      expect(teacher.id).toBe('1');
      expect(teacher.lastName).toBe('John');
      expect(teacher.firstName).toBe('Doe');
      expect(teacher.createdAt).toBe(actualDate);
      expect(teacher.updatedAt).toBe(actualDate);
    });

    const req = httpMock.expectOne(`${url}/${teacherId}`);
    expect(req.request.method).toBe('GET');
    req.flush(mockTeacher);
  });
});
