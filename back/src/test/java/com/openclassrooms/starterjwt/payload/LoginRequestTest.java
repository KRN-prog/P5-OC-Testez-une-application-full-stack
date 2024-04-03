package com.openclassrooms.starterjwt.payload;

public class LoginRequestTest {
    public static LoginRequest oneLoginRequest() {
        LoginRequest newLoginRequest = new LoginRequest();
        newLoginRequest.setEmail("jean@gmail.com");
        newLoginRequest.setPassword("test123");

        return newLoginRequest;
    }
}
