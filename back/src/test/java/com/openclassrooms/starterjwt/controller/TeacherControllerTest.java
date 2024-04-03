package com.openclassrooms.starterjwt.controller;

import com.openclassrooms.starterjwt.controllers.TeacherController;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static com.openclassrooms.starterjwt.dto.TeacherDtoSample.oneOtherTeacherDto;
import static com.openclassrooms.starterjwt.dto.TeacherDtoSample.oneTeacherDto;
import static com.openclassrooms.starterjwt.sample.TeacherSample.oneOhterTeacherSample;
import static com.openclassrooms.starterjwt.sample.TeacherSample.oneTeacherSample;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class TeacherControllerTest {

    @Mock
    private TeacherService teacherService;

    @Mock
    private TeacherMapper teacherMapper;

    @InjectMocks
    private TeacherController teacherController;

    @Test
    void testFindTeacherById_TeacherExists() {
        // Arrange
        Long teacherId = 1L;
        Teacher teacher = oneTeacherSample(); // Create a sample teacher object
        when(teacherService.findById(teacherId)).thenReturn(teacher);
        when(teacherMapper.toDto(teacher)).thenReturn(oneTeacherDto()); // Map teacher to DTO

        // Act
        ResponseEntity<?> response = teacherController.findById(teacherId.toString());

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode()); // Expecting 200 status code
        verify(teacherService, times(1)).findById(teacherId); // Verify that teacherService.findById was called
        verify(teacherMapper, times(1)).toDto(teacher); // Verify that teacherMapper.toDto was called
    }






    @Test
    void testFindTeacherById_TeacherDoesNotExist() {
        // Arrange
        Long teacherId = 1L;
        when(teacherService.findById(teacherId)).thenReturn(null); // Teacher does not exist

        // Act
        ResponseEntity<?> response = teacherController.findById(teacherId.toString());

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()); // Expecting 404 status code
        verify(teacherService, times(1)).findById(teacherId); // Verify that teacherService.findById was called
        verifyNoInteractions(teacherMapper); // Ensure that teacherMapper.toDto was not called
    }

    @Test
    void testFindTeacherById_InvalidId() {
        // Arrange
        String invalidId = "invalidId";

        // Act
        ResponseEntity<?> response = teacherController.findById(invalidId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()); // Expecting 400 status code
        verifyNoInteractions(teacherService); // Ensure that teacherService.findById was not called
        verifyNoInteractions(teacherMapper); // Ensure that teacherMapper.toDto was not called
    }





















    @Test
    void testFindAll_TeachersExist() {
        // Arrange
        List<Teacher> teachers = List.of(oneTeacherSample(), oneOhterTeacherSample()); // Add sample teacher objects
        List<TeacherDto> teachersDto = List.of(oneTeacherDto(), oneOtherTeacherDto()); // Add sample teacher objects
        when(teacherService.findAll()).thenReturn(teachers);
        when(teacherMapper.toDto(teachers)).thenReturn(teachersDto); // Map teachers to DTOs

        // Act
        ResponseEntity<?> response = teacherController.findAll();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode()); // Expecting 200 status code
        verify(teacherService, times(1)).findAll(); // Verify that teacherService.findAll was called
        verify(teacherMapper, times(1)).toDto(teachers); // Verify that teacherMapper.toDto was called
    }
}
