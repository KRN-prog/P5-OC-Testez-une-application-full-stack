package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;

import java.security.*;
import java.util.Date;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.slf4j.Logger;

@SpringBootTest
@ActiveProfiles("test")
public class JwtUtilsIntegTest {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsService userDetailsService;

    @Captor
    private ArgumentCaptor<String> captor;

    @Mock
    private Logger logger;

    private final String jwtSecret = "openclassrooms";


    @Nested
    class generateJwttoken {
        @Test
        void generateJwtToken_Success() {
            UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
            when(userDetails.getUsername()).thenReturn("testUser");

            Authentication authentication = mock(Authentication.class);
            when(authentication.getPrincipal()).thenReturn(userDetails);

            String jwtToken = jwtUtils.generateJwtToken(authentication);

            assertNotNull(jwtToken);
        }
    }

    @Nested
    class GetUserNameFromJwtToken {
        @Test
        public void testGetUserNameFromJwtToken_ValidToken_ReturnsUsername() {
            String username = "testUser";
            String jwtToken = Jwts.builder()
                    .setSubject(username)
                    .signWith(SignatureAlgorithm.HS512, jwtSecret)
                    .compact();

            String extractedUsername = jwtUtils.getUserNameFromJwtToken(jwtToken);

            assertEquals(username, extractedUsername);
        }

        @Test
        public void testGetUserNameFromJwtToken_InvalidToken_ThrowsException() {
            String invalidToken = "invalidToken";

            assertThrows(
                    MalformedJwtException.class,
                    () -> jwtUtils.getUserNameFromJwtToken(invalidToken)
            );
        }
    }


    @Nested
    class ValidateJwtToken {

        @BeforeEach
        public void setUp() {
            MockitoAnnotations.openMocks(this);
        }

        @Test
        public void testValidToken() {

            String username = "testUser";
            String jwtToken = Jwts.builder()
                    .setSubject(username)
                    .signWith(SignatureAlgorithm.HS512, jwtSecret)
                    .compact();

            boolean isValid = jwtUtils.validateJwtToken(jwtToken);

            assertTrue(isValid);

            verify(logger, never()).error(anyString(), anyString());
        }

        @Test
        public void testInvalidSignature() {

            String username = "testUser";
            String jwtToken = Jwts.builder()
                    .setSubject(username)
                    .signWith(SignatureAlgorithm.HS512, jwtSecret)
                    .compact();


            String invalidTokenWithInvalidSignature = jwtToken + "tampered"; // Invalid signature by tampering
            when(logger.isErrorEnabled()).thenReturn(true);

            boolean result = jwtUtils.validateJwtToken(invalidTokenWithInvalidSignature);

            assertFalse(result);
        }

        @Test
        public void testExpiredToken() {

            String username = "testUser";
            long expiredMillis = System.currentTimeMillis() - 1000; // 1 second ago
            String jwtToken = Jwts.builder()
                    .setSubject(username)
                    .setExpiration(new Date(expiredMillis))
                    .signWith(SignatureAlgorithm.HS512, jwtSecret)
                    .compact();

            boolean isValid = jwtUtils.validateJwtToken(jwtToken);

            assertFalse(isValid);
        }

        @Test
        public void testMalformedToken() {

            String username = "testUser";
            String jwtToken = Jwts.builder()
                    .setSubject(username)
                    .signWith(SignatureAlgorithm.HS512, jwtSecret)
                    .compact();

            String malformedToken = jwtToken.replaceFirst("\\.", "");

            when(logger.isErrorEnabled()).thenReturn(true);

            boolean result = jwtUtils.validateJwtToken(malformedToken);

            assertFalse(result);
        }

        @Test
        public void testIllegalArgument() {

            String unsupportedToken = generateUnsupportedToken();
            when(logger.isErrorEnabled()).thenReturn(true);

            boolean result = jwtUtils.validateJwtToken(unsupportedToken);

            assertFalse(result);
        }

        private String generateUnsupportedToken() {
            try {
                KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
                keyPairGenerator.initialize(2048);
                KeyPair keyPair = keyPairGenerator.generateKeyPair();

                PrivateKey privateKey = keyPair.getPrivate();

                PublicKey publicKey = keyPair.getPublic();

                return Jwts.builder()
                        .setSubject("testUser")
                        .signWith(SignatureAlgorithm.RS256, privateKey)
                        .compact();
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("Failed to generate RSA key pair", e);
            }
        }


    }
}
