package com.openclassrooms.starterjwt.service;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.openclassrooms.starterjwt.sample.SessionSample.*;
import static com.openclassrooms.starterjwt.sample.UserSample.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

public class SessionServiceTest {
    @InjectMocks
    private SessionService sessionService;

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Nested
    class CreateSession {
        @Test
        void shouldCreateSession() throws ParseException {

            Session session = oneSessionSample();
            session.setId(null);

            Session sessionSaved = oneSessionSample();

            LocalDateTime formattedExpected = LocalDateTime.of(2024, 2, 6, 17, 30, 0);

            when(sessionRepository.save(session)).thenReturn(sessionSaved);


            Session createdSession = sessionService.create(session);

            // Verification
            assertNotNull(createdSession);
            assertThat(createdSession.getId()).isEqualTo(1);
            assertThat(createdSession.getName()).isEqualTo("jean");
            assertThat(createdSession.getDate()).isEqualTo(new Date(124, 1, 6, 16, 30, 0));
            assertThat(createdSession.getDescription()).isEqualTo("This is a description");
            assertThat(createdSession.getTeacher()).isEqualTo(session.getTeacher());

            // Test Teacher
            assertThat(createdSession.getTeacher().getId(), equalTo(1L));
            assertThat(createdSession.getTeacher().getFirstName(), equalTo("Jordan"));
            assertThat(createdSession.getTeacher().getLastName(), equalTo("Dupont"));
            assertEquals(createdSession.getTeacher().getCreatedAt(), formattedExpected);
            assertEquals(createdSession.getTeacher().getUpdatedAt(), formattedExpected);

            // Test Users n°1
            assertThat(createdSession.getUsers().get(0).getId(), equalTo(1L));
            assertThat(createdSession.getUsers().get(0).getEmail(), equalTo("jean@gmail.com"));
            assertThat(createdSession.getUsers().get(0).getFirstName(), equalTo("jean"));
            assertThat(createdSession.getUsers().get(0).getLastName(), equalTo("Doe"));
            assertThat(createdSession.getUsers().get(0).getPassword(), equalTo("test123"));
            assertThat(createdSession.getUsers().get(0).isAdmin(), equalTo(false));
            assertEquals(createdSession.getUsers().get(0).getCreatedAt(), formattedExpected);
            assertEquals(createdSession.getUsers().get(0).getUpdatedAt(), formattedExpected);

            // Test Users n°2
            assertThat(createdSession.getUsers().get(1).getId(), equalTo(2L));
            assertThat(createdSession.getUsers().get(1).getEmail(), equalTo("jeanne@gmail.com"));
            assertThat(createdSession.getUsers().get(1).getFirstName(), equalTo("jeanne"));
            assertThat(createdSession.getUsers().get(1).getLastName(), equalTo("Beanns"));
            assertThat(createdSession.getUsers().get(1).getPassword(), equalTo("password123"));
            assertThat(createdSession.getUsers().get(1).isAdmin(), equalTo(true));
            assertEquals(createdSession.getUsers().get(1).getCreatedAt(), formattedExpected);
            assertEquals(createdSession.getUsers().get(1).getUpdatedAt(), formattedExpected);

            assertThat(createdSession.getCreatedAt()).isEqualTo(formattedExpected);
            assertThat(createdSession.getUpdatedAt()).isEqualTo(formattedExpected);
            verify(sessionRepository).save(session);
        }
    }

    @Nested
    class DeleteSession {
        @Test
        void shouldDeleteSession() {

            Long sessionId = 1L;


            sessionService.delete(sessionId);


            verify(sessionRepository).deleteById(sessionId);
        }
    }

