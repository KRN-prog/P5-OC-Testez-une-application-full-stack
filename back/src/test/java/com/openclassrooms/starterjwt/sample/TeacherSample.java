package com.openclassrooms.starterjwt.sample;

import com.openclassrooms.starterjwt.models.Teacher;

import java.text.ParseException;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class TeacherSample {
    public static Teacher oneTeacherSample() {
        // Creation d'une clock customisé
        Clock customClock = Clock.fixed(
                Instant.parse("2023-04-06T10:05:00Z"),
                ZoneId.of("UTC"));
        // Get current date and time using the custom clock
        LocalDateTime now = LocalDateTime.ofInstant(customClock.instant(), ZoneId.systemDefault());

        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setLastName("Herbo");
        teacher.setFirstName("Kandle");
        teacher.setCreatedAt(now);
        teacher.setUpdatedAt(now);

        return teacher;
    }

    public static Teacher oneOhterTeacherSample() {
        // Creation d'une clock customisé
        Clock customClock = Clock.fixed(
                Instant.parse("2022-10-06T03:47:00Z"),
                ZoneId.of("UTC"));
        // Get current date and time using the custom clock
        LocalDateTime now = LocalDateTime.ofInstant(customClock.instant(), ZoneId.systemDefault());

        Teacher teacher = new Teacher();
        teacher.setId(2L);
        teacher.setLastName("James");
        teacher.setFirstName("John");
        teacher.setCreatedAt(now);
        teacher.setUpdatedAt(now);

        return teacher;
    }
}
