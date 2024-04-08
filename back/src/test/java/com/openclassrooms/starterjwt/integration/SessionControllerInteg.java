package com.openclassrooms.starterjwt.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import java.util.Arrays;
import java.util.List;

import static com.openclassrooms.starterjwt.dto.SessionDtoSample.*;
import static com.openclassrooms.starterjwt.sample.SessionSample.oneOtherSessionSample;
import static com.openclassrooms.starterjwt.sample.SessionSample.oneSessionSample;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class SessionControllerInteg {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SessionService sessionService;

    @MockBean
    private SessionMapper sessionMapper;

    @Nested
    class FinfSessionById {
        @Test
        @WithMockUser
        public void testFindById_SessionExists() throws Exception {
            Session session = oneSessionSample();
            session.setId(1L);
            SessionDto sessionDto = oneSessionDtoSample();
            sessionDto.setId(1L);

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());


            when(sessionService.getById(1L)).thenReturn(session);
            when(sessionMapper.toDto(session)).thenReturn(sessionDto);

            mockMvc.perform(MockMvcRequestBuilders.get("/api/session/1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(sessionDto)));

            verify(sessionService, times(1)).getById(1L);
            verify(sessionMapper, times(1)).toDto(session);
        }
    }

    @Nested
    class FindAllSessions {
        @Test
        @WithMockUser
        void testFindAll() throws Exception {

            Session session1 = oneSessionSample();
            Session session2 = oneOtherSessionSample();
            List<Session> sessions = Arrays.asList(session1, session2);

            SessionDto sessionDto1 = oneSessionDtoSample();
            SessionDto sessionDto2 = oneOtherSessionDtoSample();
            List<SessionDto> sessionDtos = Arrays.asList(sessionDto1, sessionDto2);

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            when(sessionService.findAll()).thenReturn(sessions);
            when(sessionMapper.toDto(sessions)).thenReturn(sessionDtos);

            mockMvc.perform(MockMvcRequestBuilders.get("/api/session")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(sessionDtos)));
        }
    }


    @Nested
    class CreateNewSession {
        @Test
        @WithMockUser
        void testCreate() throws Exception {

            SessionDto sessionDto = oneSessionDtoSample();
            Session session = oneSessionSample();

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
            when(sessionService.create(any())).thenReturn(session);
            when(sessionMapper.toDto(session)).thenReturn(sessionDto);


            mockMvc.perform(post("/api/session")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(sessionDto)))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("jean"));
        }
    }


    @Nested
    class UpdateSession {
        @Test
        @WithMockUser
        void testUpdateSession() throws Exception {

            SessionDto updatedSessionDto = oneSessionDtoSample();
            updatedSessionDto.setDescription("My description");

            Session updatedSession = oneSessionSample();
            when(sessionService.update(eq(1L), any(Session.class))).thenReturn(updatedSession);
            when(sessionMapper.toDto(any(Session.class))).thenReturn(updatedSessionDto);

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            mockMvc.perform(MockMvcRequestBuilders.put("/api/session/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updatedSessionDto)))
                    .andExpect(status().isOk());
        }
    }


    @Nested
    class DeleteSession {
        @Test
        @WithMockUser
        public void testDeleteSession() throws Exception {

            long sessionId = 1L;

            when(sessionService.getById(sessionId)).thenReturn(oneSessionSample());

            mockMvc.perform(delete("/api/session/1", sessionId))
                    .andExpect(status().isOk());

            verify(sessionService, times(1)).delete(sessionId);
        }
    }

    @Nested
    class PostParticipation {
        @Test
        @WithMockUser
        public void testParticipateInSession() throws Exception {
            // Mock session ID and user ID
            long sessionId = 1L;
            long userId = 2L;

            // Perform the POST request
            mockMvc.perform(post("/api/session/{id}/participate/{userId}", sessionId, userId))
                    .andExpect(status().isOk());

            // Verify that the sessionService.participate method was called with the correct session ID and user ID
            verify(sessionService, times(1)).participate(sessionId, userId);
        }
    }


    @Nested
    class DeleteParticipation {
        @Test
        @WithMockUser
        public void testNoLongerParticipateInSession() throws Exception {
            // Mock session ID and user ID
            long sessionId = 1L;
            long userId = 2L;

            // Perform the DELETE request
            mockMvc.perform(delete("/api/session/{id}/participate/{userId}", sessionId, userId))
                    .andExpect(status().isOk());

            // Verify that the sessionService.noLongerParticipate method was called with the correct session ID and user ID
            verify(sessionService, times(1)).noLongerParticipate(sessionId, userId);
        }
    }
}
