package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.Test;
import java.util.Collection;
import java.util.HashSet;
import static org.assertj.core.api.Assertions.assertThat;

public class UserDetailsImplTest {

    @Test
    public void testUserDetailsBuilder() {
        // Arrange
        Long id = 1L;
        String username = "john_doe";
        String firstName = "John";
        String lastName = "Doe";
        Boolean admin = true;
        String password = "password";

        // Act
        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(id)
                .username(username)
                .firstName(firstName)
                .lastName(lastName)
                .admin(admin)
                .password(password)
                .build();

        // Assert
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getId()).isEqualTo(id);
        assertThat(userDetails.getUsername()).isEqualTo(username);
        assertThat(userDetails.getFirstName()).isEqualTo(firstName);
        assertThat(userDetails.getLastName()).isEqualTo(lastName);
        assertThat(userDetails.getAdmin()).isEqualTo(admin);
        assertThat(userDetails.getPassword()).isEqualTo(password);
    }

    /*@Test
    public void testDefaultValues() {
        // Act
        UserDetailsImpl userDetails = UserDetailsImpl.builder().build();

        // Assert
        assertThat(userDetails.getId()).isNull();
        assertThat(userDetails.getUsername()).isNull();
        assertThat(userDetails.getFirstName()).isNull();
        assertThat(userDetails.getLastName()).isNull();
        assertThat(userDetails.getAdmin()).isFalse();
        assertThat(userDetails.getPassword()).isNull();
    }

    @Test
    public void testEqualsAndHashCode() {
        // Arrange
        Long id1 = 1L;
        Long id2 = 2L;

        UserDetailsImpl user1 = UserDetailsImpl.builder().id(id1).build();
        UserDetailsImpl user2 = UserDetailsImpl.builder().id(id1).build();
        UserDetailsImpl user3 = UserDetailsImpl.builder().id(id2).build();

        // Assert
        assertThat(user1).isEqualTo(user2); // Test equals()
        assertThat(user1).hasSameHashCodeAs(user2); // Test hashCode()
        assertThat(user1).isNotEqualTo(user3); // Test equals()
        assertThat(user1.hashCode()).isNotEqualTo(user3.hashCode()); // Test hashCode()
    }*/
}

