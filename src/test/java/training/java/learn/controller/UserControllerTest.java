package training.java.learn.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import training.java.learn.Security.BCrypt;
import training.java.learn.dto.RegisterUserRequest;
import training.java.learn.dto.UpdateUserRequest;
import training.java.learn.dto.UserResponse;
import training.java.learn.dto.WebResponse;
import training.java.learn.entity.User;
import training.java.learn.repository.ContactRepository;
import training.java.learn.repository.UserRepository;

import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    Faker faker = new Faker();

//    @BeforeEach
//    void setUp() {
//        contactRepository.deleteAll();
//        userRepository.deleteAll();
//    }

    @Test
    void testRegisterSuccess() throws Exception {

        RegisterUserRequest request = new RegisterUserRequest();
        request.setUsername(faker.name().name());
        request.setPassword("rahasia");
        request.setName(faker.name().fullName());

        mockMvc.perform(
                post("/api/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertEquals("OK", response.getData());
        });

    }

    @Test
    void testRegisterBadRequest() throws Exception {

        RegisterUserRequest request = new RegisterUserRequest();
        request.setUsername("");
        request.setPassword("");
        request.setName("");


        mockMvc.perform(
                post("/api/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNotNull(response.getMessage());
        });

    }

    @Test
    void testRegisterDuplicate() throws Exception {
        User user = new User();
        user.setName("Aril");
        user.setUsername("test");
        user.setPassword(BCrypt.hashpw("rahasia", BCrypt.gensalt()));
        userRepository.save(user);

        RegisterUserRequest request = new RegisterUserRequest();
        request.setUsername("test");
        request.setPassword("rahasia");
        request.setName("Aril");


        mockMvc.perform(
                post("/api/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNotNull(response.getMessage());
        });

    }

    @Test
    void getUserUnauthorized() throws Exception {
        mockMvc.perform(
                get("/api/users/current")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "notfound")
        ).andExpectAll(
            status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNotNull(response.getMessage());
        });
    }

    @Test
    void getUserUnauthorizedHeaderNotFound() throws Exception {
        mockMvc.perform(
                get("/api/users/current")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNotNull(response.getMessage());
        });
    }

    @Test
    void getUserSuccess() throws Exception {

        User user = userRepository.findFirstByUsername("putri");

        mockMvc.perform(
                get("/api/users/current")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", user.getToken())
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNull(response.getMessage());
            assertNotNull(response.getData());
        });
    }

    @Test
    void updateUserSuccess() throws Exception {
        User user = new User();
        user.setName("Aril");
        user.setUsername("aril");
        user.setPassword(BCrypt.hashpw("rahasia", BCrypt.gensalt()));
        user.setToken("tokens");
        user.setTokenExpiredAt(System.currentTimeMillis() + 1000000L);

        userRepository.save(user);

        UpdateUserRequest userRequest = new UpdateUserRequest();
        userRequest.setName("Aril Update");
        userRequest.setPassword("rahasia2");
        userRequest.setOld_password("rahasia");

        mockMvc.perform(
                patch("/api/user/update")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest))
                        .header("X-API-TOKEN", "tokens")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

            assertNull(response.getMessage());
            assertNotNull(response.getData());
        });
    }

    @Test
    void updateUserUnauthorized() throws Exception {
        User user = new User();
        user.setName("Aril");
        user.setUsername("aril");
        user.setPassword(BCrypt.hashpw("rahasia", BCrypt.gensalt()));
        user.setToken("tokens");
        user.setTokenExpiredAt(System.currentTimeMillis() + 1000000L);

        userRepository.save(user);

        UpdateUserRequest userRequest = new UpdateUserRequest();
        userRequest.setName("Aril Update");
        userRequest.setPassword("rahasia2");
        userRequest.setOld_password("rahasia");

        mockMvc.perform(
                patch("/api/user/update")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest))
                        .header("X-API-TOKEN", "notfound")
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

            assertNotNull(response.getMessage());
        });
    }

    @Test
    void listALlUser() throws Exception {
        User user = new User();
        user.setName("Aril");
        user.setUsername("aril");
        user.setPassword(BCrypt.hashpw("rahasia", BCrypt.gensalt()));
        user.setToken("tokens");
        user.setTokenExpiredAt(System.currentTimeMillis() + 1000000L);

        userRepository.save(user);

        mockMvc.perform(
                get("/api/users/gets")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "tokens")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<User>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNotNull(response.getData());
            assertNull(response.getMessage());
        });
    }
}