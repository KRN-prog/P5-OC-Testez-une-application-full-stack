package com.openclassrooms.starterjwt.payload;

public class SignupRequestTest {
    public static SignupRequest oneSignUpRequest() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("herve@gmail.com");
        signupRequest.setFirstName("herve");
        signupRequest.setLastName("Dooe");
        signupRequest.setPassword("motDePasse123");

        return signupRequest;
    }
}
