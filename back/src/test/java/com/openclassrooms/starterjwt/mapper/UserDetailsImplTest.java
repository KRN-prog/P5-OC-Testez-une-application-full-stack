package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.Test;
import java.util.Collection;
import java.util.HashSet;
import static org.assertj.core.api.Assertions.assertThat;

public class UserDetailsImplTest {

    @Test
    public void testUserDetailsBuilder() {
        Long id = 1L;
        String username = "john_doe";
        String firstName = "John";
        String lastName = "Doe";
        Boolean admin = true;
        String password = "password";

        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(id)
                .username(username)
                .firstName(firstName)
                .lastName(lastName)
                .admin(admin)
                .password(password)
                .build();

        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getId()).isEqualTo(id);
        assertThat(userDetails.getUsername()).isEqualTo(username);
        assertThat(userDetails.getFirstName()).isEqualTo(firstName);
        assertThat(userDetails.getLastName()).isEqualTo(lastName);
        assertThat(userDetails.getAdmin()).isEqualTo(admin);
        assertThat(userDetails.getPassword()).isEqualTo(password);
    }
}

