package com.openclassrooms.starterjwt.dto;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class TeacherDtoSample {

    public static TeacherDto oneTeacherDto() {
        // Creation d'une clock customisé
        Clock customClock = Clock.fixed(
                Instant.parse("2023-04-06T10:05:00Z"),
                ZoneId.of("UTC"));
        // Get current date and time using the custom clock
        LocalDateTime now = LocalDateTime.ofInstant(customClock.instant(), ZoneId.systemDefault());

        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId(1L);
        teacherDto.setLastName("Herbo");
        teacherDto.setFirstName("Kandle");
        teacherDto.setCreatedAt(now);
        teacherDto.setUpdatedAt(now);

        return teacherDto;
    }

    public static TeacherDto oneOtherTeacherDto() {
        // Creation d'une clock customisé
        Clock customClock = Clock.fixed(
                Instant.parse("2022-10-06T03:47:00Z"),
                ZoneId.of("UTC"));
        // Get current date and time using the custom clock
        LocalDateTime now = LocalDateTime.ofInstant(customClock.instant(), ZoneId.systemDefault());

        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId(2L);
        teacherDto.setLastName("James");
        teacherDto.setFirstName("John");
        teacherDto.setCreatedAt(now);
        teacherDto.setUpdatedAt(now);

        return teacherDto;
    }
}
