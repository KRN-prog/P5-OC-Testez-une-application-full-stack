package com.openclassrooms.starterjwt.integration;


import static com.openclassrooms.starterjwt.payload.LoginRequestTest.oneLoginRequest;
import static com.openclassrooms.starterjwt.payload.SignupRequestTest.oneSignUpRequest;
import static com.openclassrooms.starterjwt.sample.UserSample.oneUserSample;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.LoginRequest;
import com.openclassrooms.starterjwt.payload.SignupRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerIntegTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Nested
    class Login {
        @Test
        @WithMockUser
        void testAuthenticateUser() throws Exception {
            UserDetailsImpl userDetails = Mockito.mock(UserDetailsImpl.class);
            when(userDetails.getUsername()).thenReturn("jean@gmail.com");
            when(userDetails.getPassword()).thenReturn("test123");
            when(userDetails.getAuthorities()).thenReturn(Collections.emptyList());

            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);

            when(authenticationManager.authenticate(any(Authentication.class)))
                    .thenReturn(authentication);

            User user = oneUserSample();
            when(userRepository.findByEmail("jean@gmail.com")).thenReturn(Optional.of(user));

            when(jwtUtils.generateJwtToken(authentication)).thenReturn("mocked_jwt_token");

            LoginRequest loginRequest = oneLoginRequest();

            mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(loginRequest)))
                    .andExpect(status().isOk());
        }

        private String asJsonString(final Object obj) {
            try {
                return new ObjectMapper().writeValueAsString(obj);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


    @Nested
    class Register {
        @Test
        public void testRegisterUser_SuccessfulRegistration() throws Exception {

            SignupRequest signupRequest = oneSignUpRequest();

            when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(false);

            when(passwordEncoder.encode(signupRequest.getPassword())).thenReturn("encodedPassword");

            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(signupRequest)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.message").value("User registered successfully!"))
                    .andReturn();
        }

        private String asJsonString(final Object obj) {
            try {
                return new ObjectMapper().writeValueAsString(obj);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
