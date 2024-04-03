package com.openclassrooms.starterjwt.service;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.openclassrooms.starterjwt.sample.UserSample.oneUserSample;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void testDelete() {
        Long userId = 1L;

        userService.delete(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }

    @Nested
    class FindById {
        @Test
        void testFindById_UserExists() {
            // Arrange
            Long userId = 1L;
            User expectedUser = oneUserSample();
            expectedUser.setId(userId);
            when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

            // Act
            User actualUser = userService.findById(userId);

            // Assert
            assertEquals(expectedUser, actualUser);
            verify(userRepository, times(1)).findById(userId);
        }

        @Test
        void testFindById_UserNotFound() {
            // Arrange
            Long userId = 1L;
            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            // Act
            User actualUser = userService.findById(userId);

            // Assert
            assertEquals(null, actualUser);
            verify(userRepository, times(1)).findById(userId);
        }
    }
}