    @Nested
    class FindAllSessions {
        @Test
        void shouldFindAllSessions() throws ParseException {
            LocalDateTime formattedExpected = LocalDateTime.of(2024, 2, 6, 17, 30, 0);
            LocalDateTime otherFormattedExpected = LocalDateTime.of(2023, 2, 6, 17, 30, 0);

            List<Session> sessions = List.of(oneSessionSample(), oneOtherSessionSample());
            when(sessionRepository.findAll()).thenReturn(sessions);


            List<Session> sessionResult = sessionService.findAll();
            assertThat(sessionResult.size()).isEqualTo(2);

            // Vérification si la list de session est bien la même que le résultat
            assertEquals(sessions, sessionResult);

            // ========== Test 1ère session ==========
            assertThat(sessionResult.get(0).getId()).isEqualTo(1);
            assertThat(sessionResult.get(0).getName()).isEqualTo("jean");
            assertThat(sessionResult.get(0).getDate()).isEqualTo(new Date(124, 1, 6, 16, 30, 0));
            assertThat(sessionResult.get(0).getDescription()).isEqualTo("This is a description");

            // Test Teacher
            assertThat(sessionResult.get(0).getTeacher().getId(), equalTo(1L));
            assertThat(sessionResult.get(0).getTeacher().getFirstName(), equalTo("Jordan"));
            assertThat(sessionResult.get(0).getTeacher().getLastName(), equalTo("Dupont"));
            assertEquals(sessionResult.get(0).getTeacher().getCreatedAt(), formattedExpected);
            assertEquals(sessionResult.get(0).getTeacher().getUpdatedAt(), formattedExpected);

            // Test Users n°1
            assertThat(sessionResult.get(0).getUsers().get(0).getId(), equalTo(1L));
            assertThat(sessionResult.get(0).getUsers().get(0).getEmail(), equalTo("jean@gmail.com"));
            assertThat(sessionResult.get(0).getUsers().get(0).getFirstName(), equalTo("jean"));
            assertThat(sessionResult.get(0).getUsers().get(0).getLastName(), equalTo("Doe"));
            assertThat(sessionResult.get(0).getUsers().get(0).getPassword(), equalTo("test123"));
            assertThat(sessionResult.get(0).getUsers().get(0).isAdmin(), equalTo(false));
            assertEquals(sessionResult.get(0).getUsers().get(0).getCreatedAt(), formattedExpected);
            assertEquals(sessionResult.get(0).getUsers().get(0).getUpdatedAt(), formattedExpected);

            // Test Users n°2
            assertThat(sessionResult.get(0).getUsers().get(1).getId(), equalTo(2L));
            assertThat(sessionResult.get(0).getUsers().get(1).getEmail(), equalTo("jeanne@gmail.com"));
            assertThat(sessionResult.get(0).getUsers().get(1).getFirstName(), equalTo("jeanne"));
            assertThat(sessionResult.get(0).getUsers().get(1).getLastName(), equalTo("Beanns"));
            assertThat(sessionResult.get(0).getUsers().get(1).getPassword(), equalTo("password123"));
            assertThat(sessionResult.get(0).getUsers().get(1).isAdmin(), equalTo(true));
            assertEquals(sessionResult.get(0).getUsers().get(1).getCreatedAt(), formattedExpected);
            assertEquals(sessionResult.get(0).getUsers().get(1).getUpdatedAt(), formattedExpected);
            // ========== Fin test 1ère session ==========


            // ========== Test 2ème session ==========
            assertThat(sessionResult.get(1).getId()).isEqualTo(2);
            assertThat(sessionResult.get(1).getName()).isEqualTo("jeanne");
            assertThat(sessionResult.get(1).getDate()).isEqualTo(new Date(123, 1, 6, 16, 30, 0));
            assertThat(sessionResult.get(1).getDescription()).isEqualTo("jeanne description");

            // Test Teacher
            assertThat(sessionResult.get(1).getTeacher().getId(), equalTo(1L));
            assertThat(sessionResult.get(1).getTeacher().getFirstName(), equalTo("Adrien"));
            assertThat(sessionResult.get(1).getTeacher().getLastName(), equalTo("Kinda"));
            assertEquals(sessionResult.get(1).getTeacher().getCreatedAt(), otherFormattedExpected);
            assertEquals(sessionResult.get(1).getTeacher().getUpdatedAt(), otherFormattedExpected);

            // Test Users n°1
            assertThat(sessionResult.get(1).getUsers().get(0).getId(), equalTo(4L));
            assertThat(sessionResult.get(1).getUsers().get(0).getEmail(), equalTo("mireille@gmail.com"));
            assertThat(sessionResult.get(1).getUsers().get(0).getFirstName(), equalTo("Mireille"));
            assertThat(sessionResult.get(1).getUsers().get(0).getLastName(), equalTo("Wee"));
            assertThat(sessionResult.get(1).getUsers().get(0).getPassword(), equalTo("mireillePass1"));
            assertThat(sessionResult.get(1).getUsers().get(0).isAdmin(), equalTo(true));
            assertEquals(sessionResult.get(1).getUsers().get(0).getCreatedAt(), otherFormattedExpected);
            assertEquals(sessionResult.get(1).getUsers().get(0).getUpdatedAt(), otherFormattedExpected);

            // Test Users n°2
            assertThat(sessionResult.get(1).getUsers().get(1).getId(), equalTo(6L));
            assertThat(sessionResult.get(1).getUsers().get(1).getEmail(), equalTo("bernard@gmail.com"));
            assertThat(sessionResult.get(1).getUsers().get(1).getFirstName(), equalTo("Bernard"));
            assertThat(sessionResult.get(1).getUsers().get(1).getLastName(), equalTo("Beald"));
            assertThat(sessionResult.get(1).getUsers().get(1).getPassword(), equalTo("BernardBernard123"));
            assertThat(sessionResult.get(1).getUsers().get(1).isAdmin(), equalTo(true));
            assertEquals(sessionResult.get(1).getUsers().get(1).getCreatedAt(), otherFormattedExpected);
            assertEquals(sessionResult.get(1).getUsers().get(1).getUpdatedAt(), otherFormattedExpected);
            // ========== Fin test 2ème session ==========

            verify(sessionRepository).findAll();
        }
    }

