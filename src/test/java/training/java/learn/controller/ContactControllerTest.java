package training.java.learn.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import training.java.learn.Security.BCrypt;
import training.java.learn.dto.ContactResponse;
import training.java.learn.dto.CreateContactRequest;
import training.java.learn.dto.UpdateContactRequest;
import training.java.learn.dto.WebResponse;
import training.java.learn.entity.User;
import training.java.learn.repository.ContactRepository;
import training.java.learn.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        contactRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void createContactSuccess() throws Exception {
        User user = new User();
        user.setName("Putri");
        user.setUsername("putri");
        user.setPassword(BCrypt.hashpw("rahasia", BCrypt.gensalt()));
        user.setToken("tokens");
        user.setTokenExpiredAt(System.currentTimeMillis() + 1000000L);
        userRepository.save(user);

        CreateContactRequest request = new CreateContactRequest();
        request.setFirstName("Aril");
        request.setLastName("Fauzi");
        request.setEmail("aril@gmail.com");
        request.setPhone("089654582742");

        mockMvc.perform(
                post("/api/create/contact")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "tokens")
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<ContactResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNull(response.getErrors());
            assertNotNull(response.getData());

            assertEquals("Aril", response.getData().getFirstName());
        });
    }

    @Test
    void createContactUnauthorized() throws Exception {
        User user = new User();
        user.setName("Putri");
        user.setUsername("putri");
        user.setPassword(BCrypt.hashpw("rahasia", BCrypt.gensalt()));
        user.setToken("tokens");
        user.setTokenExpiredAt(System.currentTimeMillis() + 1000000L);
        userRepository.save(user);

        CreateContactRequest request = new CreateContactRequest();
        request.setFirstName("Aril");
        request.setLastName("Fauzi");
        request.setEmail("aril@gmail.com");
        request.setPhone("089654582742");

        mockMvc.perform(
                post("/api/create/contact")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void getContactSuccess() throws Exception {
        User user = new User();
        user.setName("Putri");
        user.setUsername("putri");
        user.setPassword(BCrypt.hashpw("rahasia", BCrypt.gensalt()));
        user.setToken("tokens");
        user.setTokenExpiredAt(System.currentTimeMillis() + 1000000L);
        userRepository.save(user);

        CreateContactRequest request = new CreateContactRequest();
        request.setFirstName("Aril");
        request.setLastName("Fauzi");
        request.setEmail("aril@gmail.com");
        request.setPhone("089654582742");

        mockMvc.perform(
                post("/api/create/contact")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "tokens")
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<ContactResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            mockMvc.perform(
                    get("/api/contact/" + response.getData().getId())
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("X-API-TOKEN", "tokens")
            ).andExpectAll(
                status().isOk()
            ).andDo(res -> {
                WebResponse<ContactResponse> resGetContact = objectMapper.readValue(res.getResponse().getContentAsString(), new TypeReference<>() {});

                assertNotNull(resGetContact.getData());
                assertNull(resGetContact.getErrors());
            });
        });
    }

    @Test
    void getContactFailed() throws Exception {
        User user = new User();
        user.setName("Putri");
        user.setUsername("putri");
        user.setPassword(BCrypt.hashpw("rahasia", BCrypt.gensalt()));
        user.setToken("tokens");
        user.setTokenExpiredAt(System.currentTimeMillis() + 1000000L);
        userRepository.save(user);

        CreateContactRequest request = new CreateContactRequest();
        request.setFirstName("Aril");
        request.setLastName("Fauzi");
        request.setEmail("aril@gmail.com");
        request.setPhone("089654582742");

        mockMvc.perform(
                post("/api/create/contact")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "tokens")
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<ContactResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            mockMvc.perform(
                    get("/api/contact/" + response.getData().getId())
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
            ).andExpectAll(
                    status().isUnauthorized()
            ).andDo(res -> {
                WebResponse<ContactResponse> resGetContact = objectMapper.readValue(res.getResponse().getContentAsString(), new TypeReference<>() {});

                assertNotNull(resGetContact.getErrors());
            });
        });
    }

    @Test
    void updateContactSuccess() throws Exception {
        User user = new User();
        user.setName("Ucup");
        user.setUsername("ucup");
        user.setPassword(BCrypt.hashpw("rahasia", BCrypt.gensalt()));
        user.setToken("tokens");
        user.setTokenExpiredAt(System.currentTimeMillis() + 1000000L);

        userRepository.save(user);

        CreateContactRequest request = new CreateContactRequest();
        request.setFirstName("Aril");
        request.setLastName("Fauzi");
        request.setEmail("aril@gmail.com");
        request.setPhone("089654582742");

        mockMvc.perform(
                post("/api/create/contact")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-TOKEN", "tokens")
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<ContactResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            UpdateContactRequest updateContactRequest = new UpdateContactRequest();
            updateContactRequest.setId(response.getData().getId());
            updateContactRequest.setFirstName("Aril update");
            updateContactRequest.setLastName("Fuazi update");
            updateContactRequest.setEmail("arilupdate@gmail.com");
            updateContactRequest.setPhone("08999289284");

            mockMvc.perform(
                    patch("/api/contact/update")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("X-API-TOKEN", "tokens")
                            .content(objectMapper.writeValueAsString(updateContactRequest))
            ).andExpectAll(
                    status().isOk()
            ).andDo(result1 -> {
                WebResponse<ContactResponse> res = objectMapper.readValue(result1.getResponse().getContentAsString(), new TypeReference<>() {
                });

                assertNull(res.getErrors());
                assertNotNull(res.getData());
            });

        });
    }


}