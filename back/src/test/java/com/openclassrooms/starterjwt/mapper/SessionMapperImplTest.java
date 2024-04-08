package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import static com.openclassrooms.starterjwt.dto.SessionDtoSample.oneOtherSessionDtoSample;
import static com.openclassrooms.starterjwt.dto.SessionDtoSample.oneSessionDtoSample;
import static com.openclassrooms.starterjwt.sample.SessionSample.oneOtherSessionSample;
import static com.openclassrooms.starterjwt.sample.SessionSample.oneSessionSample;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SessionMapperImplTest {
    private SessionMapperImpl sessionMapper = mock(SessionMapperImpl.class);

    @Test
    void testToEntity_WithValidInput() throws ParseException {
        List<Session> sessions = Arrays.asList(oneSessionSample(), oneOtherSessionSample());
        List<SessionDto> sessionsDto = Arrays.asList(oneSessionDtoSample(), oneOtherSessionDtoSample());

        when(sessionMapper.toEntity(sessionsDto)).thenReturn(sessions);

        List<Session> result = sessionMapper.toEntity(sessionsDto);

        assertEquals(sessionsDto.size(), result.size());
        assertThat(sessionsDto.size()).isEqualTo(2);
        assertThat(result.size()).isEqualTo(2);
    }
}
