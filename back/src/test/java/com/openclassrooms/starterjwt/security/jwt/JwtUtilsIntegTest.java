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
            // Mock UserDetailsImpl
            UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
            when(userDetails.getUsername()).thenReturn("testUser");

            // Mock Authentication
            Authentication authentication = mock(Authentication.class);
            when(authentication.getPrincipal()).thenReturn(userDetails);

            // Generate JWT token
            String jwtToken = jwtUtils.generateJwtToken(authentication);

            // Assert that the JWT token is not null
            assertNotNull(jwtToken);

            // You can add more assertions to validate the generated token
        }
    }

    @Nested
    class GetUserNameFromJwtToken {
        @Test
        public void testGetUserNameFromJwtToken_ValidToken_ReturnsUsername() {
            // Generate a valid JWT token
            String username = "testUser";
            String jwtToken = Jwts.builder()
                    .setSubject(username)
                    .signWith(SignatureAlgorithm.HS512, jwtSecret)
                    .compact();

            // Call the method under test
            String extractedUsername = jwtUtils.getUserNameFromJwtToken(jwtToken);

            // Assert the result
            assertEquals(username, extractedUsername);
        }

        @Test
        public void testGetUserNameFromJwtToken_InvalidToken_ThrowsException() {
            // Generate an invalid JWT token (e.g., expired token)
            String invalidToken = "invalidToken";

            // Call the method under test and expect an exception to be thrown
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

            // Generate a valid JWT token
            String username = "testUser";
            String jwtToken = Jwts.builder()
                    .setSubject(username)
                    .signWith(SignatureAlgorithm.HS512, jwtSecret)
                    .compact();

            // Call the method under test
            boolean isValid = jwtUtils.validateJwtToken(jwtToken);

            // Assert the result
            assertTrue(isValid);

            verify(logger, never()).error(anyString(), anyString());
        }

        @Test
        public void testInvalidSignature() {

            // Generate a valid JWT token
            String username = "testUser";
            String jwtToken = Jwts.builder()
                    .setSubject(username)
                    .signWith(SignatureAlgorithm.HS512, jwtSecret)
                    .compact();


            // Arrange
            String invalidTokenWithInvalidSignature = jwtToken + "tampered"; // Invalid signature by tampering
            when(logger.isErrorEnabled()).thenReturn(true);

            // Act
            boolean result = jwtUtils.validateJwtToken(invalidTokenWithInvalidSignature);

            // Assert
            assertFalse(result);
            //verify(logger).error("Invalid JWT signature: {}", "JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted.");
        }

        @Test
        public void testExpiredToken() {

            // Generate an expired JWT token
            String username = "testUser";
            long expiredMillis = System.currentTimeMillis() - 1000; // 1 second ago
            String jwtToken = Jwts.builder()
                    .setSubject(username)
                    .setExpiration(new Date(expiredMillis))
                    .signWith(SignatureAlgorithm.HS512, jwtSecret)
                    .compact();

            // Call the method under test
            boolean isValid = jwtUtils.validateJwtToken(jwtToken);

            // Assert the result
            assertFalse(isValid);
        }

        @Test
        public void testMalformedToken() {

            // Generate a valid JWT token
            String username = "testUser";
            String jwtToken = Jwts.builder()
                    .setSubject(username)
                    .signWith(SignatureAlgorithm.HS512, jwtSecret)
                    .compact();

            // Create a malformed token by removing a period character
            String malformedToken = jwtToken.replaceFirst("\\.", "");

            // Arrange
            when(logger.isErrorEnabled()).thenReturn(true);

            // Act
            boolean result = jwtUtils.validateJwtToken(malformedToken);

            // Assert
            assertFalse(result);
        }

        @Test
        public void testIllegalArgument() {

            // Arrange
            String unsupportedToken = generateUnsupportedToken();
            when(logger.isErrorEnabled()).thenReturn(true);

            // Act
            boolean result = jwtUtils.validateJwtToken(unsupportedToken);

            // Assert
            assertFalse(result);
        }

        private String generateUnsupportedToken() {
            try {
                // Generate an RSA key pair
                KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
                keyPairGenerator.initialize(2048);
                KeyPair keyPair = keyPairGenerator.generateKeyPair();

                // Get the private key for signing
                PrivateKey privateKey = keyPair.getPrivate();

                // Get the public key for verification (not used in this example)
                PublicKey publicKey = keyPair.getPublic();

                // Generate a JWT token with an unsupported algorithm (RS256)
                return Jwts.builder()
                        .setSubject("testUser")
                        .signWith(SignatureAlgorithm.RS256, privateKey)
                        .compact();
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("Failed to generate RSA key pair", e);
            }
        }

        /*@Test
        void testInvalidJwtToken_UnsupportedJwtException() {
            // Arrange
            String invalidToken = "invalid.token.string";

            // Act
            boolean isValid = jwtUtils.validateJwtToken(invalidToken);

            // Assert
            assertFalse(isValid);
            verify(logger).error(startsWith("JWT token is unsupported: "));
        }*/


        /*@Test
        public void testUnsupportedJwtException() {
            String invalidToken = "invalidToken";

            // Act & Assert
            UnsupportedJwtException exception = assertThrows(UnsupportedJwtException.class, () -> jwtUtils.validateJwtToken(invalidToken));
            verify(logger).error("JWT token is unsupported: {}", exception.getMessage());
        }*/

        /*@Test
        public void testValidateJwtToken_ValidToken_ReturnsTrue() {
            // Generate a valid JWT token
            String username = "testUser";
            String jwtToken = Jwts.builder()
                    .setSubject(username)
                    .signWith(SignatureAlgorithm.HS512, jwtSecret)
                    .compact();

            // Call the method under test
            boolean isValid = jwtUtils.validateJwtToken(jwtToken);

            // Assert the result
            assertTrue(isValid);
        }

        @Test
        public void testValidateJwtToken_ExpiredToken_ReturnsFalse() {
            // Generate an expired JWT token
            String username = "testUser";
            long expiredMillis = System.currentTimeMillis() - 1000; // 1 second ago
            String jwtToken = Jwts.builder()
                    .setSubject(username)
                    .setExpiration(new Date(expiredMillis))
                    .signWith(SignatureAlgorithm.HS512, jwtSecret)
                    .compact();

            // Call the method under test
            boolean isValid = jwtUtils.validateJwtToken(jwtToken);

            // Assert the result
            assertFalse(isValid);
        }
        */


    }
}
