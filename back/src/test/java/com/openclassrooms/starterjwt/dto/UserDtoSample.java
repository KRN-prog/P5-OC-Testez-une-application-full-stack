package com.openclassrooms.starterjwt.dto;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class UserDtoSample {
    // Creation d'une clock customisé
    static Clock customClock = Clock.fixed(
            Instant.parse("2024-02-06T16:30:00Z"),
            ZoneId.of("UTC"));
    // Get current date and time using the custom clock
    static LocalDateTime now = LocalDateTime.ofInstant(customClock.instant(), ZoneId.systemDefault());
    public static UserDto oneUserDtoSample() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("jean@gmail.com");
        userDto.setLastName("Doe");
        userDto.setFirstName("jean");
        userDto.setAdmin(false);
        userDto.setPassword("test123");
        userDto.setCreatedAt(now);
        userDto.setUpdatedAt(now);

        return userDto;
    }

    public static UserDto oneModifiedUserDtoSample() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("fred@mail.com");
        userDto.setLastName("Doe");
        userDto.setFirstName("jean");
        userDto.setAdmin(false);
        userDto.setPassword("test123");
        userDto.setCreatedAt(now);
        userDto.setUpdatedAt(now);

        return userDto;
    }
}