    @Nested
    class GetSessionById {
        @Test
        void shouldGetSessionById() throws ParseException {

            Long sessionId = 1L;
            Session session = oneSessionSample();

            LocalDateTime formattedExpected = LocalDateTime.of(2024, 2, 6, 17, 30, 0);

            when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));


            Session result = sessionService.getById(sessionId);

            // Verification
            assertEquals(session, result);
            assertThat(result.getId()).isEqualTo(1);
            assertThat(result.getName()).isEqualTo("jean");
            assertThat(result.getDate()).isEqualTo(new Date(124, 1, 6, 16, 30, 0));
            assertThat(result.getDescription()).isEqualTo("This is a description");
            assertThat(result.getTeacher().getId()).isEqualTo(1L);
            assertThat(result.getTeacher().getLastName()).isEqualTo("Dupont");
            assertThat(result.getTeacher().getFirstName()).isEqualTo("Jordan");
            assertThat(result.getTeacher().getCreatedAt()).isEqualTo(formattedExpected);
            assertThat(result.getTeacher().getUpdatedAt()).isEqualTo(formattedExpected);
            assertThat(result.getUsers().get(0).getId()).isEqualTo(1L);
            assertThat(result.getUsers().get(0).getEmail()).isEqualTo("jean@gmail.com");
            assertThat(result.getUsers().get(0).getLastName()).isEqualTo("Doe");
            assertThat(result.getUsers().get(0).getFirstName()).isEqualTo("jean");
            assertThat(result.getUsers().get(0).getPassword()).isEqualTo("test123");
            assertThat(result.getUsers().get(0).isAdmin()).isEqualTo(false);
            assertThat(result.getUsers().get(0).getCreatedAt()).isEqualTo(formattedExpected);
            assertThat(result.getUsers().get(0).getUpdatedAt()).isEqualTo(formattedExpected);

            assertThat(result.getUsers().get(1).getId()).isEqualTo(2L);
            assertThat(result.getUsers().get(1).getEmail()).isEqualTo("jeanne@gmail.com");
            assertThat(result.getUsers().get(1).getLastName()).isEqualTo("Beanns");
            assertThat(result.getUsers().get(1).getFirstName()).isEqualTo("jeanne");
            assertThat(result.getUsers().get(1).getPassword()).isEqualTo("password123");
            assertThat(result.getUsers().get(1).isAdmin()).isEqualTo(true);
            assertThat(result.getUsers().get(1).getCreatedAt()).isEqualTo(formattedExpected);
            assertThat(result.getUsers().get(1).getUpdatedAt()).isEqualTo(formattedExpected);

