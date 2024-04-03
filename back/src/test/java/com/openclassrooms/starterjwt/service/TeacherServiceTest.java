package com.openclassrooms.starterjwt.service;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.openclassrooms.starterjwt.sample.TeacherSample.oneOhterTeacherSample;
import static com.openclassrooms.starterjwt.sample.TeacherSample.oneTeacherSample;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceTest {
    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    @Nested
    class FindAllTeachers {
        @Test
        void testFindAll() {
            // Arrange
            List<Teacher> expectedTeachers = List.of(oneTeacherSample(), oneOhterTeacherSample());
            when(teacherRepository.findAll()).thenReturn(expectedTeachers);

            // Act
            List<Teacher> actualTeachers = teacherService.findAll();
            assertThat(actualTeachers.size()).isEqualTo(2);

            // Assert
            assertEquals(expectedTeachers.size(), actualTeachers.size());
            for (int i = 0; i < expectedTeachers.size(); i++) {
                assertEquals(expectedTeachers.get(i), actualTeachers.get(i));
            }
            verify(teacherRepository).findAll();
        }
    }

    @Nested
    class FindTeacherById {
        @Test
        void testFindById_TeacherExists() {
            // Arrange
            Long teacherId = 1L;
            Teacher expectedTeacher = oneTeacherSample();
            when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(expectedTeacher));

            // Act
            Teacher actualTeacher = teacherService.findById(teacherId);

            // Assert
            assertEquals(expectedTeacher, actualTeacher);
        }

        @Test
        void testFindById_TeacherNotFound() {
            // Arrange
            Long teacherId = 1L;
            when(teacherRepository.findById(teacherId)).thenReturn(Optional.empty());

            // Act
            Teacher actualTeacher = teacherService.findById(teacherId);

            // Assert
            assertEquals(null, actualTeacher);
        }
    }
}
