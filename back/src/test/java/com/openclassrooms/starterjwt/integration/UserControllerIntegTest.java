package com.openclassrooms.starterjwt.integration;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.openclassrooms.starterjwt.dto.UserDtoSample.oneUserDtoSample;
import static com.openclassrooms.starterjwt.sample.UserSample.oneUserSample;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntegTest {

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    void findById_UserExists_ReturnsUserDto() throws Exception {
        User user = oneUserSample();
        user.setEmail("fred@mail.com");

        UserDto userDto = oneUserDtoSample();
        userDto.setEmail("fred@mail.com");

        when(userService.findById(1L)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("yoga@studio.com"));
    }
}
