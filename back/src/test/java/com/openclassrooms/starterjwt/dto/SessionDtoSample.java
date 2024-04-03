package com.openclassrooms.starterjwt.dto;

import com.openclassrooms.starterjwt.dto.SessionDto;
import lombok.NoArgsConstructor;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class SessionDtoSample {
    // Creation d'une clock customisé
    static Clock customClock = Clock.fixed(
            Instant.parse("2024-02-06T16:30:00Z"),
            ZoneId.of("UTC"));
    // Get current date and time using the custom clock
    static LocalDateTime now = LocalDateTime.ofInstant(customClock.instant(), ZoneId.systemDefault());

    public static SessionDto oneSessionDtoSample() {
        return SessionDto.builder()
                .id(1L)
                .name("jean")
                .date(new Date())
                .teacher_id(2L)
                .description("This is a description")
                .users(List.of(1L,2L,3L))
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    public static SessionDto oneOtherSessionDtoSample() {
        return SessionDto.builder()
                .id(2L)
                .name("jeanne")
                .date(new Date())
                .teacher_id(8L)
                .description("Description de jeanne")
                .users(List.of(6L,7L,8L))
                .createdAt(now)
                .updatedAt(now)
                .build();
    }


    public static SessionDto oneModifiedSessionDtoSample() {
        return SessionDto.builder()
                .id(1L)
                .name("fred")
                .date(new Date())
                .teacher_id(2L)
                .description("This is a description")
                .users(List.of(1L,2L,3L))
                .createdAt(now)
                .updatedAt(now)
                .build();
    }
}
