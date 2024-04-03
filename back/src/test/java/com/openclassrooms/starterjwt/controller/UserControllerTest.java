package com.openclassrooms.starterjwt.controller;

import com.openclassrooms.starterjwt.controllers.UserController;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import static com.openclassrooms.starterjwt.dto.UserDtoSample.oneUserDtoSample;
import static com.openclassrooms.starterjwt.sample.UserSample.oneUserSample;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private UserController userController;

    @Nested
    class UserById {
        @Test
        void testFindById_UserExists() {
            // Arrange
            Long userId = 1L;
            User user = oneUserSample();
            when(userService.findById(userId)).thenReturn(user); // Mock UserService response
            when(userMapper.toDto(user)).thenReturn(oneUserDtoSample()); // Mock UserMapper response

            // Act
            ResponseEntity<?> response = userController.findById(userId.toString());

            // Assert
            assertEquals(200, response.getStatusCodeValue()); // Assuming 200 is the status code for success
            // Add more assertions based on the expected response entity
        }

        @Test
        void testFindById_UserNotFound() {
            // Arrange
            Long userId = 1L;
            when(userService.findById(userId)).thenReturn(null); // Mock UserService response

            // Act
            ResponseEntity<?> response = userController.findById(userId.toString());

            // Assert
            assertEquals(404, response.getStatusCodeValue()); // Assuming 404 is the status code for not found
        }

        @Test
        void testFindById_InvalidId() {
            // Act
            ResponseEntity<?> response = userController.findById("invalidId");

            // Assert
            assertEquals(400, response.getStatusCodeValue()); // Assuming 400 is the status code for bad request
        }
    }

    @Nested
    class DeleteUserById {
        @Test
        void testDeleteUser_UserExistsAndAuthorized() {
            // Arrange
            Long userId = 1L;
            UserDetails userDetails = new UserDetailsImpl(1L, "jean@gmail.com", "jean", "Doe", false, "test123");
            User user = oneUserSample();

            Authentication authentication = mock(Authentication.class);
            when(authentication.getPrincipal()).thenReturn(userDetails);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            SecurityContextHolder.setContext(securityContext);

            when(userService.findById(userId)).thenReturn(user); // User exists
            doNothing().when(userService).delete(userId);

            // Act
            ResponseEntity<?> response = userController.save(userId.toString());

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode()); // Expecting 200 status code
            verify(userService).delete(userId); // Verify that userService.delete was called exactly once
        }

        @Test
        void testDeleteUser_Unauthorized() {
            // Arrange
            Long userId = 1L;
            User user = oneUserSample();
            UserDetails userDetails = new UserDetailsImpl(userId, "test@example.com", "Test", "User", true, "12345678");

            Authentication authentication = mock(Authentication.class);
            when(authentication.getPrincipal()).thenReturn(userDetails);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            SecurityContextHolder.setContext(securityContext);
            when(userService.findById(userId)).thenReturn(user);

            // Act
            ResponseEntity<?> response = userController.save(userId.toString());

            // Assert
            assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
            verify(userService, never()).delete(any());
        }

        @Test
        void testDeleteUser_UserDoesNotExist() {
            // Arrange
            Long userId = 444L;
            when(userService.findById(userId)).thenReturn(null);

            // Act
            ResponseEntity<?> response = userController.save(userId.toString());

            // Assert
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()); // Expecting 404 status code
            verify(userService, never()).delete(any()); // Verify that userService.delete was not called
        }

        @Test
        void testDeleteUser_InvalidId() {
            // Act
            ResponseEntity<?> response = userController.save("invalidId");

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()); // Expecting 400 status code
            verify(userService, never()).delete(any()); // Verify that userService.delete was not called
        }
    }
}
