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

            // Get la session par id
            ResponseEntity<?> responseEntity = controller.findById(id);

            // Vérification de la réponse
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals(sessionDto, responseEntity.getBody());
        }

        @Test
        void shouldNotFoundByNullId() {
            when(sessionService.getById(sessionId)).thenReturn(null);

            // Get la session par id
            ResponseEntity<?> responseEntity = controller.findById(id);

            // Vérification de la réponse
            assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        }

        @Test
        void shouldNotFoundByInvalidId() {
            // Set un mauvaise id pour retourner une erreur
            String id = "invalidId";

            // Get la session par id invalid
            ResponseEntity<?> responseEntity = controller.findById(id);

            // Vérification de la réponse
            assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        }
    }

    @Nested
    class FindAllSessionController {
        @Test
        public void shouldFindAllSessions() throws ParseException {
            // Définir les données mocké
            List<Session> sessions = Arrays.asList(oneSessionSample(), oneOtherSessionSample()); // Doit retourner une list de session
            List<SessionDto> sessionDtos = Arrays.asList(oneSessionDtoSample(), oneOtherSessionDtoSample()); // Le mapper doit retourner une liste de sesions

            // Quand on cherche tout les session, retourne une liste de session
            when(sessionService.findAll()).thenReturn(sessions);

            // Quand on map notre session, doit retourner une liste de session dto
            when(sessionMapper.toDto(sessions)).thenReturn(sessionDtos);

            // Appel de la méthode
            ResponseEntity<?> responseEntity = controller.findAll();

            // Vérification de si la valeur retourné est OK
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

            // Vérification de si la réponse du body est égale à la liste de sessionsDto
            assertEquals(sessionDtos, responseEntity.getBody());
        }
    }

    @Nested
    class CreateSessionController {
        @Test
        public void shouldCreateSession() throws ParseException {
            // Définition de la valeur des sessions
            Session session = oneSessionSample();
            session.setId(null);
            Session sessionCreated = oneSessionSample();
            SessionDto sessionDto = oneSessionDtoSample();

            // Quand on créer une session, doit retourner une session créé
            when(sessionService.create(session)).thenReturn(sessionCreated);

            // Quand on fait (sessionMapper.toEntity() doit retourner une session
            when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
            // Quand on fait (sessionMapper.toDto() doit retourner une sessionDto
            when(sessionMapper.toDto(session)).thenReturn(sessionDto);

            // Appel de la méthode pour créer une session
            ResponseEntity<?> responseEntity = controller.create(sessionDto);

            // Vérification de si la création nous retourne OK
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        }
    }

    @Nested
    class ModifySessionController {
        @Test
        void shouldUpdateSessionById() throws ParseException {
            // Définition de la valeur des sessions, etc.
            String id = "1";
            Long sessionId = 1L;
            SessionDto sessionDto = oneSessionDtoSample();
            Session session = oneSessionSample();

            // Quand on fait sessionService.update() doit retourner une session
            when(sessionService.update(sessionId, session)).thenReturn(session);
            // Quand on fait sessionMapper.toDto() doit retourner une sessionDto
            when(sessionMapper.toDto(session)).thenReturn(sessionDto);

            // Appel de la méthode pour update via l'id
            ResponseEntity<?> responseEntity = controller.update(id, sessionDto);

            // Vérification de si ça nous retourne OK
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        }

        @Test
        void shouldNotUpdateSessionByInvaldId() {
            // Définition de la valeur de la sessionDto, etc.
            String id = "invalidId";
            SessionDto sessionDto = oneSessionDtoSample();

            // Appel de la méthode pour update via l'id
            ResponseEntity<?> responseEntity = controller.update(id, sessionDto);

            // Vérification de si tout se passe mal
            assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        }
    }

    @Nested
    class DeleteSessionController {
        // Valeur pour la session
        String id = "1";
        Long sessionId = 1L;

        @Test
        void shouldDeleteById() throws ParseException {
            Session session = oneSessionSample();

            // Quand on fait sessionService.getById() doit retourner une session
            when(sessionService.getById(sessionId)).thenReturn(session);

            // Appel de la méthode pour supprimer via l'id
            ResponseEntity<?> responseEntity = controller.save(id);

            // Vérification de si ça retourn OK
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        }

        @Test
        void shouldNotDeleteByNullId() {
            String id = "1";

            // Quand on fait sessionService.getById() doit retourner null
            when(sessionService.getById(sessionId)).thenReturn(null);

            // Appel de la méthode pour supprimer via l'id
            ResponseEntity<?> responseEntity = controller.save(id);

            // Vérification de si tout se passe mal
            assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        }

        @Test
        void shouldNotDeleteByInvalidId() {
            String id = "invalidId";

            // Appel de la méthode pour supprimer via l'id
            ResponseEntity<?> responseEntity = controller.save(id);

            // Vérification de si tout se passe mal
            assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        }
    }

    @Nested
    class CreateNewParticipation {
        @Test
        void shouldCreateNewParticipation() {
            String id = "1";
            String userId = "2";

            // Appel de la méthode pour créer une nouvelle participation
            ResponseEntity<?> responseEntity = controller.participate(id, userId);

            // Vérification de si ça nous retourne OK
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        }

        @Test
        void shouldNotCreateParticipationByInvalidId() {
            String id = "invalidId";
            String userId = "2";

            // Appel de la méthode pour créer une nouvelle participation
            ResponseEntity<?> responseEntity = controller.participate(id, userId);

            // Vérification de si tout ce passe mal
            assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        }

        @Test
        void shouldNotCreateParticipationByInvalidUserId() {
            String id = "1";
            String userId = "invalidUserId";

            // Appel de la méthode pour créer une nouvelle participation
            ResponseEntity<?> responseEntity = controller.participate(id, userId);

            // Vérification de si tout ce passe mal
            assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        }
    }

    @Nested
    class DeleteParticipation {
        @Test
        void shouldDeleteParticipation() {
            String id = "1";
            String userId = "2";

            // Appel de la méthode pour supprimer la participation
            ResponseEntity<?> responseEntity = controller.noLongerParticipate(id, userId);

            // Vérification que ça retourne OK
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        }

        @Test
        void shouldNotDeleteParticipationByInvalidId() {
            String id = "invalidId";
            String userId = "2";

            // Appel de la méthode pour supprimer la participation
            ResponseEntity<?> responseEntity = controller.noLongerParticipate(id, userId);

            // Vérification que tout se passe mal
            assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        }

        @Test
        void shouldNotDeleteParticipationByInvalidUserId() {
            String id = "1";
            String userId = "invalidUserId";

            // Appel de la méthode pour supprimer la participation
            ResponseEntity<?> responseEntity = controller.noLongerParticipate(id, userId);

            // Vérification que tout se passe mal
            assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        }
    }
}
