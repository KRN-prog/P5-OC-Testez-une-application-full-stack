package com.openclassrooms.starterjwt.controller;

import com.openclassrooms.starterjwt.controllers.SessionController;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import static com.openclassrooms.starterjwt.dto.SessionDtoSample.oneOtherSessionDtoSample;
import static com.openclassrooms.starterjwt.dto.SessionDtoSample.oneSessionDtoSample;
import static com.openclassrooms.starterjwt.sample.SessionSample.oneOtherSessionSample;
import static com.openclassrooms.starterjwt.sample.SessionSample.oneSessionSample;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SessionControllerTest {

    private SessionService sessionService = mock(SessionService.class);
    private SessionMapper sessionMapper = mock(SessionMapper.class);
    private SessionController controller = new SessionController(sessionService, sessionMapper);

    @Nested
    class GetSessionById {
        String id = "1";
        Long sessionId = 1L;
        @Test
        void shouldFindSessionWithValidId() throws ParseException {
            Session session = oneSessionSample();
            SessionDto sessionDto = oneSessionDtoSample();
            when(sessionService.getById(sessionId)).thenReturn(session);
            when(sessionMapper.toDto(session)).thenReturn(sessionDto);

            ResponseEntity<?> responseEntity = controller.findById(id);

            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals(sessionDto, responseEntity.getBody());
        }

        @Test
        void shouldNotFoundByNullId() {
            when(sessionService.getById(sessionId)).thenReturn(null);

            ResponseEntity<?> responseEntity = controller.findById(id);

            assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        }

        @Test
        void shouldNotFoundByInvalidId() {
            String id = "invalidId";

            ResponseEntity<?> responseEntity = controller.findById(id);

            assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        }
    }

    @Nested
    class FindAllSessionController {
        @Test
        public void shouldFindAllSessions() throws ParseException {
            List<Session> sessions = Arrays.asList(oneSessionSample(), oneOtherSessionSample()); // Doit retourner une list de session
            List<SessionDto> sessionDtos = Arrays.asList(oneSessionDtoSample(), oneOtherSessionDtoSample()); // Le mapper doit retourner une liste de sesions

            when(sessionService.findAll()).thenReturn(sessions);

            when(sessionMapper.toDto(sessions)).thenReturn(sessionDtos);

            ResponseEntity<?> responseEntity = controller.findAll();

            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals(sessionDtos, responseEntity.getBody());
        }
    }

    @Nested
    class CreateSessionController {
        @Test
        public void shouldCreateSession() throws ParseException {
            Session session = oneSessionSample();
            session.setId(null);
            Session sessionCreated = oneSessionSample();
            SessionDto sessionDto = oneSessionDtoSample();

            when(sessionService.create(session)).thenReturn(sessionCreated);

            when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
            when(sessionMapper.toDto(session)).thenReturn(sessionDto);

            ResponseEntity<?> responseEntity = controller.create(sessionDto);

            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        }
    }

    @Nested
    class ModifySessionController {
        @Test
        void shouldUpdateSessionById() throws ParseException {
            String id = "1";
            Long sessionId = 1L;
            SessionDto sessionDto = oneSessionDtoSample();
            Session session = oneSessionSample();

            when(sessionService.update(sessionId, session)).thenReturn(session);
            when(sessionMapper.toDto(session)).thenReturn(sessionDto);

            ResponseEntity<?> responseEntity = controller.update(id, sessionDto);

            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        }

        @Test
        void shouldNotUpdateSessionByInvaldId() {
            String id = "invalidId";
            SessionDto sessionDto = oneSessionDtoSample();

            ResponseEntity<?> responseEntity = controller.update(id, sessionDto);

            assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        }
    }

    @Nested
    class DeleteSessionController {
        String id = "1";
        Long sessionId = 1L;

        @Test
        void shouldDeleteById() throws ParseException {
            Session session = oneSessionSample();

            when(sessionService.getById(sessionId)).thenReturn(session);

            ResponseEntity<?> responseEntity = controller.save(id);

            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        }

        @Test
        void shouldNotDeleteByNullId() {
            String id = "1";

            when(sessionService.getById(sessionId)).thenReturn(null);

            ResponseEntity<?> responseEntity = controller.save(id);

            assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        }

        @Test
        void shouldNotDeleteByInvalidId() {
            String id = "invalidId";

            ResponseEntity<?> responseEntity = controller.save(id);

            assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        }
    }

    @Nested
    class CreateNewParticipation {
        @Test
        void shouldCreateNewParticipation() {
            String id = "1";
            String userId = "2";

            ResponseEntity<?> responseEntity = controller.participate(id, userId);

            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        }

        @Test
        void shouldNotCreateParticipationByInvalidId() {
            String id = "invalidId";
            String userId = "2";

            ResponseEntity<?> responseEntity = controller.participate(id, userId);

            assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        }

        @Test
        void shouldNotCreateParticipationByInvalidUserId() {
            String id = "1";
            String userId = "invalidUserId";

            ResponseEntity<?> responseEntity = controller.participate(id, userId);

            assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        }
    }

    @Nested
    class DeleteParticipation {
        @Test
        void shouldDeleteParticipation() {
            String id = "1";
            String userId = "2";

            ResponseEntity<?> responseEntity = controller.noLongerParticipate(id, userId);

            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        }

        @Test
        void shouldNotDeleteParticipationByInvalidId() {
            String id = "invalidId";
            String userId = "2";

            ResponseEntity<?> responseEntity = controller.noLongerParticipate(id, userId);

            assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        }

        @Test
        void shouldNotDeleteParticipationByInvalidUserId() {
            String id = "1";
            String userId = "invalidUserId";

            ResponseEntity<?> responseEntity = controller.noLongerParticipate(id, userId);

            assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        }
    }
}
