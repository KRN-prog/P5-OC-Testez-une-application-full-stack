package com.openclassrooms.starterjwt.sample;

import com.openclassrooms.starterjwt.models.User;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class UserSample {
    static Clock customClock = Clock.fixed(
            Instant.parse("2024-02-06T16:30:00Z"),
            ZoneId.of("UTC"));
    static LocalDateTime now = LocalDateTime.ofInstant(customClock.instant(), ZoneId.systemDefault());

    public static User oneUserSample() {
        User user = new User();
        user.setId(1L);
        user.setEmail("jean@gmail.com");
        user.setLastName("Doe");
        user.setFirstName("jean");
        user.setPassword("test123");
        user.setAdmin(false);
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        return user;
    }

    public static User oneOtherUserSample() {
        User user = new User();
        user.setId(2L);
        user.setEmail("jeanne@gmail.com");
        user.setLastName("Beanns");
        user.setFirstName("jeanne");
        user.setPassword("password123");
        user.setAdmin(true);
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        return user;
    }


    public static User oneOtherOtherUserSample() {
        User user = new User();
        user.setId(21L);
        user.setEmail("legacy@gmail.com");
        user.setLastName("Beats");
        user.setFirstName("legacy");
        user.setPassword("password123");
        user.setAdmin(true);
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        return user;
    }
}