            assertThat(result.getCreatedAt()).isEqualTo(formattedExpected);
            assertThat(result.getUpdatedAt()).isEqualTo(formattedExpected);
            verify(sessionRepository).findById(sessionId);
        }
    }


    @Nested
    class UpdateSession {
        @Test
        public void shouldUpdateSession() throws ParseException {

            LocalDateTime formattedExpected = LocalDateTime.of(2024, 2, 6, 17, 30, 0);

            Long sessionId = 1L;
            Session session = oneSessionSample();
            Session updatedSession = oneModifierSessionSample(); // Adjust this based on your Session class

            when(sessionRepository.save(session)).thenReturn(updatedSession);

            Session updateSession = sessionService.update(sessionId, updatedSession);


            assertEquals(sessionId, updateSession.getId());
            assertThat(updateSession).isNotNull();
            // Verification
            assertThat(updateSession.getId()).isEqualTo(1);
            assertThat(updateSession.getName()).isEqualTo("David");
            assertThat(updateSession.getDate()).isEqualTo(new Date(124, 1, 6, 16, 30, 0));
            assertThat(updateSession.getDescription()).isEqualTo("This is a description");
            assertThat(updateSession.getTeacher()).isEqualTo(session.getTeacher());

            // Test Teacher
            assertThat(updateSession.getTeacher().getId(), equalTo(1L));
            assertThat(updateSession.getTeacher().getFirstName(), equalTo("Jordan"));
            assertThat(updateSession.getTeacher().getLastName(), equalTo("Dupont"));
            assertEquals(updateSession.getTeacher().getCreatedAt(), formattedExpected);
            assertEquals(updateSession.getTeacher().getUpdatedAt(), formattedExpected);

            // Test Users n°1
            assertThat(updateSession.getUsers().get(0).getId(), equalTo(1L));
            assertThat(updateSession.getUsers().get(0).getEmail(), equalTo("jean@gmail.com"));
            assertThat(updateSession.getUsers().get(0).getFirstName(), equalTo("jean"));
            assertThat(updateSession.getUsers().get(0).getLastName(), equalTo("Doe"));
            assertThat(updateSession.getUsers().get(0).getPassword(), equalTo("test123"));
            assertThat(updateSession.getUsers().get(0).isAdmin(), equalTo(false));
            assertEquals(updateSession.getUsers().get(0).getCreatedAt(), formattedExpected);
            assertEquals(updateSession.getUsers().get(0).getUpdatedAt(), formattedExpected);

            // Test Users n°2
            assertThat(updateSession.getUsers().get(1).getId(), equalTo(2L));
            assertThat(updateSession.getUsers().get(1).getEmail(), equalTo("jeanne@gmail.com"));
            assertThat(updateSession.getUsers().get(1).getFirstName(), equalTo("jeanne"));
            assertThat(updateSession.getUsers().get(1).getLastName(), equalTo("Beanns"));
            assertThat(updateSession.getUsers().get(1).getPassword(), equalTo("password123"));
            assertThat(updateSession.getUsers().get(1).isAdmin(), equalTo(true));
            assertEquals(updateSession.getUsers().get(1).getCreatedAt(), formattedExpected);
            assertEquals(updateSession.getUsers().get(1).getUpdatedAt(), formattedExpected);

            assertThat(updateSession.getCreatedAt()).isEqualTo(formattedExpected);
            assertThat(updateSession.getUpdatedAt()).isEqualTo(formattedExpected);
        }
    }


    @Nested
    class CreateParticipation {

        @Test
        public void shouldCreateParticipation() {
            // Prepare test data
            Long sessionId = 2L;
            Long userId = 1L;
            Session session = mock(Session.class);
            User user = oneUserSample();

            // Mock repository methods
            when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(session.getUsers()).thenReturn(new ArrayList<>());

            // Call the method under test
            sessionService.participate(sessionId, userId);

            // Verify that the user has been added to the session
            assertTrue(session.getUsers().contains(user));
            verify(sessionRepository, times(1)).save(session);
        }

        @Test
        void testParticipate_SessionNotFound() {
            // Prepare test data
            Long sessionId = 1L;
            Long userId = 1L;

            // Mock repository methods
            when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

            // Call the method under test and assert that it throws NotFoundException
            assertThrows(NotFoundException.class, () -> sessionService.participate(sessionId, userId));
            verify(sessionRepository, never()).save(any());
        }

        @Test
        void testParticipate_UserNotFound() {
            // Prepare test data
            Long sessionId = 1L;
            Long userId = 1L;
            Session session = new Session();

            // Mock repository methods
            when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            // Call the method under test and assert that it throws NotFoundException
            assertThrows(NotFoundException.class, () -> sessionService.participate(sessionId, userId));
            verify(sessionRepository, never()).save(any());
        }

        @Test
        void testParticipate_AlreadyParticipated() throws ParseException {
            // Prepare test data
            Long sessionId = 1L;
            Long userId = 1L;
            Session session = oneSessionSample();
            User user = oneUserSample();
            session.setUsers(List.of(user));

            // Mock repository methods
            when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
            when(userRepository.findById(userId)).thenReturn(Optional.of(user));

            // Call the method under test and assert that it throws BadRequestException
            assertThrows(BadRequestException.class, () -> sessionService.participate(sessionId, userId));
            verify(sessionRepository, never()).save(session);
        }
    }


    @Nested
    class RemoveSessionFromParticipation {
        Long sessionId = 1L;
        Long userId = 1L;

        @Test
        void testNoLongerParticipate_SuccessfulParticipation() throws ParseException {
            Session session = oneSessionSample();
            User user = oneUserSample();
            session.setUsers(List.of(user));

            // Mock repository methods
            when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

            // Call the method under test
            sessionService.noLongerParticipate(sessionId, userId);

            // Verify that the user has been removed from the session
            assertFalse(session.getUsers().contains(user));
            verify(sessionRepository, times(1)).save(session);
        }

        @Test
        void testNoLongerParticipate_SessionNotFound() {
            // Mock repository methods
            when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

            // Call the method under test and assert that it throws NotFoundException
            assertThrows(NotFoundException.class, () -> sessionService.noLongerParticipate(sessionId, userId));
            verify(sessionRepository, never()).save(any());
        }

        @Test
        void testNoLongerParticipate_UserNotParticipate() throws ParseException {
            Long sessionId = 1L;
            Long userId = 1L;
            Session session = oneSessionSample();
            session.setId(sessionId);
            List<User> users = new ArrayList<>();
            User user = oneUserSample();
            user.setId(2L); // Different user ID
            users.add(user);
            session.setUsers(users);
            when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

            // Act & Assert
            assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(sessionId, userId));
            verify(sessionRepository, never()).save(any());
        }
    }

}
