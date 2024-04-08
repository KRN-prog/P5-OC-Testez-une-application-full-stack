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
            Long userId = 1L;
            User user = oneUserSample();
            when(userService.findById(userId)).thenReturn(user);
            when(userMapper.toDto(user)).thenReturn(oneUserDtoSample());

            ResponseEntity<?> response = userController.findById(userId.toString());

            assertEquals(200, response.getStatusCodeValue());
        }

        @Test
        void testFindById_UserNotFound() {
            Long userId = 1L;
            when(userService.findById(userId)).thenReturn(null);

            ResponseEntity<?> response = userController.findById(userId.toString());

            assertEquals(404, response.getStatusCodeValue());
        }

        @Test
        void testFindById_InvalidId() {
            ResponseEntity<?> response = userController.findById("invalidId");

            assertEquals(400, response.getStatusCodeValue());
        }
    }

    @Nested
    class DeleteUserById {
        @Test
        void testDeleteUser_UserExistsAndAuthorized() {
            Long userId = 1L;
            UserDetails userDetails = new UserDetailsImpl(1L, "jean@gmail.com", "jean", "Doe", false, "test123");
            User user = oneUserSample();

            Authentication authentication = mock(Authentication.class);
            when(authentication.getPrincipal()).thenReturn(userDetails);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            SecurityContextHolder.setContext(securityContext);

            when(userService.findById(userId)).thenReturn(user);
            doNothing().when(userService).delete(userId);

            ResponseEntity<?> response = userController.save(userId.toString());

            assertEquals(HttpStatus.OK, response.getStatusCode());
            verify(userService).delete(userId);
        }

        @Test
        void testDeleteUser_Unauthorized() {
            Long userId = 1L;
            User user = oneUserSample();
            UserDetails userDetails = new UserDetailsImpl(userId, "test@example.com", "Test", "User", true, "12345678");

            Authentication authentication = mock(Authentication.class);
            when(authentication.getPrincipal()).thenReturn(userDetails);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            SecurityContextHolder.setContext(securityContext);
            when(userService.findById(userId)).thenReturn(user);

            ResponseEntity<?> response = userController.save(userId.toString());

            assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
            verify(userService, never()).delete(any());
        }

        @Test
        void testDeleteUser_UserDoesNotExist() {
            Long userId = 444L;
            when(userService.findById(userId)).thenReturn(null);

            ResponseEntity<?> response = userController.save(userId.toString());

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            verify(userService, never()).delete(any());
        }

        @Test
        void testDeleteUser_InvalidId() {
            ResponseEntity<?> response = userController.save("invalidId");

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            verify(userService, never()).delete(any());
        }
    }
}
